package io.material.catalog.progressindicator

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

class ProgressIndicatorFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_progress_indicator_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_progress_indicator_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ProgressIndicatorMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_progress_indicator_visibility_demo_title) {
        override val fragment: Fragment
          get() = ProgressIndicatorVisibilityDemoFragment()
      },
      object : Demo(R.string.cat_progress_indicator_demo_standalone_title) {
        override val fragment: Fragment
          get() = ProgressIndicatorStandaloneDemoFragment()
      },
      object : Demo(R.string.cat_progress_indicator_wave_demo_title) {
        override val fragment: Fragment
          get() = ProgressIndicatorWaveDemoFragment()
      },
      object : Demo(R.string.cat_progress_indicator_multi_color_demo_title) {
        override val fragment: Fragment
          get() = ProgressIndicatorMultiColorDemoFragment()
      }
    )
}

@dagger.Module
abstract class ProgressIndicatorModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ProgressIndicatorFragment

  companion object {
    @JvmStatic
    @Provides
    @ActivityScope
    @IntoSet
    fun provideFeatureDemo(): FeatureDemo {
      return object :
        FeatureDemo(R.string.cat_progress_indicator_title, R.drawable.ic_progress_activity_24px) {
        override val landingFragment: Fragment
          get() = ProgressIndicatorFragment()
      }
    }
  }
}
