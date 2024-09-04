package io.material.catalog.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.platform.MaterialSharedAxis
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity
const val SHARED_AXIS_KEY = "activity_shared_axis_axis"
class TransitionSharedAxisStartDemoActivity : DemoActivity() {
  private var sharedAxisHelper: SharedAxisHelper? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    window?.setAllowReturnTransitionOverlap(true)
    super.onCreate(savedInstanceState)
    sharedAxisHelper = SharedAxisHelper(findViewById(R.id.controls_layout))
    sharedAxisHelper?.nextButtonOnClickListener = View.OnClickListener { navigateToEndActivity() }
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
   return  layoutInflater.inflate(R.layout.cat_transition_shared_axis_start_activity, viewGroup, false)
  }

  override val isShouldSetUpContainerTransform: Boolean
    get() = false

  private fun navigateToEndActivity() {
    sharedAxisHelper ?: return
    val axis = sharedAxisHelper!!.selectedAxis
    val exitTransition = MaterialSharedAxis(axis, true)
    exitTransition.addTarget(R.id.start_activity)
    window.exitTransition = exitTransition

    val reenterTransition = MaterialSharedAxis(axis, false)
    reenterTransition.addTarget(R.id.start_activity)
    window.reenterTransition = reenterTransition

    val options = ActivityOptions.makeSceneTransitionAnimation(this)
    startActivity(
      Intent(
        this@TransitionSharedAxisStartDemoActivity,
        TransitionSharedAxisEndDemoActivity::class.java
      ).apply {
        putExtra(SHARED_AXIS_KEY, axis)
      }, options.toBundle()
    )
  }
}
