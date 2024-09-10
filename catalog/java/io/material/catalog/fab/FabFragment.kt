package io.material.catalog.fab

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

class FabFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_fab_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_fab_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = FabMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_extended_fab_demo_title) {
        override val fragment: Fragment
          get() = ExtendedFabDemoFragment()
      },
      object : Demo(R.string.cat_extended_fab_behavior_demo_title) {
        override val fragment: Fragment
          get() = ExtendedFabBehaviorDemoFragment()
      }
    )
}

@dagger.Module
abstract class FabModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): FabFragment

  companion object {
    @JvmStatic
    @ActivityScope
    @IntoSet
    @Provides
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_fab_title, R.drawable.ic_fab) {
        override val landingFragment: Fragment
          get() = FabFragment()
      }
    }
  }
}
