package io.material.catalog.transition.non_material

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Explode
import android.transition.Fade
import android.transition.Slide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class NonMaterialTransitionDemoEndActivity: DemoActivity()  {
  override fun onCreate(savedInstanceState: Bundle?) {
    //告诉系统启用transition
    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    //设置具体的场景的过渡效果
    window?.enterTransition = Slide()
//    window?.exitTransition = Explode()
//    window?.reenterTransition = Fade()
//    window?.returnTransition = ChangeBounds()
    super.onCreate(savedInstanceState)

  }

  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(
      R.layout.cat_transition_container_transform_end_activity,
      viewGroup,
      false
    )
    return view
  }
}
