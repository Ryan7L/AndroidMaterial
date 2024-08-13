package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

/**
 * 用于Activity的容器过渡(共享元素过渡)
 */

class TransitionContainerTransformEndDemoActivity : DemoActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    //启用过渡
    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    findViewById<ViewGroup>(android.R.id.content).transitionName = SHARED_ELEMENT_END_ROOT
    setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
    window?.sharedElementEnterTransition = buildContainerTransform(true)
    window?.sharedElementReturnTransition = buildContainerTransform(false)
    super.onCreate(savedInstanceState)
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(R.layout.cat_transition_container_transform_end_activity,viewGroup,false)
  }

  override val demoTitleResId: Int
    get() = R.string.cat_transition_container_transform_activity_title

  private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
      return MaterialContainerTransform(this,entering).apply {
        //使用全部 3 个容器层颜色，因为可以使用任何淡入淡出模式配置此变换，并且某些开始视图没有背景，并且结束视图没有背景。
        setAllContainerColors(MaterialColors.getColor(findViewById(android.R.id.content),R.attr.colorSurface))
        addTarget(android.R.id.content)
        TransitionContainerTransformStartDemoActivity.configurationHelper?.configure(this,entering)
      }
  }
}
