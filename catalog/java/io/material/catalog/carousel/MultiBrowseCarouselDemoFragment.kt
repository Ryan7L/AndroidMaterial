package io.material.catalog.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class MultiBrowseCarouselDemoFragment : DemoFragment() {
  private lateinit var horizontalDivider: MaterialDividerItemDecoration
  private var adapter: CarouselAdapter? = null
  private lateinit var positionSlider: Slider

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_carousel_multi_browse_fragment, container, false)
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

    val rv = view.findViewById<RecyclerView>(R.id.multi_browse_start_carousel_recycler_view)
    val layoutManager = CarouselLayoutManager()
    layoutManager.setDebuggingEnabled(rv, debugSwitch.isChecked)
    rv.layoutManager = layoutManager
    rv.isNestedScrollingEnabled = false
    debugSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      rv.setBackgroundResource(if (isChecked) R.drawable.dashed_outline_rectangle else 0)
      layoutManager.setDebuggingEnabled(rv, isChecked)
    }
    drawDividers.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked) {
        rv.addItemDecoration(horizontalDivider)
      } else {
        rv.removeItemDecoration(horizontalDivider)
      }
    }
    //CarouselSnapHelper 是 Android 中的一个辅助类，用于在 RecyclerView 中实现类似轮播图的吸附效果。它可以帮助你将
    // RecyclerView 的 item 自动吸附到屏幕的中心位置，从而创建一种类似 ViewPager 的滑动体验。
    val snapHelper = CarouselSnapHelper()
    snapSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked) {
        snapHelper.attachToRecyclerView(rv)
      } else {
        snapHelper.attachToRecyclerView(null)
      }
    }
    adapter = CarouselAdapter({ item, position ->
      rv.scrollToPosition(position)
      positionSlider.value = position + 1f
    }, R.layout.cat_carousel_item_narrow)
    rv.addOnScrollListener(createUpdateSliderOnScrollListener(positionSlider, adapter!!))
    itemCountDropdown.setOnItemClickListener { parent, view1, position, id ->
      adapter?.submitList(
        createItems().subList(0, position),
        updateSliderRange(positionSlider, adapter!!)
      )
    }
    positionSlider.addOnSliderTouchListener(createScrollToPositionSliderTouchListener(rv))
    rv.adapter = adapter
  }

  override fun onStart() {
    super.onStart()
    adapter?.submitList(createItems(), updateSliderRange(positionSlider, adapter!!))
  }

  private fun updateSliderRange(slider: Slider, adapter: CarouselAdapter): Runnable {
    return Runnable {
      if (adapter.itemCount <= 1) {
        slider.isEnabled = false
        return@Runnable
      }
      slider.valueFrom = 1f
      slider.valueTo = adapter.itemCount.toFloat()
      slider.isEnabled = true
      slider.value = 1f
    }
  }
}
