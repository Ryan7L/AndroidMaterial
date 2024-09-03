package io.material.catalog.tabs

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

class TabsFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_tabs_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_tabs_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = TabsMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_tabs_controllable_demo_title) {
        override val fragment: Fragment
          get() = TabsControllableDemoFragment()
      },
      object : Demo(R.string.cat_tabs_scrollable_demo_title) {
        override val fragment: Fragment
          get() = TabsScrollableDemoFragment()
      },
      object : Demo(R.string.cat_tabs_auto_demo_title) {
        override val fragment: Fragment
          get() = TabsAutoDemoFragment()
      },
      object : Demo(R.string.cat_tabs_viewpager_demo_title) {
        override val fragment: Fragment
          get() = TabsViewPagerDemoFragment()
      }
    )
}

@dagger.Module
abstract class TabsModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TabsFragment

  companion object {
    @JvmStatic
    @IntoSet
    @ActivityScope
    @Provides
    fun featureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_tabs_title, R.drawable.ic_tabs) {
        override val landingFragment: Fragment
          get() = TabsFragment()
      }
    }
  }
}
