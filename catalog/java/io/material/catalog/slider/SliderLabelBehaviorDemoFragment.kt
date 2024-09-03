package io.material.catalog.slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class SliderLabelBehaviorDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_slider_demo_label_behavior, container, false)
    setUpSlider(view, R.id.switch_button_1, R.id.slider_1)
    setUpSlider(view, R.id.switch_button_2, R.id.slider_2)
    setUpSlider(view, R.id.switch_button_3, R.id.slider_3)
    setUpSlider(view, R.id.switch_button_4, R.id.slider_4)

    return view
  }

  private fun setUpSlider(view: View, switchId: Int, sliderId: Int) {
    val slider = view.findViewById<RangeSlider>(sliderId)
    val switchButton = view.findViewById<SwitchCompat>(switchId)
    switchButton.setOnCheckedChangeListener { buttonView, isChecked ->
      slider.setEnabled(isChecked)
    }
    switchButton.isChecked = true
  }
}
