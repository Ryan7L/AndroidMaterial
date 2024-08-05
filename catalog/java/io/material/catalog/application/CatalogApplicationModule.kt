package io.material.catalog.application

import androidx.annotation.Keep
import dagger.Module
import dagger.Provides
import io.material.catalog.preferences.BaseCatalogPreferences
import io.material.catalog.preferences.CatalogPreferences

/**
 * [android.app.Application]级别的绑定模块
 *
 * 提供[BaseCatalogPreferences]的实例
 */
@Module
@Keep
abstract class CatalogApplicationModule {

  companion object {
    @Provides
    @JvmStatic
    fun provideBaseCatalogPreference(): BaseCatalogPreferences {
      return CatalogPreferences()
    }
  }
}
