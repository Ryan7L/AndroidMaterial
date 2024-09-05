package io.material.catalog.color

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import com.google.android.material.color.ColorRoles
import com.google.android.material.color.MaterialColors

class ColorGridData private constructor(
  val colorRoles: ColorRoles,
  val colorRoleNames: ColorRoleNames
) {
  companion object {
    @JvmStatic
    fun createFromColorResId(
      context: Context,
      @ColorRes colorResourceId: Int,
      @ArrayRes colorNameIds: Int
    ): ColorGridData {
      return createFromColorValue(
        context, context.resources.getColor(colorResourceId), colorNameIds
      )
    }

    @JvmStatic
    fun createFromColorValue(
      context: Context,
      @ColorInt seedColorValue: Int,
      @ArrayRes colorNameIds: Int
    ): ColorGridData {
      val colorNames = context.resources.getStringArray(colorNameIds)
      return ColorGridData(
        MaterialColors.getColorRoles(context, seedColorValue),
        ColorRoleNames(colorNames[0], colorNames[1], colorNames[2], colorNames[3])
      )
    }
  }
}
