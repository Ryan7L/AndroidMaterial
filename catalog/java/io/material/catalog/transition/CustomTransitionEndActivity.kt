package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class CustomTransitionEndActivity : DemoActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    findViewById<ViewGroup>(android.R.id.content).transitionName = SHARED_ELEMENT_END_ROOT
    setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window.sharedElementEnterTransition = MaterialContainerTransform(this, true).apply {
      addTarget(android.R.id.content)
      duration = 300
      isDrawDebugEnabled = true
    }
    super.onCreate(savedInstanceState)
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(R.layout.cat_custom_transition_end_activity, viewGroup, false)
  }
}
