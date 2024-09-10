package io.material.catalog.carousel

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.windowpreferences.WindowPreferencesManager

class FullScreenStrategyDemoFragment : DemoFragment() {
  private lateinit var verticalDivider: MaterialDividerItemDecoration
  private lateinit var bottomSheetDialog: BottomSheetDialog
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    return inflater.inflate(R.layout.cat_carousel_full_screen_fragment, container, false)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bottomSheetDialog = BottomSheetDialog(view.context)
    bottomSheetDialog.setContentView(R.layout.cat_carousel_bottom_sheet_contents)
    bottomSheetDialog.dismissWithAnimation = true
    WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(bottomSheetDialog.window)
    verticalDivider =
      MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)

    val showBottomSheetButton = view.findViewById<View>(R.id.show_bottomsheet_button)
    showBottomSheetButton.setOnClickListener { bottomSheetDialog.show() }

    val debugSwitch = bottomSheetDialog.findViewById<MaterialSwitch>(R.id.debug_switch)
    val drawDividers = bottomSheetDialog.findViewById<MaterialSwitch>(R.id.draw_dividers_switch)
    val enableFlingSwitch = bottomSheetDialog.findViewById<MaterialSwitch>(R.id.enable_fling_switch)
    val itemCountDropdown =
      bottomSheetDialog.findViewById<AutoCompleteTextView>(R.id.item_count_dropdown)
    val positionSlider = bottomSheetDialog.findViewById<Slider>(R.id.position_slider)

    val rv = view.findViewById<RecyclerView>(R.id.fullscreen_carousel_recycler_view)

    val layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy(), RecyclerView.VERTICAL)
    layoutManager.setDebuggingEnabled(rv, debugSwitch!!.isChecked)
    rv.layoutManager = layoutManager
    rv.isNestedScrollingEnabled = false
    debugSwitch.setOnCheckedChangeListener { _, isChecked ->
      layoutManager.setOrientation(CarouselLayoutManager.VERTICAL)
      rv.setBackgroundResource(if (isChecked) R.drawable.dashed_outline_rectangle else 0)
      layoutManager.setDebuggingEnabled(rv, isChecked)
    }
    drawDividers!!.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        rv.addItemDecoration(verticalDivider)
      } else {
        rv.removeItemDecoration(verticalDivider)
      }
    }
    val adapter = CarouselAdapter(
      { item, position -> rv.scrollToPosition(position) },
      R.layout.cat_carousel_item_vertical
    )
    rv.addOnScrollListener(object : OnScrollListener() {
      private var dragged = false
      override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
          dragged = true
        } else if (dragged && newState == RecyclerView.SCROLL_STATE_IDLE) {
          if (recyclerView.computeVerticalScrollRange() != 0) {
            positionSlider!!.value =
              ((adapter.itemCount - 1) * recyclerView.computeVerticalScrollOffset() / recyclerView.computeVerticalScrollRange() + 1).toFloat()
          }
          dragged = false
        }
      }
    })

    val flingDisabledSnapHelper = CarouselSnapHelper()
    val flingEnabledSnapHelper = CarouselSnapHelper(false)
    flingDisabledSnapHelper.attachToRecyclerView(rv)
    enableFlingSwitch!!.setOnCheckedChangeListener { _, isChecked ->
      if (isChecked) {
        flingDisabledSnapHelper.attachToRecyclerView(null)
        flingEnabledSnapHelper.attachToRecyclerView(rv)
      } else {
        flingEnabledSnapHelper.attachToRecyclerView(null)
        flingDisabledSnapHelper.attachToRecyclerView(rv)
      }
    }
    itemCountDropdown!!.setOnItemClickListener { _, _, position, _ ->
      adapter.submitList(
        createItems().subList(0, position),
        updateSliderRange(positionSlider!!, adapter)
      )
    }
    positionSlider?.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
      override fun onStartTrackingTouch(slider: Slider) {

      }

      override fun onStopTrackingTouch(slider: Slider) {
        rv.smoothScrollToPosition(slider.value.toInt() - 1)
      }
    })
    rv.adapter = adapter
    adapter.submitList(createItems(), updateSliderRange(positionSlider!!, adapter))
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
