package io.material.catalog.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

const val CUSTOM_TRANSITION = "custom"

class CustomTransitionStartActivity : DemoActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    super.onCreate(savedInstanceState)
    findViewById<ImageView>(R.id.img).setOnClickListener {
      val options = ActivityOptions.makeSceneTransitionAnimation(
        this,
        it,
        CUSTOM_TRANSITION
      )
      startActivity(Intent(this, CustomTransitionEndActivity::class.java), options.toBundle())
    }
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(
      R.layout.cat_custom_transition_start_activity, viewGroup, false
    )
  }
}
