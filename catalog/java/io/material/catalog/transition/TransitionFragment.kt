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

/**
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
//      add(
//        object : Demo(){
//          override val activityIntent: Intent?
//            get() = Intent(context,CustomTransitionStartActivity::class.java)
//        }
//      )
    }.toList()

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
