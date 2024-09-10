package io.material.catalog.color

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils

object ColorDemoUtils {
  @JvmStatic
  fun getTextColor(@ColorInt backgroundColor: Int): Int {
    //使用与背景颜色对比度最佳的文本颜色。
    return if (ColorUtils.calculateContrast(
        Color.BLACK,
        backgroundColor
      ) > ColorUtils.calculateContrast(Color.WHITE, backgroundColor)
    ) {
      Color.BLACK
    } else {
      Color.WHITE
    }
  }
}
