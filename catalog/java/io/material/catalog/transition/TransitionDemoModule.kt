package io.material.catalog.transition

import dagger.android.ContributesAndroidInjector
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope

@dagger.Module
abstract class TransitionDemoModule {
  @ActivityScope
  @ContributesAndroidInjector
  abstract fun contributeTransitionContainerTransformStartDemoActivity(): TransitionContainerTransformStartDemoActivity

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeTransitionContainerTransformDemoFragment(): TransitionContainerTransformDemoFragment

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeTransitionContainerTransformViewDemoFragment(): TransitionContainerTransformViewDemoFragment
}
