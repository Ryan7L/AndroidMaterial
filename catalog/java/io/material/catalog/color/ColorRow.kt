package io.material.catalog.color

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.google.android.material.color.MaterialColors
import io.material.catalog.R

/**
 * 颜色行中项目的类。 ColorRow 由两个 [ColorRoleItem] 对象组成。左侧 colorRoleItem 代表容器颜色，右侧 colorRoleItem 代表内容颜色。
 */
class ColorRow(private val colorRoleItemLeft: ColorRoleItem, private val colorRoleItemRight: ColorRoleItem?) {

  private lateinit var catColorsSchemeRow: View
  private fun bindColorRoleItem(
    view: View,
    @IdRes textViewId: Int,
    @StringRes colorRoleTextResID: Int,
    @AttrRes colorAttrResId: Int
  ){
    val colorRole = view.findViewById<TextView>(textViewId)
    colorRole.setText(colorRoleTextResID)
    colorRole.setTextColor(ColorDemoUtils.getTextColor(MaterialColors.getColor(catColorsSchemeRow,colorAttrResId)))
    colorRole.setBackgroundColor(MaterialColors.getColor(catColorsSchemeRow,colorAttrResId))
  }
  fun addTo(layoutInflater: LayoutInflater, layout: LinearLayout){
    catColorsSchemeRow = layoutInflater.inflate(R.layout.cat_colors_scheme_row,layout,false)
    bindColorRoleItem(
      catColorsSchemeRow,
      R.id.cat_color_role_left,
      colorRoleItemLeft.colorRoleStringResId,
      colorRoleItemLeft.colorRoleAttrResId
    )
    colorRoleItemRight?.let {
      bindColorRoleItem(
        catColorsSchemeRow,
        R.id.cat_color_role_right,
        it.colorRoleStringResId,
        it.colorRoleAttrResId)
    }
    layout.addView(catColorsSchemeRow)
  }
}
