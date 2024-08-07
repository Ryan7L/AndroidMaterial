package io.material.catalog.z_ryan.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.FeatureDemoUtils

class CoordinatorLayoutFragment : DemoFragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.coordinator_layout_fragment, container, false)
    arguments?.let {
      val transitionName = it.getString(FeatureDemoUtils.ARG_TRANSITION_NAME)
      ViewCompat.setTransitionName(view, transitionName)
    }
    demoFragmentContainer = view.findViewById(R.id.container)
    demoFragmentContainer.addView(onCreateDemoView(inflater, container, savedInstanceState))
    return view
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    TODO("Not yet implemented")
  }
}
