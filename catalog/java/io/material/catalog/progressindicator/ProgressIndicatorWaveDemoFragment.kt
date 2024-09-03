package io.material.catalog.progressindicator

import android.view.View
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider
import io.material.catalog.R

class ProgressIndicatorWaveDemoFragment: ProgressIndicatorDemoFragment() {
  private lateinit var linearIndicator: LinearProgressIndicator
  private lateinit var circularIndicator: CircularProgressIndicator
  override fun initDemoContents(view: View) {
    linearIndicator = view.findViewById(R.id.linear_indicator)
    circularIndicator = view.findViewById(R.id.circular_indicator)
  }

  override fun initDemoControls(view: View) {
    val progressSlider = view.findViewById<Slider>(R.id.progress_slider)
    val determinateSwitch = view.findViewById<MaterialSwitch>(R.id.determinate_mode_switch)
    val circularAnimationMode = view.findViewById<MaterialButtonToggleGroup>(R.id.circular_animation_mode)
    progressSlider.addOnChangeListener { slider, value, fromUser ->
      if (!linearIndicator.isIndeterminate) {
        linearIndicator.setProgressCompat(value.toInt(), true)
      }
      if (!circularIndicator.isIndeterminate) {
        circularIndicator.setProgressCompat(value.toInt(), true)
    }}
    determinateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked) {
        val progress = progressSlider.value
        linearIndicator.setProgressCompat(progress.toInt(), true)
        circularIndicator.setProgressCompat(progress.toInt(), true)
      }else{
        linearIndicator.setProgressCompat(0, false)
        circularIndicator.setProgressCompat(0, false)
        linearIndicator.setIndeterminate(true)
        circularIndicator.setIndeterminate(true)
      }
      circularAnimationMode.setEnabled(isChecked)
    }
    val pixelsInDp = view.resources.displayMetrics.density
    val amplitudeSlider = view.findViewById<Slider>(R.id.amplitude_slider)
    amplitudeSlider.addOnChangeListener { slider, value, fromUser ->
      val newAmplitude = Math.round(value * pixelsInDp)
      if (linearIndicator.getWaveAmplitude() != newAmplitude) {
        linearIndicator.setWaveAmplitude(newAmplitude)
      }
      if (circularIndicator.getWaveAmplitude() != newAmplitude) {
        circularIndicator.setWaveAmplitude(newAmplitude)}
    }
    val waveLengthSlider = view.findViewById<Slider>(R.id.wavelength_slider)
    waveLengthSlider.addOnChangeListener { slider, value, fromUser ->
      val newWaveLength = Math.round(value * pixelsInDp)
      linearIndicator.setWavelength(newWaveLength)
      circularIndicator.setWavelength(newWaveLength)
    }
    val speedSlider = view.findViewById<Slider>(R.id.speed_slider)
    speedSlider.addOnChangeListener { slider, value, fromUser ->
      val newSpeed = Math.round(value * pixelsInDp)
      if (linearIndicator.waveSpeed != newSpeed) {
        linearIndicator.setWaveSpeed(newSpeed)
      }
      if (circularIndicator.waveSpeed != newSpeed) {
        circularIndicator.setWaveSpeed(newSpeed)
      }
    }
    val circularSizeSlider = view.findViewById<Slider>(R.id.circular_size_slider)
    circularSizeSlider.addOnChangeListener { slider, value, fromUser ->
      val newCornerRadius = Math.round(value * pixelsInDp)
      if (circularIndicator.indicatorSize != newCornerRadius) {
        circularIndicator.indicatorSize = newCornerRadius
      }
    }
    circularAnimationMode.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (!isChecked) {
        return@addOnButtonCheckedListener}
      if (checkedId == R.id.circular_advance_animation) {
        circularIndicator.setIndeterminateAnimationType(CircularProgressIndicator.INDETERMINATE_ANIMATION_TYPE_ADVANCE)
      }else{
        circularIndicator.setIndeterminateAnimationType(CircularProgressIndicator.INDETERMINATE_ANIMATION_TYPE_RETREAT)
      }
    }
  }
  override val progressIndicatorContentLayout: Int
    get() = R.layout.cat_progress_indicator_main_content

  override val progressIndicatorDemoControlLayout: Int
    get() = R.layout.cat_progress_indicator_wave_controls
}
