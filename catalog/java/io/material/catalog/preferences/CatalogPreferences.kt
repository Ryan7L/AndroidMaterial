package io.material.catalog.preferences

import io.material.catalog.application.scope.ApplicationScope

private val PREFERENCES = listOf(
  DynamicColorPreference(),
  FontPreference()
)

@ApplicationScope
class CatalogPreferences : BaseCatalogPreferences() {
  /**
   * 获取配置项的集合
   */
  override val preferences: List<CatalogPreference> = PREFERENCES + COMMON_PREFERENCES
}
