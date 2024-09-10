package io.material.catalog.preferences

import android.content.Context
import android.os.Bundle
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.material.catalog.R
import javax.inject.Inject

class CatalogPreferencesDialogFragment : BottomSheetDialogFragment(), HasAndroidInjector {
  /**
   * Preferences 配置工具类
   */
  @Inject
  lateinit var preferences: BaseCatalogPreferences

  /**
   * Child fragment injector
   */
  @Inject
  lateinit var childFragmentInjector: DispatchingAndroidInjector<Any>

  /**
   * 按钮 ID 和 配置Id 的映射
   */
  private val buttonIdToOptionId = SparseIntArray()

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.mtrl_preferences_dialog, container, false)
    val preferencesLayout = rootView.findViewById<LinearLayout>(R.id.preferences_layout)
    preferences.preferences.forEach {
      preferencesLayout.addView(createPreferenceView(inflater, preferencesLayout, it))
    }
    return rootView
  }

  /**
   * 创建首选项配置视图
   * @param inflater LayoutInflater
   * @param rootView ViewGroup
   * @param preference CatalogPreference
   * @return View
   */
  private fun createPreferenceView(
    inflater: LayoutInflater,
    rootView: ViewGroup,
    preference: CatalogPreference
  ): View {
    val isEnable = preference.isEnabled
    val preferenceView =
      inflater.inflate(R.layout.mtrl_preferences_dialog_preference, rootView, false)
    preferenceView.findViewById<TextView>(R.id.preference_description).apply {
      isEnabled = isEnable
      setText(preference.preferenceDescription)
    }
    //MaterialButtonToggleGroup:一组相关的、可切换的MaterialButton的通用容器。此组中的MaterialButton将显示在一行上。
    //此布局目前仅支持MaterialButton类型的子视图
    val buttonToggleGroup =
      preferenceView.findViewById<MaterialButtonToggleGroup>(R.id.preference_options)
    val selectedOptionId = preference.getSelectedOption(preferenceView.context).optionId
    preference.options.forEach {
      val button = createOptionButton(inflater, buttonToggleGroup, it)
      button.isEnabled = isEnable
      buttonToggleGroup.addView(button)
      button.isChecked = selectedOptionId == it.optionId
    }
    buttonToggleGroup.isEnabled = isEnable
    if (isEnable) {
      buttonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
        if (isChecked) {
          preference.setSelectedOption(group.context, buttonIdToOptionId.get(checkedId))
        }
      }
    }
    return preferenceView
  }

  /**
   * 创建Button
   * @param inflater LayoutInflater
   * @param rootView ViewGroup
   * @param option Option
   * @return MaterialButton
   */
  private fun createOptionButton(
    inflater: LayoutInflater,
    rootView: ViewGroup,
    option: Option
  ): MaterialButton {
    val button = inflater.inflate(
      R.layout.mtrl_preferences_dialog_option_button,
      rootView,
      false
    ) as MaterialButton
    val id = View.generateViewId()
    buttonIdToOptionId.append(id, option.optionId)
    button.id = id
    button.setIconResource(option.optionIcon)
    button.setText(option.optionDescription)
    return button
  }

  override fun androidInjector(): AndroidInjector<Any> = childFragmentInjector
}
