package io.material.catalog.color

import android.content.Context
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.google.android.material.color.MaterialColors

/**
 * 表示材料规范中定义的颜色值。
 */
class MaterialColorSpec(val description: String, @ColorInt val colorValue: Int) {

  companion object{
    @JvmStatic
    fun createFromResource(context: Context,@ColorRes colorRes: Int): MaterialColorSpec{
      return MaterialColorSpec(context.resources.getResourceEntryName(colorRes),ContextCompat.getColor(context,colorRes))
    }
    @JvmStatic
    fun createFromColorValue(colorNameResource: String,@ColorInt colorValue: Int): MaterialColorSpec{
      return MaterialColorSpec(colorNameResource,colorValue)
    }
    @JvmStatic
    fun createFromAttrResId(context: Context,colorNameResource: String,@AttrRes attrRes: Int): MaterialColorSpec{
      return createFromColorValue(colorNameResource,MaterialColors.getColor(context,attrRes,colorNameResource + "cannot be resolved."))
    }
  }
}
