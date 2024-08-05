package io.material.catalog.preferences

import android.content.Context
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import com.google.common.collect.ImmutableList
import io.material.catalog.R
import io.material.catalog.windowpreferences.WindowPreferencesManager

private const val EDGE_TO_EDGE_OPTION_ID_ON = 1
private const val EDGE_TO_EDGE_OPTION_ID_OFF = 2
private val DEFAULT_OPTION = Option(
  EDGE_TO_EDGE_OPTION_ID_ON,
  R.drawable.ic_edge_to_edge_enable_24dp,
  R.string.dynamic_color_preference_option_on
)
private val optionList = ImmutableList.of(
  DEFAULT_OPTION,
  Option(
    EDGE_TO_EDGE_OPTION_ID_OFF,
    R.drawable.ic_edge_to_edge_disable_24dp,
    R.string.edge_to_edge_preference_option_off
  )
)

/**
 * 边到边的配置项(Android edge-to-edge 是指让应用程序的内容扩展到设备屏幕的边缘， 覆盖系统栏（ 状态栏、 导航栏） 的区域， 从而提供更沉浸式的用户体验。)
 */
class EdgeToEdgePreference : CatalogPreference(R.string.edge_to_edge_preference_description) {
  private var windowPreferencesManager: WindowPreferencesManager? = null


  @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
  override val isEnabled: Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

  /**
   * 应用所选选项以在应用程序上生效。
   */
  override fun apply(context: Context, selectedOption: Option) {
    val isEdgeToEdgeOn = selectedOption.optionId == EDGE_TO_EDGE_OPTION_ID_ON
    windowPreferencesManager ?: also {
      windowPreferencesManager = WindowPreferencesManager(context)
    }
    windowPreferencesManager?.let {
      if (isEdgeToEdgeOn != it.isEdgeToEdgeEnabled) {
        it.toggleEdgeToEdgeEnabled()
      }
    }
  }

  override val isShouldRecreateActivityOnOptionChanged: Boolean = true

  override val options: List<Option> = optionList

  override val defaultOption: Option = DEFAULT_OPTION

}
