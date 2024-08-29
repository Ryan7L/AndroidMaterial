package io.material.catalog.dialog

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

class DialogDemoLandingFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_dialog_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_dialog_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = DialogMainDemoFragment()
    }
}

@dagger.Module
abstract class DialogModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): DialogDemoLandingFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_dialog_title, R.drawable.ic_dialog) {
        override val landingFragment: Fragment
          get() = DialogDemoLandingFragment()
      }
    }
  }
}
