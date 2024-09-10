package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.transition.platform.MaterialSharedAxis
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class TransitionSharedAxisEndDemoActivity : DemoActivity() {

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(
      R.layout.cat_transition_shared_axis_end_activity,
      viewGroup,
      false
    )
  }

  override val isShouldSetUpContainerTransform: Boolean
    get() = false

  override fun onCreate(savedInstanceState: Bundle?) {
    //删除过渡名称以避免超类 DemoActivity 使用自己的过渡设置配置窗口。
    window.allowEnterTransitionOverlap = true
    val axis = intent.getIntExtra(SHARED_AXIS_KEY, MaterialSharedAxis.X)
    val enterTransition = MaterialSharedAxis(axis, true).apply {
      addTarget(R.id.end_activity)
    }
    window.enterTransition = enterTransition
    val returnTransition = MaterialSharedAxis(axis, false).apply {
      addTarget(R.id.end_activity)
    }
    window.returnTransition = returnTransition
    super.onCreate(savedInstanceState)
    SharedAxisHelper(findViewById(R.id.controls_layout)).apply {
      setAxisButtonGroupEnabled(false)
      updateButtonsEnabled(false)
      selectedAxis = axis
      backButtonOnClickListener = View.OnClickListener { onBackPressed() }
    }
  }
}
