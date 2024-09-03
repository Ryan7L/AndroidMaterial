package io.material.catalog.navigationrail

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class NavigationRailAnimatedDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_navigation_rail_animated, container, false)
    val config = resources.configuration
    val msg = view.findViewById<View>(R.id.cat_navigation_rail_compact_msg)
    if (config.smallestScreenWidthDp <= 600) {//// 600dp及以下属于紧凑型
      msg.visibility = View.VISIBLE
      activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    } else {
      activity?.requestedOrientation = config.orientation
    }
    return view
  }
}
