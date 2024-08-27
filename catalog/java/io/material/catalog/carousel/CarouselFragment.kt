package io.material.catalog.carousel

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

class CarouselFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_carousel_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_carousel_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = CarouselMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_carousel_multi_browse_demo_title) {
        override val fragment: Fragment
          get() = MultiBrowseCarouselDemoFragment()
      },
      object : Demo(R.string.cat_carousel_hero_demo_title) {
        override val fragment: Fragment
          get() = HeroCarouselDemoFragment()
      },
      object : Demo(R.string.cat_carousel_fullscreen_demo_title) {
        override val fragment: Fragment
          get() = FullScreenStrategyDemoFragment()
      },
      object : Demo(R.string.cat_carousel_uncontained_demo_title) {
        override val fragment: Fragment
          get() = UncontainedCarouselDemoFragment()
      }
    )
}

@dagger.Module
abstract class CarouselModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): CarouselFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_carousel_title, R.drawable.ic_lists) {
        override val landingFragment: Fragment
          get() = CarouselFragment()
      }
    }
  }
}
