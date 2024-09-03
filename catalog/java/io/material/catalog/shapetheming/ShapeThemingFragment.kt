package io.material.catalog.shapetheming

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

class ShapeThemingFragment: DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_shape_theming_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_shape_theming_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ShapeThemingMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_shape_theming_crane_demo_title) {
        override val fragment: Fragment
          get() = ShapeThemingCraneDemoFragment()
      },
      object : Demo(R.string.cat_shape_theming_fortnightly_demo_title) {
        override val fragment: Fragment
          get() = ShapeThemingFortnightlyDemoFragment()
      },
      object : Demo(R.string.cat_shape_theming_shrine_demo_title) {
        override val fragment: Fragment
          get() = ShapeThemingShrineDemoFragment()
      }

    )
}
@dagger.Module
abstract class ShapeThemingModule{
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ShapeThemingFragment
  companion object{
    @JvmStatic
    @IntoSet
    @ActivityScope
    @Provides
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_shape_theming_title, R.drawable.ic_shape) {
        override val landingFragment: Fragment
          get() = ShapeThemingFragment()
      }
    }
  }
}
