package io.material.catalog.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonGroup
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ButtonGroupDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_buttons_group_fragment, container, false)

    val content = view.findViewById<ViewGroup>(R.id.button_group_content)
    inflater.inflate(iconOnlyButtonGroupContent, content, true)
    inflater.inflate(labelOnlyButtonGroupContent, content, true)
    inflater.inflate(mixedButtonGroupContent, content, true)

    val buttonGroups = DemoUtils.findViewsWithType(view, MaterialButtonGroup::class.java)

    val verticalOrientationToggle =
      view.findViewById<MaterialSwitch>(R.id.orientation_switch_toggle)
    verticalOrientationToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      buttonGroups.forEach {
        val orientation =
          if (isChecked) MaterialButtonGroup.VERTICAL else MaterialButtonGroup.HORIZONTAL
        it.orientation = orientation
        it.requestLayout()
      }
    }
    val groupEnabledToggle = view.findViewById<MaterialSwitch>(R.id.switch_enable)
    groupEnabledToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      buttonGroups.forEach { it.isEnabled = isChecked }
    }
    return view
  }
//  for (i in 0 until it.childCount) {
//    val inset = getInsetForOrientation(orientation)
//    val button = it.getChildAt(i) as MaterialButton
//    button.insetBottom = inset
//    button.insetTop = inset
//    adjustParams(button.layoutParams, orientation)
//  }
//  private fun getInsetForOrientation(orientation: Int): Int {
//    return if (orientation == MaterialButtonGroup.VERTICAL) 0 else defaultInset
//  }

//  private fun adjustParams(layoutParams: ViewGroup.LayoutParams, orientation: Int) {
//    layoutParams.width =
//      if (orientation == MaterialButtonGroup.VERTICAL) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
//  }

  private val iconOnlyButtonGroupContent = R.layout.cat_button_group_content_icon_only

  private val labelOnlyButtonGroupContent = R.layout.cat_button_group_content_label_only
  private val mixedButtonGroupContent = R.layout.cat_button_group_content_mixed
}
