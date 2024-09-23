package io.material.catalog.navigationrail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class NavigationRailSubMenuDemoFragment: DemoFragment() {
  lateinit var navigationRail: NavigationRailView
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_navigation_rail_submenus_fragment, container, false)
    navigationRail = view.findViewById(R.id.cat_navigation_rail)
    return view
  }
}
