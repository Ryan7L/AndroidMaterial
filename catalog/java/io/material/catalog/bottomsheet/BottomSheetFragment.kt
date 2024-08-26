package io.material.catalog.bottomsheet

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

class BottomSheetFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_bottomsheet_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_bottomsheet_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = BottomSheetMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_bottomsheet_scrollable_content_demo_title) {
        override val fragment: Fragment
          get() = BottomSheetScrollableContentDemoFragment()
      }
    )
}

@dagger.Module
abstract class BottomSheetModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): BottomSheetFragment

  companion object {
    @JvmStatic
    @IntoSet
    @Provides
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_bottomsheet_title, R.drawable.ic_bottomsheet) {
        override val landingFragment: Fragment
          get() = BottomSheetFragment()
      }
    }
  }
}
