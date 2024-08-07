package io.material.catalog.topappbar

import android.content.Intent
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

class TopAppBarFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int = R.string.cat_topappbar_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int = R.string.cat_topappbar_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo = object : Demo() {
    override val fragment: Fragment
      get() = TopAppBarMainDemoFragment()
  }
  override val additionalDemos: List<Demo> = listOf(
    object : Demo(R.string.cat_topappbar_compress_effect_demo_title) {
      override val fragment: Fragment
        get() = TopAppBarCompressEffectFragment()
    },
    object : Demo(R.string.cat_topappbar_scrolling_title) {
      override val fragment: Fragment
        get() = TopAppBarScrollingDemoFragment()
    },
    object : Demo(R.string.cat_topappbar_scrolling_transparent_title) {
      override val activityIntent: Intent
        get() = Intent(context, TopAppBarScrollingTransparentStatusDemoActivity::class.java)
    },
    object : Demo(R.string.cat_topappbar_preferences_demo_title) {
      override val fragment: Fragment?
        get() = TopAppBarPreferencesFragment()
    },
    /*折叠工具栏demo*/
    object : Demo(R.string.cat_topappbar_collapsing_medium_title) {
      override val fragment: Fragment
        get() = TopAppBarCollapsingMediumDemoFragment()
    },
    object : Demo(R.string.cat_topappbar_collapsing_large_title) {
      override val fragment: Fragment
        get() = TopAppBarCollapsingLargeDemoFragment()
    },
    object : Demo(R.string.cat_topappbar_collapsing_multiline_title) {
      override val fragment: Fragment
        get() = TopAppBarCollapsingMultilineDemoFragment()
    },
    /*ToolBar Demo*/
    object : Demo(R.string.cat_topappbar_toolbar_title) {
      override val fragment: Fragment
        get() = TopAppBarToolbarDemoFragment()
    },
    /*ActionBar Demo*/
    object : Demo(R.string.cat_topappbar_action_bar_title) {
      override val activityIntent: Intent
        get() = Intent(context, TopAppBarActionBarDemoActivity::class.java)
    },
    object : Demo(R.string.cat_topappbar_collapsing_parallax_title) {
      override val fragment: Fragment
        get() = TopAppBarCollapsingParallaxFragment()
    }
  )
}

@dagger.Module
abstract class TopAppBarModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TopAppBarFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo =
      object : FeatureDemo(R.string.cat_topappbar_title, R.drawable.ic_topappbar) {
        override val landingFragment: Fragment
          get() = TopAppBarFragment()
      }
  }
}
