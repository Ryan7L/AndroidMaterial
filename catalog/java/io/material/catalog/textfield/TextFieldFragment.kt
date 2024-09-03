package io.material.catalog.textfield

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

class TextFieldFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_textfield_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_textfield_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = TextFieldMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_textfield_filled_demo_title) {
        override val fragment: Fragment
          get() = TextFieldFilledDemoFragment()
      },
      object : Demo(R.string.cat_textfield_outlined_demo_title) {
        override val fragment: Fragment
          get() = TextFieldOutlinedDemoFragment()
      },
      object : Demo(R.string.cat_textfield_exposed_dropdown_menu_demo_title) {
        override val fragment: Fragment
          get() = TextFieldExposedDropdownMenuDemoFragment()
      },
      object : Demo(R.string.cat_textfield_filled_icons_demo_title) {
        override val fragment: Fragment
          get() = TextFieldFilledIconsDemoFragment()
      },
      object : Demo(R.string.cat_textfield_outlined_icons_demo_title) {
        override val fragment: Fragment
          get() = TextFieldOutlinedIconsDemoFragment()
      },
      object : Demo(R.string.cat_textfield_prefix_suffix_demo_title) {
        override val fragment: Fragment
          get() = TextFieldPrefixSuffixDemoFragment()
      },
      object : Demo(R.string.cat_textfield_legacy_demo_title) {
        override val fragment: Fragment
          get() = TextFieldLegacyDemoFragment()
      }
    )
}

@dagger.Module
abstract class TextFieldModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TextFieldFragment

  companion object {
    @JvmStatic
    @Provides
    @ActivityScope
    @IntoSet
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_textfield_title, R.drawable.ic_textfield) {
        override val landingFragment: Fragment
          get() = TextFieldFragment()
      }
    }
  }
}
