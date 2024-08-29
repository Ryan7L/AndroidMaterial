package io.material.catalog.divider

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

class DividerFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_divider_demo_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_divider_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = DividerMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_divider_item_decoration_demo_title) {
        override val fragment: Fragment
          get() = DividerItemDecorationDemoFragment()
      }
    )
}

@dagger.Module
abstract class DividerModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeDividerFragmentInjector(): DividerFragment

  companion object {
    @IntoSet
    @ActivityScope
    @Provides
    @JvmStatic
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_divider_demo_title, R.drawable.ic_placeholder) {
        override val landingFragment: Fragment
          get() = DividerFragment()
      }
    }
  }
}
