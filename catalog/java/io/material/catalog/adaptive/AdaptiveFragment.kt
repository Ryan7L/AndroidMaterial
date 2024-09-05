package io.material.catalog.adaptive

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

class AdaptiveFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_adaptive_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_adaptive_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val activityIntent: Intent
        get() = Intent(context, AdaptiveListViewDemoActivity::class.java)
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_feed_title) {
        override val activityIntent: Intent
          get() = Intent(context, AdaptiveFeedDemoActivity::class.java)
      },
      object : Demo(R.string.cat_hero_title) {
        override val activityIntent: Intent
          get() = Intent(context, AdaptiveHeroDemoActivity::class.java)
      },
      object : Demo(R.string.cat_supporting_panel_title) {
        override val activityIntent: Intent
          get() = Intent(context, AdaptiveSupportingPanelDemoActivity::class.java)
      }
    )
}

@dagger.Module
abstract class AdaptiveModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeAdaptiveFragment(): AdaptiveFragment

  companion object {
    @JvmStatic
    @ActivityScope
    @IntoSet
    @Provides
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_adaptive_title, R.drawable.ic_side_drawer) {
        override val landingFragment: Fragment
          get() = AdaptiveFragment()
      }
    }
  }
}
