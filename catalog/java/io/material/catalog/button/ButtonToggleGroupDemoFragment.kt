package io.material.catalog.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.shape.RelativeCornerSize
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ButtonToggleGroupDemoFragment : DemoFragment() {
  private var defaultInset: Int = 0
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(buttonToggleGroupContent, container, false)
    val requireSelectionToggle = view.findViewById<MaterialSwitch>(R.id.switch_toggle)
    defaultInset = resources.getDimensionPixelSize(R.dimen.mtrl_btn_inset)
    val toggleGroups = DemoUtils.findViewsWithType(view, MaterialButtonToggleGroup::class.java)
    requireSelectionToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      toggleGroups.forEach {
        it.isSelectionRequired = isChecked
      }
    }

    val verticalOrientationToggle =
      view.findViewById<MaterialSwitch>(R.id.orientation_switch_toggle)
    verticalOrientationToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      toggleGroups.forEach {
        val orientation = if (isChecked) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL
        it.orientation = orientation
        for (i in 0 until it.childCount) {
          val insets = getInsetForOrientation(orientation)
          val button = it.getChildAt(i) as MaterialButton
          button.insetTop = insets
          button.insetBottom = insets
          adjustParams(button.layoutParams, orientation)
        }
        it.requestLayout()
      }
    }

    val groupEnabledToggle = view.findViewById<MaterialSwitch>(R.id.switch_enable)
    groupEnabledToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      toggleGroups.forEach {
        it.isEnabled = isChecked
      }
    }
    toggleGroups.forEach {
      it.addOnButtonCheckedListener { group, checkedId, isChecked ->
        val msg = "button ${if (isChecked) "checked" else "unchecked"}"
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
      }
    }

    val innerCornerSizeSlider = view.findViewById<Slider>(R.id.innerCornerSizeSlider)
    innerCornerSizeSlider.addOnChangeListener { slider, value, fromUser ->
      toggleGroups.forEach {
        it.innerCornerSize = RelativeCornerSize(value / 100f)
      }
    }
    val spacingSlider = view.findViewById<Slider>(R.id.spacingSlider)
    spacingSlider.addOnChangeListener { slider, value, fromUser ->
      val pixelsInDp = view.resources.displayMetrics.density
      toggleGroups.forEach {
        it.spacing = (value * pixelsInDp).toInt()
      }
    }
    return view
  }

  private fun getInsetForOrientation(orientation: Int) =
    if (orientation == LinearLayout.VERTICAL) 0 else defaultInset

  private fun adjustParams(layoutParams: ViewGroup.LayoutParams, orientation: Int) {
    layoutParams.width =
      if (orientation == LinearLayout.VERTICAL) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
  }

  @LayoutRes
  private val buttonToggleGroupContent: Int = R.layout.cat_buttons_toggle_group_fragment
}
