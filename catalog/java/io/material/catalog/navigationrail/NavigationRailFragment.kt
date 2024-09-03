package io.material.catalog.navigationrail

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

class NavigationRailFragment: DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_navigation_rail_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_navigation_rail_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = NavigationRailDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_navigation_rail_additional_controls_demo_title){
        override val fragment: Fragment
          get() = NavigationRailDemoControlsFragment()
      },
      object : Demo(R.string.cat_navigation_rail_animated_demo_title){
        override val fragment: Fragment
          get() = NavigationRailAnimatedDemoFragment()
      }
    )
}
@dagger.Module
abstract class NavigationRailModule{
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): NavigationRailFragment
  companion object{
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_navigation_rail_title, R.drawable.ic_placeholder) {
        override val landingFragment: Fragment
          get() = NavigationRailFragment()
      }
    }
  }
}
