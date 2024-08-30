package io.material.catalog.materialswitch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class SwitchMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_material_switch, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val toggledView = view.findViewById<View>(R.id.toggled_views)
    val toggledSwitches = DemoUtils.findViewsWithType(toggledView, MaterialSwitch::class.java)
    val switchToggle = view.findViewById<MaterialSwitch>(R.id.switch_toggle)
    switchToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      for (materialSwitch in toggledSwitches) {
        materialSwitch.setEnabled(isChecked)
      }
    }
  }
}
