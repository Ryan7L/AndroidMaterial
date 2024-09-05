package io.material.catalog.color

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.material.catalog.R

class ColorGrid(
  private val materialColorSpecAccent: MaterialColorSpec,
  private val materialColorSpecOnAccent: MaterialColorSpec,
  private val materialColorSpecAccentContainer: MaterialColorSpec,
  private val materialColorSpecOnAccentContainer: MaterialColorSpec
) {
  companion object{
    fun createFromColorGridData(colorGridData: ColorGridData): ColorGrid{
      val colorRoles = colorGridData.colorRoles
      val colorRoleNames = colorGridData.colorRoleNames
      val materialColorSpecs = arrayOf(
        MaterialColorSpec.createFromColorValue(
          colorRoleNames.onAccentName,colorRoles.accent
        ),
        MaterialColorSpec.createFromColorValue(
          colorRoleNames.onAccentName,colorRoles.onAccent),
        MaterialColorSpec.createFromColorValue(
          colorRoleNames.accentContainerName,colorRoles.accentContainer),
        MaterialColorSpec.createFromColorValue(
          colorRoleNames.onAccentContainerName,colorRoles.onAccentContainer)
        )
      return ColorGrid(
        materialColorSpecs[0],
        materialColorSpecs[1],
        materialColorSpecs[2],
        materialColorSpecs[3]
      )
    }
    fun createFromAttrResId(context: Context,colorNames: Array<String>,attrResIds: IntArray): ColorGrid{
      if (colorNames.size < 4 || colorNames.size != attrResIds.size){
        throw IllegalArgumentException("Color names need to be at least four and correspond to attribute resource ids.")
      }
      return ColorGrid(MaterialColorSpec.createFromAttrResId(context,colorNames[0],attrResIds[0]),
        MaterialColorSpec.createFromAttrResId(context,colorNames[1],attrResIds[1]),
        MaterialColorSpec.createFromAttrResId(context,colorNames[2],attrResIds[2]),
        MaterialColorSpec.createFromAttrResId(context,colorNames[3],attrResIds[3]))
    }
    private fun bindColorSpecItem(gridView: View,textViewId: Int,materialColorSpec: MaterialColorSpec){
      val colorSpec = gridView.findViewById<TextView>(textViewId)
      colorSpec.text = materialColorSpec.description
      colorSpec.setTextColor(ColorDemoUtils.getTextColor(materialColorSpec.colorValue))
      colorSpec.setBackgroundColor(materialColorSpec.colorValue)
    }
  }
fun renderView(layoutInflater: LayoutInflater, container: ViewGroup): View {
  return layoutInflater.inflate(R.layout.cat_colors_grid,container,false).apply {
    bindColorSpecItem(this,R.id.cat_color_accent,materialColorSpecAccent)
    bindColorSpecItem(this,R.id.cat_color_on_accent,materialColorSpecOnAccent)
    bindColorSpecItem(this,R.id.cat_color_accent_container,materialColorSpecAccentContainer)
    bindColorSpecItem(this,R.id.cat_color_on_accent_container,materialColorSpecOnAccentContainer)

  }
}
}
