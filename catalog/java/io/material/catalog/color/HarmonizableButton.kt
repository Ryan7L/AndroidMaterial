package io.material.catalog.color

import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.MaterialColors

/**
 * 可以协调的MaterialButton类。
 */
class HarmonizableButton private constructor(
  private val button: MaterialButton,
  @ColorInt private val colorValue: Int
) {
  companion object {
    @JvmStatic
    fun create(view: View, harmonizableButtonData: HarmonizableButtonData): HarmonizableButton {
      val colorRoles = MaterialColors.getColorRoles(
        view.context,
        view.resources.getColor(harmonizableButtonData.colorResId)
      )
      return HarmonizableButton(
        view.findViewById(harmonizableButtonData.buttonId),
        if (harmonizableButtonData.isLightButton) colorRoles.accentContainer else colorRoles.accent
      )
    }
  }
  fun updateColors(harmonize: Boolean){
    val maybeHarmonizedColor = if (harmonize) {
      MaterialColors.harmonizeWithPrimary(button.context, colorValue)
    }else {
      colorValue
    }
    button.setBackgroundColor(maybeHarmonizedColor)
    button.setTextColor(ColorDemoUtils.getTextColor(maybeHarmonizedColor))
  }
}
