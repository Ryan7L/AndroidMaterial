package io.material.catalog.chip

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

class ChipFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_chip_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_chip_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ChipMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_chip_group_demo_title) {
        override val fragment: Fragment
          get() = ChipGroupDemoFragment()
      },
      object : Demo(R.string.cat_chip_recyclerview_demo_title) {
        override val fragment: Fragment
          get() = ChipRecyclerviewDemoFragment()
      }
    )
}

@dagger.Module
abstract class ChipModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ChipFragment

  companion object {
    @JvmStatic
    @Provides
    @ActivityScope
    @IntoSet
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_chip_title, R.drawable.ic_chips) {
        override val landingFragment: Fragment
          get() = ChipFragment()
      }
    }
  }
}
