package io.material.catalog.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class SliderScrollContainerDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_slider_demo_scroll, container, false)
    val sliderContainer = view.findViewById<ViewGroup>(R.id.sliderContainer)
    for (i in 0..49) {
      sliderContainer.addView(
        Slider(inflater.context),
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
      )

    }
    return view
  }
}
