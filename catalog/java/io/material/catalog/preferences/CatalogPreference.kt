package io.material.catalog.preferences

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.internal.ContextUtils

/**
 * SharedPreferences Name
 */
private const val SHARED_PREFERENCES_NAME = "catalog.preferences"

/**
 * 非法的OptionID
 */
private const val INVALID_OPTION_ID = 0

/**
 * Preference 的接口,可实现此接口实现自定义设置项
 * @property preferenceDescription Int 配置类别的描述的资源id
 * @property sharedPreferencesKey [@EnhancedForWarnings(String)] 用于SharedPreferences保存数据的 Key
 * @property options List<Option> 可用的配置集合
 * @property defaultOption Option 默认配置
 * @property isEnabled Boolean 该选项类别是否可以更改
 * @property isShouldRecreateActivityOnOptionChanged Boolean 是否在配置改变时重新创建Activity
 */
abstract class CatalogPreference(@StringRes var preferenceDescription: Int) {

  private val sharedPreferencesKey = this.javaClass.simpleName

  abstract val options: List<Option>

  abstract val defaultOption: Option

  open val isEnabled = true

  open val isShouldRecreateActivityOnOptionChanged = false

  /**
   * 当配置改变时重新创建Activity
   * @param context Context
   */
  private fun recreateActivityIfPossible(context: Context) {
    ContextUtils.getActivity(context)?.recreate()
  }

  /**
   * 获取SharedPreferences
   * @param context Context
   * @return (SharedPreferences..SharedPreferences?)
   */
  private fun getSharedPreferences(context: Context) = context.getSharedPreferences(
    SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
  )

  /**
   * 获取已选择的配置的OptionID
   * @param context Context
   * @return Int
   */
  private fun getSelectedOptionId(context: Context) = getSharedPreferences(context).getInt(
    sharedPreferencesKey,
    INVALID_OPTION_ID
  )

  /**
   * 获取已选择的配置
   * @param context Context
   * @return Option
   */
  fun getSelectedOption(context: Context): Option {
    val selectedOptionId = getSelectedOptionId(context)
    return options.find { it.optionId == selectedOptionId } ?: run {
      setSelectedOption(context, defaultOption.optionId)
      defaultOption
    }
  }

  /**
   * 设置选项并应用
   * @param context Context
   * @param optionId Int
   */
  fun setSelectedOption(context: Context, optionId: Int) {
    if (optionId == getSelectedOptionId(context)) return
    options.find { it.optionId == optionId }?.let {
      getSharedPreferences(context).edit().putInt(sharedPreferencesKey, optionId).apply()
      apply(context, it)
      if (isShouldRecreateActivityOnOptionChanged) {
        recreateActivityIfPossible(context)
      }
    }
  }

  /**
   * 应用选项
   * @param context Context
   */
  fun apply(context: Context) {
    apply(context, getSelectedOption(context))
  }

  /**
   * 应用选项
   * @param context Context
   * @param selectedOption Option
   */
  protected abstract fun apply(context: Context, selectedOption: Option)


}

data class Option(val optionId: Int, val optionIcon: Int, val optionDescription: Int)
