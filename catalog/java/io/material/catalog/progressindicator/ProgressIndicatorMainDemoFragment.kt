package io.material.catalog.progressindicator

import android.view.View
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider
import io.material.catalog.R

class ProgressIndicatorMainDemoFragment : ProgressIndicatorDemoFragment() {

  private lateinit var linearProgressIndicator: LinearProgressIndicator
  private lateinit var circularIndicator: CircularProgressIndicator

  override fun initDemoContents(view: View) {
    linearProgressIndicator = view.findViewById(R.id.linear_indicator)
    circularIndicator = view.findViewById(R.id.circular_indicator)
  }

  override fun initDemoControls(view: View) {
    val progressSlider = view.findViewById<Slider>(R.id.progress_slider)
    val determinateSwitch = view.findViewById<MaterialSwitch>(R.id.determinate_mode_switch)
    progressSlider.addOnChangeListener { slider, value, fromUser ->
      if (!linearProgressIndicator.isIndeterminate) {
        linearProgressIndicator.setProgressCompat(value.toInt(), true)
      }
      if (!circularIndicator.isIndeterminate) {
        circularIndicator.setProgressCompat(value.toInt(), true)
      }
    }
    determinateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked) {
        val progress = progressSlider.value
        linearProgressIndicator.setProgressCompat(progress.toInt(), true)
        circularIndicator.setProgressCompat(progress.toInt(), true)
      } else {
        linearProgressIndicator.setProgressCompat(0, false)
        circularIndicator.setProgressCompat(0, false)
        linearProgressIndicator.setIndeterminate(true)
        circularIndicator.setIndeterminate(true)
      }
    }
    val pixelsInDp = view.resources.displayMetrics.density
    val thicknessSlider = view.findViewById<Slider>(R.id.thicknessSlider)
    thicknessSlider.addOnChangeListener { slider, value, fromUser ->
      val newThickness = Math.round(value * pixelsInDp)
      if (linearProgressIndicator.trackThickness != newThickness) {
        linearProgressIndicator.trackThickness = newThickness
      }
      if (circularIndicator.trackThickness != newThickness) {
        circularIndicator.trackThickness = newThickness
      }
    }
    val cornerSlider = view.findViewById<Slider>(R.id.cornerSlider)
    cornerSlider.addOnChangeListener { slider, value, fromUser ->
      val newCornerRadius = Math.round(value * pixelsInDp)
      if (linearProgressIndicator.trackCornerRadius != newCornerRadius) {
        linearProgressIndicator.trackCornerRadius = newCornerRadius
      }
      if (circularIndicator.trackCornerRadius != newCornerRadius) {
        circularIndicator.trackCornerRadius = newCornerRadius
      }
    }
    val gasSlider = view.findViewById<Slider>(R.id.gapSlider)
    gasSlider.addOnChangeListener { slider, value, fromUser ->
      val newGapSize = Math.round(value * pixelsInDp)
      if (linearProgressIndicator.indicatorTrackGapSize != newGapSize) {
        linearProgressIndicator.indicatorTrackGapSize = newGapSize
      }
      if (circularIndicator.indicatorTrackGapSize != newGapSize) {
        circularIndicator.indicatorTrackGapSize = newGapSize
      }
    }
    val reverseSwitch = view.findViewById<MaterialSwitch>(R.id.reverseSwitch)
    reverseSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

      linearProgressIndicator.setIndicatorDirection(if (isChecked) LinearProgressIndicator.INDICATOR_DIRECTION_RIGHT_TO_LEFT else LinearProgressIndicator.INDICATOR_DIRECTION_LEFT_TO_RIGHT)
      circularIndicator.setIndicatorColor(if (isChecked) CircularProgressIndicator.INDICATOR_DIRECTION_COUNTERCLOCKWISE else CircularProgressIndicator.INDICATOR_DIRECTION_CLOCKWISE)
    }

    val linearStopIndicatorSlider = view.findViewById<Slider>(R.id.linearStopIndicatorSizeSlider)
    linearStopIndicatorSlider.addOnChangeListener { slider, value, fromUser ->
      val newStopIndicatorSize = Math.round(value * pixelsInDp)
      if (linearProgressIndicator.trackStopIndicatorSize != newStopIndicatorSize) {
        linearProgressIndicator.trackStopIndicatorSize = newStopIndicatorSize
      }
    }
    val circularSizeSlider = view.findViewById<Slider>(R.id.circularSizeSlider)
    circularSizeSlider.addOnChangeListener { slider, value, fromUser ->
      val newCornerRadius = Math.round(value * pixelsInDp)
      if (circularIndicator.indicatorSize != newCornerRadius) {
        circularIndicator.indicatorSize = newCornerRadius
      }
    }
  }

  override val progressIndicatorContentLayout: Int
    get() = R.layout.cat_progress_indicator_main_content
  override val progressIndicatorDemoControlLayout: Int
    get() = R.layout.cat_progress_indicator_basic_controls
}
