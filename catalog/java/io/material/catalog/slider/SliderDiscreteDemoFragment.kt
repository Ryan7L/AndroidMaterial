package io.material.catalog.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.BasicLabelFormatter
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class SliderDiscreteDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_slider_demo_discrete, container, false)
    val withLabelFormatter = view.findViewById<Slider>(R.id.slider_4)
    withLabelFormatter.setLabelFormatter(BasicLabelFormatter())

    val sliders = listOf(
      view.findViewById<Slider>(R.id.slider_1),
      view.findViewById<Slider>(R.id.slider_2),
      view.findViewById<Slider>(R.id.slider_3),
      withLabelFormatter,
      view.findViewById<Slider>(R.id.slider_5),
      view.findViewById<Slider>(R.id.slider_6)
    )

    val switchButton = view.findViewById<View>(R.id.switch_button)
    switchButton.setOnClickListener {
      for (slider in sliders) {
        slider.isEnabled = !slider.isEnabled
      }
    }

    return view
  }
}
