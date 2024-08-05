package io.material.catalog.preferences

import android.content.Context
import android.util.SparseIntArray
import androidx.appcompat.app.AppCompatDelegate
import com.google.common.collect.ImmutableList
import io.material.catalog.R

private const val THEME_OPTION_ID_LIGHT = 1
private const val THEME_OPTION_ID_DARK = 2
private const val THEME_OPTION_ID_SYSTEM_DEFAULT = 3
private val DEFAULT_OPTION = Option(
  THEME_OPTION_ID_SYSTEM_DEFAULT,
  R.drawable.ic_theme_default_24px,
  R.string.theme_preference_option_system_default
)
private val OPTION_ID_TO_NIGHT_MODE = SparseIntArray().apply {
  append(THEME_OPTION_ID_LIGHT, AppCompatDelegate.MODE_NIGHT_NO)
  append(THEME_OPTION_ID_DARK, AppCompatDelegate.MODE_NIGHT_YES)
  append(THEME_OPTION_ID_SYSTEM_DEFAULT, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}
private val optionList = ImmutableList.of(
  Option(
    THEME_OPTION_ID_LIGHT,
    R.drawable.ic_theme_light_24px,
    R.string.theme_preference_option_light
  ),
  Option(
    THEME_OPTION_ID_DARK,
    R.drawable.ic_theme_dark_24px,
    R.string.theme_preference_option_dark
  ),
  DEFAULT_OPTION
)

/**
 * 明暗模式配置项.
 */
class ThemePreference : CatalogPreference(R.string.theme_preference_description) {

  override fun apply(context: Context, selectedOption: Option) {
    AppCompatDelegate.setDefaultNightMode(OPTION_ID_TO_NIGHT_MODE[selectedOption.optionId])
  }

  override val options: List<Option> = optionList


  override val defaultOption: Option = DEFAULT_OPTION

}
