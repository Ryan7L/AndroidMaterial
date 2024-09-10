package io.material.catalog.transition.non_material

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class NonMaterialTransitionDemoStartActivity : DemoActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    //告诉系统启用transition
//    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
//    //设置具体的场景的过渡效果
//    window?.enterTransition = Slide()
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
      R.layout.cat_transition_container_transform_start_fragment,
      viewGroup,
      false
    )
    view.findViewById<View>(R.id.start_fab).setOnClickListener {
      startEndActivity()
    }
    view.findViewById<View>(R.id.single_line_list_item).setOnClickListener {
      startEndActivity()
    }
    view.findViewById<View>(R.id.vertical_card_item).setOnClickListener {
      startEndActivity()
    }
    view.findViewById<View>(R.id.horizontal_card_item).setOnClickListener {
      startEndActivity()
    }
    view.findViewById<View>(R.id.grid_card_item).setOnClickListener {
      startEndActivity()
    }
    view.findViewById<View>(R.id.grid_tall_card_item).setOnClickListener {
      startEndActivity()
    }

    return view
  }


  private fun startEndActivity() {
    val option = ActivityOptions.makeSceneTransitionAnimation(this)
    startActivity(Intent(this, NonMaterialTransitionDemoEndActivity::class.java), option.toBundle())
  }

}
/**
 * ActivityOptions:
 *    用于构建可与Context. startActivity(Intent, Bundle)和相关方法一起使用的选项 Bundle 的辅助类.主要用于定义和管理在启动新活动时的动画和过渡效果。
 *    允许在启动活动时指定一些额外的选项，如共享元素过渡动画、启动位置的动画效果，以及是否在多窗口模式下启动等。
 *  主要作用:
 *      过渡动画: ActivityOptions 可以指定在两个活动之间切换时的动画效果。例如，你可以使用共享元素过渡动画，使得两个活动之间的某个视图看起来像是从一个活动“移动”到另一个活动。
 *
 *      启动动画: 通过 ActivityOptions，你可以控制新活动从屏幕的哪个位置开始动画。例如，可以指定新活动从屏幕的某个特定位置缩放或滑入。
 *
 *      多窗口支持: 在 Android 7.0 (API level 24) 及更高版本中，ActivityOptions 支持多窗口模式。你可以通过该类指定活动启动在哪个窗口中。
 *
 *      共享元素: 在使用共享元素过渡时，ActivityOptions 可以传递共享元素的信息，确保过渡动画在两个活动之间平滑运行。
 *   常用方法：以下方法都用于Activity之间的过渡动画
 *      makeSceneTransitionAnimation():创建过渡元素动画
 *      makeCustomAnimation(): 创建自定义的进入动画(用在目标Activity)和退出动画(用于当前需要退出的Activity)
 *      makeScaleUpAnimation():创建从指定大小慢慢放大的过渡动画
 *      makeThumbnailScaleUpAnimation():从略缩图慢慢放大的过渡动画
 *      makeClipRevealAnimation():从一个点，以圆形渐变到全屏的过渡动画
 *
 *    流程：
 *    1.告诉系统启用了过渡动画
 *    2.定义过渡动画
 *    3.设置过渡动画
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */
