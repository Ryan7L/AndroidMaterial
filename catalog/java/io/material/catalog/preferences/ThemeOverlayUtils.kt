package io.material.catalog.preferences

import android.app.Activity
import android.util.SparseIntArray
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.util.forEach
import com.google.android.material.color.ThemeUtils

private val themeOverlays = SparseIntArray()

object ThemeOverlayUtils {
  const val NO_THEME_OVERLAY: Int = 0

  /**
   * 设置主题覆盖
   * @param id Int 主题的标识ID
   * @param themeOverlay Int 主题的样式的Id
   */
  @JvmStatic
  fun setThemeOverlay(@IdRes id: Int, @StyleRes themeOverlay: Int) {
    if (themeOverlay == NO_THEME_OVERLAY) {
      themeOverlays.delete(id)
    } else {
      themeOverlays.put(id, themeOverlay)
    }
  }

  /**
   * 删除主题覆盖
   * @param id Int 主题的标识ID
   */
  @JvmStatic
  fun deleteThemeOverlay(@IdRes id: Int) {
    themeOverlays.delete(id)
  }

  /**
   * 清空主题覆盖
   * @param activity Activity 当前Activity
   */
  @JvmStatic
  fun clearThemeOverlays(activity: Activity) {
    themeOverlays.clear()
    activity.recreate()
  }

  /**
   * 获取主题覆盖
   * @param id Int 主题的标识ID
   * @return Int 主题的样式的Id
   */
  @JvmStatic
  fun getThemeOverlay(@IdRes id: Int) = themeOverlays.get(id)

  /**
   * 应用主题覆盖
   * @param activity Activity 当前Activity
   */
  @JvmStatic
  fun applyThemeOverlays(activity: Activity) {
    themeOverlays.forEach { key, value ->
      ThemeUtils.applyThemeOverlay(activity, value)
    }
  }

}
