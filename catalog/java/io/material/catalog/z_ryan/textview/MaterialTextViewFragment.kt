package io.material.catalog.z_ryan.textview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class MaterialTextViewFragment: DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_text_view_material_text_view_fragment, container, false)

    return view
  }
}
