package io.material.catalog.progressindicator

import android.view.View
import android.widget.AutoCompleteTextView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider
import io.material.catalog.R
import java.util.Locale

class ProgressIndicatorVisibilityDemoFragment : ProgressIndicatorDemoFragment() {

  private val BEHAVIOR_SHOW_NONE = 0
  private val BEHAVIOR_SHOW_OUTWARD = 1
  private val BEHAVIOR_SHOW_INWARD = 2
  private val BEHAVIOR_HIDE_NONE = 0
  private val BEHAVIOR_HIDE_OUTWARD = 1
  private val BEHAVIOR_HIDE_INWARD = 2
  private val BEHAVIOR_HIDE_ESCAP = 3

  private val showBehaviorCodes = mapOf(
    "none" to BEHAVIOR_SHOW_NONE,
    "outward" to BEHAVIOR_SHOW_OUTWARD,
    "inward" to BEHAVIOR_SHOW_INWARD
  )
  private val hideBehaviorCodes = mapOf(
    "none" to BEHAVIOR_HIDE_NONE,
    "outward" to BEHAVIOR_HIDE_OUTWARD,
    "inward" to BEHAVIOR_HIDE_INWARD,
    "escape" to BEHAVIOR_HIDE_ESCAP
  )
  private lateinit var linearIndicator: LinearProgressIndicator
  private lateinit var circularIndicator: CircularProgressIndicator
  override fun initDemoContents(view: View) {
    linearIndicator = view.findViewById(R.id.linear_indicator)
    circularIndicator = view.findViewById(R.id.circular_indicator)
  }

  override fun initDemoControls(view: View) {
    val progressSlider = view.findViewById<Slider>(R.id.progress_slider)
    val determinateSwitch = view.findViewById<MaterialSwitch>(R.id.determinate_mode_switch)
    progressSlider.addOnChangeListener { slider, value, fromUser ->
      if (!linearIndicator.isIndeterminate) {
        linearIndicator.setProgressCompat(value.toInt(), true)
      }
      if (!circularIndicator.isIndeterminate) {
        circularIndicator.setProgressCompat(value.toInt(), true)
      }
    }
    determinateSwitch.setOnCheckedChangeListener { buttonView, isChecked ->

      if (isChecked) {
        val progress = progressSlider.value
        linearIndicator.setProgressCompat(progress.toInt(), true)
        circularIndicator.setProgressCompat(progress.toInt(), true)
      } else {
        linearIndicator.setProgressCompat(0, false)
        circularIndicator.setProgressCompat(0, false)
        linearIndicator.setIndeterminate(true)
        circularIndicator.setIndeterminate(true)
      }
    }
    val showBehaviorInput = view.findViewById<AutoCompleteTextView>(R.id.showBehaviorDropdown)
    showBehaviorInput.setOnItemClickListener { parent, view, position, id ->
      val selectedId = showBehaviorInput.adapter.getItem(position) as String
      val showBehaviorCode = showBehaviorCodes[selectedId.lowercase(Locale.US)] ?: 0
      linearIndicator.setShowAnimationBehavior(showBehaviorCode)
      circularIndicator.setShowAnimationBehavior(showBehaviorCode)
    }
    val hideBehaviorInput = view.findViewById<AutoCompleteTextView>(R.id.hideBehaviorDropdown)
    hideBehaviorInput.setOnItemClickListener { parent, view, position, id ->
      val selectedId = hideBehaviorInput.adapter.getItem(position) as String
      val hideBehaviorCode = hideBehaviorCodes[selectedId.lowercase(Locale.US)] ?: 0
      linearIndicator.setHideAnimationBehavior(hideBehaviorCode)
      circularIndicator.setHideAnimationBehavior(hideBehaviorCode)
    }
    val showBtn = view.findViewById<View>(R.id.showButton)
    showBtn.setOnClickListener {
      if (linearIndicator.visibility != View.VISIBLE) {
        linearIndicator.show()
      }
      if (circularIndicator.visibility != View.VISIBLE) {
        circularIndicator.show()
      }
    }
    val hideBtn = view.findViewById<View>(R.id.hideButton)
    hideBtn.setOnClickListener {
      if (linearIndicator.visibility == View.VISIBLE) {
        linearIndicator.hide()
      }
      if (circularIndicator.visibility == View.VISIBLE) {
        circularIndicator.hide()
      }
    }
  }


  override val progressIndicatorContentLayout: Int
    get() = R.layout.cat_progress_indicator_main_content
  override val progressIndicatorDemoControlLayout: Int
    get() = R.layout.cat_progress_indicator_visibility_controls
}

