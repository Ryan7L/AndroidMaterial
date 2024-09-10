package io.material.catalog.sidesheet

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

class SideSheetFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_sidesheet_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_sidesheet_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = SideSheetMainDemoFragment()
    }
}

@dagger.Module
abstract class SideSheetModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): SideSheetFragment

  companion object {
    @JvmStatic
    @Provides
    @ActivityScope
    @IntoSet
    fun provideFeatureDemo(): FeatureDemo {
      return object :
        FeatureDemo(R.string.cat_sidesheet_title, R.drawable.ic_side_navigation_24px) {
        override val landingFragment: Fragment
          get() = SideSheetFragment()
      }
    }
  }
}
