package io.material.catalog.transition

import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.feature.Demo
import io.material.catalog.feature.DemoLandingFragment
import io.material.catalog.feature.FeatureDemo
import io.material.catalog.musicplayer.MusicPlayerDemoActivity


open class TransitionFragment : DemoLandingFragment() {
  private val PLATFORM_TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
  override val titleResId: Int = R.string.cat_transition_title
  override val descriptionResId: Int = R.string.cat_transition_description
  override val mainDemo: Demo
    get() = object : Demo() {
      override val activityIntent: Intent
        get() = Intent(context, MusicPlayerDemoActivity::class.java)

    }
  override val additionalDemos: List<Demo>
    get() = mutableListOf<Demo>().apply {
      if (PLATFORM_TRANSITIONS_AVAILABLE) {
        add(
          object : Demo(R.string.cat_transition_container_transform_activity_title) {
            override val activityIntent: Intent
              get() = Intent(context, TransitionContainerTransformStartDemoActivity::class.java)
          }
        )
      }
      add(
        object : Demo(R.string.cat_transition_container_transform_fragment_title) {
          override val fragment: Fragment
            get() = TransitionContainerTransformDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_container_transform_view_title) {
          override val fragment: Fragment
            get() = TransitionContainerTransformViewDemoFragment()
        }
      )
      if (PLATFORM_TRANSITIONS_AVAILABLE) {
        add(
          object : Demo(R.string.cat_transition_shared_axis_activity_title) {
            override val activityIntent: Intent
              get() = Intent(context, TransitionSharedAxisStartDemoActivity::class.java)
          }
        )
      }
      add(
        object : Demo(R.string.cat_transition_shared_axis_fragment_title) {
          override val fragment: Fragment
            get() = TransitionSharedAxisDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_shared_axis_view_title) {
          override val fragment: Fragment
            get() = TransitionSharedAxisViewDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_fade_through_title) {
          override val fragment: Fragment
            get() = TransitionFadeThroughDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_fade_title) {
          override val fragment: Fragment
            get() = TransitionFadeDemoFragment()
        }
      )
    }.toList()

  //添加非Material库demo
  override val nonMaterialDemos: List<Demo>
    get() = mutableListOf<Demo>().apply {

      add(
        object : Demo(R.string.cat_transition_non_material_base_transition_title) {
          override val fragment: Fragment
            get() = BaseTransitionDemoFragment()
        }
      )
    }
}

@dagger.Module
abstract class TransitionModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TransitionFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_transition_title, R.drawable.ic_transition) {
        override val landingFragment: Fragment
          get() = TransitionFragment()
      }

    }
  }
}

/**
 * Transition:过渡
 *    分类：
 *        1.内容变换动画(Content Transition)：包含进入动画和退出动画
 *        2.共享元素动画
 * Transition类：transition主要负责捕捉每个View在开始场景和结束场景时的状态，根据两个场景（开始和结束）之间的区别创建一个Animator，所以Transition内部利用了属性动画
 *              Transition内部使用了属性动画实现，所以它可以认为是属性动画的封装。Transition两个核心概念为：场景（scenes）和变换（transitions），场景是UI当前状态，变换则定义了在不同场景之间动画变化的过程。所以Transition主要负责两个方面的事，一是保存开始和结束场景的两种状态，二是在两种状态之间创建动画。
 *              由于场景记录了内部所有View的开始和结束状态，所以Transition动画更具连贯性。TransitionManager负责执行动画的任务。
 * Scene：场景过渡动画就是实现View从一种状态变化到另外一种状态，Scene就代表一个场景，它内部保存一个完整地视图结构，从根ViewGroup到所有子view，还有它们的所有状态信息。所以Scene最终就一个设置了不同属性特征的ViewGroup
 *
 *
 *
 *
 *
 *
 *
 */


/**
 * https://lianjiehao.github.io/2019/07/29/%E8%BF%87%E6%B8%A1%E5%8A%A8%E7%94%BB/
 * Transition:过渡：
 * 分类：
 *  - 内置过渡：严格意义上来说应该算是过渡的效果，而不是单独的分类
 *    - Fade:淡入淡出 视图在出现或消失时逐渐改变透明度。
 *    - Enter/Exit:进入/退出 视图从屏幕边缘滑入或滑出
 *    - Slide:滑动 视图水平或垂直滑动
 *    - Explode:爆炸 视图从中心向外扩散或从外向内收缩
 *  - 场景过渡：场景过渡用于在两个场景之间切换
 *    - ActivityOptionsCompat.makeSceneTransitionAnimation(): 用于在 Activity 之间创建共享元素过渡动画
 *    - FragmentTransaction.setTransition: 用于Fragment之间的过渡
 *  - 共享元素过渡:适用于在两个 Activity 或 Fragment 之间共享一个或多个视图，并在过渡过程中平滑地将这些视图从一个位置动画到另一个位置， 例如在图片库应用中， 点击缩略图， 图片放大到全屏显示。
 */
