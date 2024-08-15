package io.material.catalog.animation

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

class AnimationFragment : DemoLandingFragment() {
  override val titleResId: Int
    get() = R.string.cat_animation_title
  override val descriptionResId: Int
    get() = R.string.cat_animation_description
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = AnimationMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_animation_additional_frame) {
        override val fragment: Fragment
          get() = FrameDemoFragment()
      },
      object : Demo(R.string.cat_animation_additional_tween) {
        override val fragment: Fragment
          get() = TweenDemoFragment()
      }
    )
}

@dagger.Module
abstract class AnimationFragmentModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): AnimationFragment

  companion object {
    @IntoSet
    @Provides
    @ActivityScope
    @JvmStatic
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_animation_title, R.drawable.ic_transition) {
        override val landingFragment: Fragment
          get() = AnimationFragment()
      }
    }
  }
}
