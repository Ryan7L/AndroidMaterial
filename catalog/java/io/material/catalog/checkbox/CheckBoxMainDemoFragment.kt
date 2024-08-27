package io.material.catalog.checkbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.google.android.material.checkbox.MaterialCheckBox
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class CheckBoxMainDemoFragment : DemoFragment() {
  private var isUpdateChildren = false

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_checkbox, container, false)

    val toggleContainer = view.findViewById<ViewGroup>(R.id.checkbox_toggle_container)
    val toggledCheckBoxes = DemoUtils.findViewsWithType(toggleContainer, CheckBox::class.java)
    val allCheckBoxes = DemoUtils.findViewsWithType(view, MaterialCheckBox::class.java)
    val checkBoxToggle = view.findViewById<CheckBox>(R.id.checkbox_toggle)
    checkBoxToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      toggledCheckBoxes.forEach {
        it.isEnabled = isChecked
      }
    }

    val checkBoxToggleError = view.findViewById<CheckBox>(R.id.checkbox_toggle_error)
    checkBoxToggleError.setOnCheckedChangeListener { buttonView, isChecked ->
      allCheckBoxes.forEach {
        it.setErrorShown(isChecked)
      }
    }

    val firstChild = view.findViewById<CheckBox>(R.id.checkbox_child_1)
    firstChild.isChecked = true
    val indeterminateContainer = view.findViewById<ViewGroup>(R.id.checkbox_indeterminate_container)
    val childrenCheckBoxes =
      DemoUtils.findViewsWithType(indeterminateContainer, CheckBox::class.java)

    val checkBoxParent = view.findViewById<MaterialCheckBox>(R.id.checkbox_parent)
    val parentOnCheckedStateChangedListener =
      MaterialCheckBox.OnCheckedStateChangedListener { checkBox, state ->
        val isChecked = checkBox.isChecked
        if (state != MaterialCheckBox.STATE_INDETERMINATE) {
          isUpdateChildren = true
          childrenCheckBoxes.forEach {
            it.isChecked = isChecked
          }
          isUpdateChildren = false
        }
      }
    checkBoxParent.addOnCheckedStateChangedListener(parentOnCheckedStateChangedListener)

    val childOnCheckedStateChangedListener =
      MaterialCheckBox.OnCheckedStateChangedListener { checkBox, state ->
        if (isUpdateChildren) {
          return@OnCheckedStateChangedListener
        }
        setParentState(checkBoxParent, childrenCheckBoxes, parentOnCheckedStateChangedListener)
      }

    childrenCheckBoxes.forEach {
      (it as MaterialCheckBox).addOnCheckedStateChangedListener(childOnCheckedStateChangedListener)
    }
    setParentState(checkBoxParent, childrenCheckBoxes, parentOnCheckedStateChangedListener)
    return view
  }

  private fun setParentState(
    checkBoxParent: MaterialCheckBox,
    childrenCheckBoxes: List<CheckBox>,
    parentOnCheckedStateChangedListener: MaterialCheckBox.OnCheckedStateChangedListener
  ) {
    var allChecked = true
    var noneChecked = true
    childrenCheckBoxes.forEach {
      if (!it.isChecked) {
        allChecked = false
      } else {
        noneChecked = false
      }
//      if (!allChecked && !noneChecked){
//        break
//      }
    }
    checkBoxParent.removeOnCheckedStateChangedListener(parentOnCheckedStateChangedListener)
    if (allChecked) {
      checkBoxParent.isChecked = true
    } else if (noneChecked) {
      checkBoxParent.isChecked = false
    } else {
      checkBoxParent.checkedState = MaterialCheckBox.STATE_INDETERMINATE
    }
    checkBoxParent.addOnCheckedStateChangedListener(parentOnCheckedStateChangedListener)
  }
}
