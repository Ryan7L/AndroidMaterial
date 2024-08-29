package io.material.catalog.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class DividerMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_divider_fragment, container, false)
  }
}
