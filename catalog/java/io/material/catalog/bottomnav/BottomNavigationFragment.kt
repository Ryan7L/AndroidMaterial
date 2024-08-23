package io.material.catalog.bottomnav

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

class BottomNavigationFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_bottom_nav_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_bottom_nav_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = BottomNavigationMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_bottom_nav_label_visibility_demo_title) {
        override val fragment: Fragment
          get() = BottomNavigationLabelVisibilityDemoFragment()
      },
      object : Demo(R.string.cat_bottom_nav_animated_demo_title) {
        override val fragment: Fragment
          get() = BottomNavigationAnimatedDemoFragment()
      }
    )
}

@dagger.Module
abstract class BottomNavigationModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): BottomNavigationFragment

  companion object {
    @IntoSet
    @Provides
    @JvmStatic
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_bottom_nav_title, R.drawable.ic_bottomnavigation) {
        override val landingFragment: Fragment
          get() = BottomNavigationFragment()
      }
    }
  }
}
