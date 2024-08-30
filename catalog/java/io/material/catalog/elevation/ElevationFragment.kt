package io.material.catalog.elevation

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

class ElevationFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_elevation_fragment_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_elevation_fragment_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ElevationMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_elevation_overlay_title) {
        override val activityIntent: Intent?
          get() = Intent(context, ElevationOverlayDemoActivity::class.java)
      },
      object : Demo(R.string.cat_elevation_animation_title) {
        override val fragment: Fragment
          get() = ElevationAnimationDemoFragment()
      }
    )
}

@dagger.Module
abstract class ElevationModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ElevationFragment

  companion object {
    @JvmStatic
    @IntoSet
    @Provides
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_elevation_fragment_title, R.drawable.ic_elevation) {
        override val landingFragment: Fragment
          get() = ElevationFragment()
      }
    }
  }
}
