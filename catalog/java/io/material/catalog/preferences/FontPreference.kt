package io.material.catalog.preferences

import android.content.Context
import android.util.SparseIntArray
import io.material.catalog.R

private const val FONT_PREFERENCE_OPTION_ID_DEFAULT = 1
private const val FONT_PREFERENCE_OPTION_ID_CUSTOM_1 = 2
private const val FONT_PREFERENCE_OPTION_ID_CUSTOM_2 = 3
private const val FONT_PREFERENCE_OPTION_ID_CUSTOM_3 = 4
private val DEFAULT_OPTION = Option(
  FONT_PREFERENCE_OPTION_ID_DEFAULT,
  0,
  R.string.font_preference_option_default
)
private val optionList = listOf(
  DEFAULT_OPTION,
  Option(
    FONT_PREFERENCE_OPTION_ID_CUSTOM_1,
    0,
    R.string.font_preference_option_custom_1
  ),
  Option(
    FONT_PREFERENCE_OPTION_ID_CUSTOM_2,
    0,
    R.string.font_preference_option_custom_2
  ),
  Option(
    FONT_PREFERENCE_OPTION_ID_CUSTOM_3,
    0,
    R.string.font_preference_option_custom_3
  )
)

private val OPTION_ID_TO_THEME_OVERLAY = SparseIntArray().apply {
  append(FONT_PREFERENCE_OPTION_ID_DEFAULT, R.style.ThemeOverlay_Font_Default)
  append(FONT_PREFERENCE_OPTION_ID_CUSTOM_1, R.style.ThemeOverlay_Font_Custom_1)
  append(FONT_PREFERENCE_OPTION_ID_CUSTOM_2, R.style.ThemeOverlay_Font_Custom_2)
  append(FONT_PREFERENCE_OPTION_ID_CUSTOM_3, R.style.ThemeOverlay_Font_Custom_3)
}

/**
 * 字体配置项
 */
class FontPreference : CatalogPreference(R.string.font_preference_description) {

  override val defaultOption: Option = DEFAULT_OPTION
  override val options: List<Option> = optionList

  override fun apply(context: Context, selectedOption: Option) {
    ThemeOverlayUtils.setThemeOverlay(
      R.id.theme_feature_font,
      OPTION_ID_TO_THEME_OVERLAY.get(selectedOption.optionId)
    )
  }

  override val isShouldRecreateActivityOnOptionChanged: Boolean = true
}
