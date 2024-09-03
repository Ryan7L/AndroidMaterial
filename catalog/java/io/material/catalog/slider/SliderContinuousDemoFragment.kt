package io.material.catalog.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class SliderContinuousDemoFragment: DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
   val view = inflater.inflate(R.layout.cat_slider_demo_continuous, container, false)
    val sliderOne = view.findViewById<Slider>(R.id.slider_1)
    val sliderTwo = view.findViewById<Slider>(R.id.slider_2)
    val sliderThree = view.findViewById<Slider>(R.id.slider_3)
    val switchButton = view.findViewById<View>(R.id.switch_button)
    switchButton.setOnClickListener {
      sliderOne.isEnabled = !sliderOne.isEnabled
      sliderTwo.isEnabled = !sliderTwo.isEnabled
      sliderThree.isEnabled = !sliderThree.isEnabled
    }
    setUpSlider(sliderOne,view.findViewById(R.id.value_1),"%.0f")
    setUpSlider(sliderTwo,view.findViewById(R.id.value_2),"%.0f")
    setUpSlider(sliderThree,view.findViewById(R.id.value_3),"%.2f")
    return view
  }
  private fun setUpSlider(slider: Slider, textView: TextView, valueFormat: String){
    slider.addOnChangeListener { slider1, value, fromUser ->
      textView.text = String.format(valueFormat,value)
    }
    slider.value = slider.valueFrom
  }
}
