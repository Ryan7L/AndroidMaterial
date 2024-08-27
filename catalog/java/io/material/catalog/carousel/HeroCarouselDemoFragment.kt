package io.material.catalog.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.HeroCarouselStrategy
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class HeroCarouselDemoFragment : DemoFragment() {
  private lateinit var horizontalDivider: MaterialDividerItemDecoration
  private lateinit var adapter: CarouselAdapter
  private lateinit var positionSlider: Slider

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_carousel_hero_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    horizontalDivider =
      MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.HORIZONTAL)

    val debugSwitch = view.findViewById<MaterialSwitch>(R.id.debug_switch)
    val drawDividers = view.findViewById<MaterialSwitch>(R.id.draw_dividers_switch)
    val enableFlingSwitch = view.findViewById<MaterialSwitch>(R.id.enable_fling_switch)
    val itemCountDropdown = view.findViewById<AutoCompleteTextView>(R.id.item_count_dropdown)
    positionSlider = view.findViewById(R.id.position_slider)
    val startAlignButton = view.findViewById<RadioButton>(R.id.start_align)
    val centerAlignButton = view.findViewById<RadioButton>(R.id.center_align)

    val rv = view.findViewById<RecyclerView>(R.id.hero_start_carousel_recycler_view)
    val layoutManager = CarouselLayoutManager(HeroCarouselStrategy())
    layoutManager.setDebuggingEnabled(rv, debugSwitch.isChecked)
    rv.layoutManager = layoutManager
    rv.isNestedScrollingEnabled = false

    debugSwitch.setOnCheckedChangeListener { _, isChecked ->
      rv.setBackgroundResource(if (isChecked) R.drawable.dashed_outline_rectangle else 0)
      layoutManager.setDebuggingEnabled(rv, isChecked)
    }

    drawDividers.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        rv.addItemDecoration(horizontalDivider)
      } else {
        rv.removeItemDecoration(horizontalDivider)
      }
    }
    adapter = CarouselAdapter({ item, position ->
      rv.scrollToPosition(position)
      positionSlider.value = position + 1f
    }, R.layout.cat_carousel_item)
    rv.addOnScrollListener(createUpdateSliderOnScrollListener(positionSlider, adapter))
    val disableFlingSnapHelper = CarouselSnapHelper()
    val enableFlingSnapHelper = CarouselSnapHelper(false)
    if (enableFlingSwitch.isChecked) {
      enableFlingSnapHelper.attachToRecyclerView(rv)
    } else {
      disableFlingSnapHelper.attachToRecyclerView(rv)
    }
    enableFlingSwitch.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        disableFlingSnapHelper.attachToRecyclerView(null)
        enableFlingSnapHelper.attachToRecyclerView(rv)
      } else {
        enableFlingSnapHelper.attachToRecyclerView(null)
        disableFlingSnapHelper.attachToRecyclerView(rv)
      }
    }
    itemCountDropdown.setOnItemClickListener { _, _, position, _ ->
      adapter.submitList(
        createItems().subList(0, position),
        updateSliderRange(positionSlider, adapter)
      )
    }
    positionSlider.addOnSliderTouchListener(createScrollToPositionSliderTouchListener(rv))
    startAlignButton.setOnClickListener { layoutManager.setCarouselAlignment(CarouselLayoutManager.ALIGNMENT_START) }
    centerAlignButton.setOnClickListener {
      layoutManager.setCarouselAlignment(
        CarouselLayoutManager.ALIGNMENT_CENTER
      )
    }
    rv.adapter = adapter
  }

  override fun onStart() {
    super.onStart()
    adapter.submitList(createItems(), updateSliderRange(positionSlider, adapter))
  }

  private fun updateSliderRange(slider: Slider, adapter: CarouselAdapter): Runnable {
    return Runnable {
      if (adapter.itemCount <= 1) {
        slider.isEnabled = false
        return@Runnable
      }
      slider.valueFrom = 1f
      slider.value = 1f
      slider.valueTo = adapter.itemCount.toFloat()
      slider.isEnabled = true
    }
  }
}
