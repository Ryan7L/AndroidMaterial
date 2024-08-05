package io.material.catalog.application

import android.app.Application
import dagger.BindsInstance
import dagger.android.AndroidInjectionModule
import io.material.catalog.application.scope.ApplicationScope
import io.material.catalog.main.MainActivity
import io.material.catalog.musicplayer.MusicPlayerDemoModule
import io.material.catalog.transition.TransitionDemoModule
import javax.inject.Singleton

@Singleton
@ApplicationScope
@dagger.Component(
  modules = [
    AndroidInjectionModule::class,
    CatalogApplicationModule::class,
    MainActivity.Module::class,
    CatalogDemoModule::class,
    TransitionDemoModule::class,
    MusicPlayerDemoModule::class,
  ]
)
/**
 * app 的根组件
 */
interface CatalogApplicationComponent {

  fun inject(app: CatalogApplication)

  @dagger.Component.Builder

  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder
    fun build(): CatalogApplicationComponent
  }
}
