package io.material.catalog.application.legacymultidex

import android.content.Context
import androidx.multidex.MultiDex
import io.material.catalog.application.CatalogApplication

/**
 * 用于开发的CatalogApplication版本是在旧款手机上构建的，它使用 Multidex 支持库来允许多个 dex 文件
 */
class LegacyMultidexCatalogApplication : CatalogApplication(){

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }
}
