package io.material.catalog.preferences

import android.content.Context

/**
 * 配置集合的基类
 * @property preferences List<CatalogPreference>
 */
abstract class BaseCatalogPreferences {

  companion object {
    /**
     * 基本配置项集合
     */
    @JvmField
    val COMMON_PREFERENCES = listOf(
      ThemePreference(),
      ShapeCornerSizePreference(),
      ShapeCornerFamilyPreference(),
      EdgeToEdgePreference()
    )
  }

  /**
   * 获取配置项的集合
   */
  abstract val preferences: List<CatalogPreference>

  /**
   * 应用配置
   * @param context Context
   */
  fun applyPreferences(context: Context) {
    preferences.forEach {
      it.apply(context)
    }
  }
}
