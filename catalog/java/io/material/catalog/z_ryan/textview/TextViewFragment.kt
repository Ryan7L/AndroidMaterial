package io.material.catalog.z_ryan.textview

import androidx.fragment.app.Fragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.feature.Demo
import io.material.catalog.feature.DemoLandingFragment
import io.material.catalog.feature.FeatureDemo

class TextViewFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_text_view_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_text_view_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo = object : Demo(R.string.cat_text_view_main_title) {
    override val fragment: Fragment = WidgetTextViewFragment()
  }
  override val additionalDemos: List<Demo> = listOf(
    object : Demo(R.string.cat_text_view_additional_title) {
      override val fragment: Fragment = MaterialTextViewFragment()
    }
  )
  override val linksArrayResId: Int = R.array.resource_link
  override val linkUrlArrayResId: Int = R.array.link_url
}

@Module
abstract class TextViewModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TextViewFragment

  companion object {
    @IntoSet
    @Provides
    @ActivityScope
    @JvmStatic
    fun provideFeatureDemo(): FeatureDemo =
      object : FeatureDemo(R.string.cat_text_view_title, R.drawable.ic_text_field_24px) {
        override val landingFragment: Fragment = TextViewFragment()
      }
  }
}
