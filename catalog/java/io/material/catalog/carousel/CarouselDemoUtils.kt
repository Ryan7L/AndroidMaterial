package io.material.catalog.carousel

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import kotlin.math.abs

fun createUpdateSliderOnScrollListener(
  slider: Slider,
  adapter: CarouselAdapter
): RecyclerView.OnScrollListener {
  return object : RecyclerView.OnScrollListener() {
    private var dragged = false
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
      if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
        dragged = true
      } else if (dragged && newState == RecyclerView.SCROLL_STATE_IDLE) {
        if (recyclerView.computeHorizontalScrollRange() != 0) {
          slider.value =
            ((adapter.itemCount - 1) * abs(recyclerView.computeHorizontalScrollOffset()) / recyclerView.computeHorizontalScrollRange() + 1).toFloat()
        }
        dragged = false
      }
    }
  }
}

fun createScrollToPositionSliderTouchListener(recyclerView: RecyclerView): OnSliderTouchListener {
  return object : OnSliderTouchListener {
    override fun onStartTrackingTouch(slider: Slider) {

    }

    override fun onStopTrackingTouch(slider: Slider) {
      recyclerView.smoothScrollToPosition(slider.value.toInt() - 1)
    }

  }
}
