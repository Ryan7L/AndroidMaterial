package io.material.catalog.bottomnav

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class BottomNavigationAnimatedDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_bottom_navs_animated, container, false)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = super.onCreateView(inflater, container, savedInstanceState)
    val coordinatorLayout = view?.findViewById<CoordinatorLayout>(R.id.cat_demo_fragment_container)
    // 由于未知原因，xml中的设置被清除，但此处设置生效。
    val animatedContainer =
      coordinatorLayout?.findViewById<View>(R.id.cat_bottom_navs_animated_container)
    val layoutParams = animatedContainer?.layoutParams as CoordinatorLayout.LayoutParams
    layoutParams.let {
      it.behavior = null
      it.gravity = Gravity.BOTTOM
    }
    ViewCompat.setOnApplyWindowInsetsListener(coordinatorLayout) { v, insets ->
      insets
    }
    return view
  }
}
