package io.material.catalog.button

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

class ButtonsFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_buttons_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_buttons_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ButtonsMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_buttons_toggle_group) {
        override val fragment: Fragment
          get() = ButtonToggleGroupDemoFragment()
      }
    )
}

@dagger.Module
abstract class ButtonModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ButtonsFragment

  companion object {
    @Provides
    @JvmStatic
    @ActivityScope
    @IntoSet
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_buttons_title, R.drawable.ic_button) {
        override val landingFragment: Fragment
          get() = ButtonsFragment()

      }
    }
  }

}
