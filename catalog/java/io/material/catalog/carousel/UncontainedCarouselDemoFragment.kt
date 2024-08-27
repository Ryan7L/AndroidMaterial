package io.material.catalog.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.UncontainedCarouselStrategy
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class UncontainedCarouselDemoFragment : DemoFragment() {
  private lateinit var horizontalDivider: MaterialDividerItemDecoration
  private lateinit var adapter: CarouselAdapter
  private lateinit var positionSlider: Slider

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_carousel_uncontained_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    horizontalDivider =
      MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.HORIZONTAL)
    val debugSwitch = view.findViewById<MaterialSwitch>(R.id.debug_switch)
    val drawDividers = view.findViewById<MaterialSwitch>(R.id.draw_dividers_switch)
    val snapSwitch = view.findViewById<MaterialSwitch>(R.id.snap_switch)
    val itemCountDropdown = view.findViewById<AutoCompleteTextView>(R.id.item_count_dropdown)
    positionSlider = view.findViewById(R.id.position_slider)

    val rv = view.findViewById<RecyclerView>(R.id.uncontained_carousel_recycler_view)
    val layoutManager = CarouselLayoutManager(UncontainedCarouselStrategy())
    layoutManager.setDebuggingEnabled(rv, debugSwitch.isChecked)
    rv.layoutManager = layoutManager
    rv.isNestedScrollingEnabled = false
    debugSwitch.setOnCheckedChangeListener { _, isChecked ->
      rv.setBackgroundResource(if (isChecked) R.drawable.dashed_outline_rectangle else 0)
      layoutManager.setDebuggingEnabled(rv, isChecked)
    }
    drawDividers!!.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        rv.addItemDecoration(horizontalDivider)
      } else {
        rv.removeItemDecoration(horizontalDivider)
      }
    }
    val snapHelper = CarouselSnapHelper()
    snapSwitch.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        snapHelper.attachToRecyclerView(rv)
      } else {
        snapHelper.attachToRecyclerView(null)
      }
    }
    adapter = CarouselAdapter({ item, position ->
      rv.scrollToPosition(position)
      positionSlider.value = (position.toFloat() + 1)
    }, R.layout.cat_carousel_item_narrow)
    rv.addOnScrollListener(createUpdateSliderOnScrollListener(positionSlider, adapter))
    itemCountDropdown!!.setOnItemClickListener { _, _, position, _ ->
      adapter.submitList(
        createItems().subList(0, position),
        updateSliderRange(positionSlider, adapter)
      )

    }
    positionSlider.addOnSliderTouchListener(createScrollToPositionSliderTouchListener(rv))
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
