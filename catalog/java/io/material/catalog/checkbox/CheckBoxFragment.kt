package io.material.catalog.checkbox

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

class CheckBoxFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_checkbox_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_checkbox_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment?
        get() = CheckBoxMainDemoFragment()
    }
}

@dagger.Module
abstract class CheckBoxModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): CheckBoxFragment

  companion object {
    @JvmStatic
    @IntoSet
    @Provides
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_checkbox_title, R.drawable.ic_checkbox) {
        override val landingFragment: Fragment
          get() = CheckBoxFragment()
      }
    }
  }
}
