package io.material.catalog.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnSliderTouchListener
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class SliderMainDemoFragment: DemoFragment() {
  private val touchListener = object : OnSliderTouchListener {
    override fun onStartTrackingTouch(slider: Slider) {
      showSnackBar(slider, R.string.cat_slider_start_touch_description)
    }

    override fun onStopTrackingTouch(slider: Slider) {
      showSnackBar(slider, R.string.cat_slider_stop_touch_description)
    }
  }
  private val rangeSliderTouchListener = object : RangeSlider.OnSliderTouchListener {
    override fun onStartTrackingTouch(slider: RangeSlider) {
      showSnackBar(slider, R.string.cat_slider_start_touch_description)
    }

    override fun onStopTrackingTouch(slider: RangeSlider) {
      showSnackBar(slider, R.string.cat_slider_stop_touch_description)
    }
  }
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_slider_fragment, container, false)
    val slider = view.findViewById<Slider>(R.id.slider)
    slider.addOnSliderTouchListener(touchListener)
    val rangeSlider = view.findViewById<RangeSlider>(R.id.range_slider)
    rangeSlider.addOnSliderTouchListener(rangeSliderTouchListener)
    val rangeSliderMulti = view.findViewById<RangeSlider>(R.id.range_slider_multi)
    rangeSliderMulti.addOnSliderTouchListener(rangeSliderTouchListener)
    val button = view.findViewById<View>(R.id.button)
    button.setOnClickListener { slider.value = slider.valueTo }
    return view
  }
  private fun showSnackBar(view: View, messageRes: Int){
    Snackbar.make(view,messageRes,Snackbar.LENGTH_SHORT).show()
  }
}
