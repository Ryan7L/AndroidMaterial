package io.material.catalog.musicplayer

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope

@Module
abstract class MusicPlayerDemoModule {
  @ActivityScope
  @ContributesAndroidInjector
  abstract fun contributeMusicPlayerDemoActivity(): MusicPlayerDemoActivity

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeMusicPlayerAlbumDemoFragment(): MusicPlayerAlbumDemoFragment
}
