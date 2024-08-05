package io.material.catalog.preferences

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.google.android.material.color.HarmonizedColors
import com.google.android.material.color.HarmonizedColorsOptions
import com.google.common.collect.ImmutableList
import io.material.catalog.R

private const val DYNAMIC_COLOR_OPTION_ID_ON = 1
private const val DYNAMIC_COLOR_OPTION_ID_OFF = 2

private val DEFAULT_OPTION = Option(
  DYNAMIC_COLOR_OPTION_ID_ON,
  R.drawable.ic_dynamic_color_24px,
  R.string.dynamic_color_preference_option_on
)

private val optionList = ImmutableList.of(
  DEFAULT_OPTION,
  Option(
    DYNAMIC_COLOR_OPTION_ID_OFF,
    R.drawable.ic_dynamic_color_24px,
    R.string.dynamic_color_preference_option_off
  )
)

/**
 * 动态颜色配置项
 * @property isApplied Boolean 是否已经应用
 * @property isOptionOn Boolean 是否开启了动态颜色
 */
class DynamicColorPreference : CatalogPreference(R.string.dynamic_color_preference_description) {

  private var isApplied = false

  private var isOptionOn = false

  override fun apply(context: Context, selectedOption: Option) {
    isOptionOn = selectedOption.optionId == DYNAMIC_COLOR_OPTION_ID_ON
    if (isOptionOn && !isApplied) {
      isApplied = true
      applyDynamicColorWithMaterialDefaultHarmonization(context)
    }
  }

  override val options: List<Option> = optionList
  override val defaultOption: Option = DEFAULT_OPTION
  override val isShouldRecreateActivityOnOptionChanged: Boolean = true
  override val isEnabled: Boolean = DynamicColors.isDynamicColorAvailable()

  private fun applyDynamicColorWithMaterialDefaultHarmonization(context: Context) {
    DynamicColors.applyToActivitiesIfAvailable(context.applicationContext as Application,
      DynamicColorsOptions.Builder()
        .setPrecondition { _, _ -> isOptionOn }
        .setOnAppliedCallback {
          if (it is BaseCatalogActivity && it.isColorHarmonizationEnabled) {
            HarmonizedColors.applyToContextIfAvailable(
              it,
              HarmonizedColorsOptions.createMaterialDefaults()
            )
          }
        }
        .build()
    )
  }
}
