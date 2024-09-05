package io.material.catalog.adaptive

import dagger.android.ContributesAndroidInjector
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope

@dagger.Module
abstract class AdaptiveMusicPlayerDemoModule {

  @ActivityScope
  @ContributesAndroidInjector
  abstract fun contributeAdaptiveMusicPlayerDemoActivity(): AdaptiveMusicPlayerDemoActivity

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeAdaptiveMusicPlayerAlbumDemoFragment(): AdaptiveMusicPlayerAlbumDemoFragment

}
