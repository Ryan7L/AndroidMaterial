package io.material.catalog.slider

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

class SliderFragment: DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_slider_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_slider_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = SliderMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_slider_demo_continuous_title) {
        override val fragment: Fragment
          get() = SliderContinuousDemoFragment()
      },
      object : Demo(R.string.cat_slider_demo_discrete_title) {
        override val fragment: Fragment
          get() = SliderDiscreteDemoFragment()
      },
      object : Demo(R.string.cat_slider_demo_scroll_container_title) {
        override val fragment: Fragment
          get() = SliderScrollContainerDemoFragment()
      },
      object : Demo(R.string.cat_slider_demo_label_behavior_title) {
        override val fragment: Fragment
          get() = SliderLabelBehaviorDemoFragment()
      }
    )
}
@dagger.Module
abstract class SliderModule{
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): SliderFragment
  companion object{
    @ActivityScope
    @IntoSet
    @Provides
    @JvmStatic
    fun featureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_slider_title, R.drawable.ic_sliders_24px) {
        override val landingFragment: Fragment
          get() = SliderFragment()
      }
    }
  }
}
