package io.material.catalog.color

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.material.catalog.R

class ColorsAdapter(private val context: Context, @ArrayRes colorItems: Int?) :
  RecyclerView.Adapter<ViewHolder>() {
  companion object {
    val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_COLOR = 1


  }

  val items = ArrayList<ColorAdapterItem>()

  init {
    colorItems?.let {
      val colorsArray = context.resources.obtainTypedArray(it)
      for (i in 0 until colorsArray.length()) {
        val colors = getColorsFromArrayResource(colorsArray.getResourceId(i, 0))
        items.add(ColorHeaderItem(colors))
        items.addAll(colors)
      }
      colorsArray.recycle()
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (viewType) {
      VIEW_TYPE_HEADER -> ColorHeaderViewHolder(parent)
      VIEW_TYPE_COLOR -> ColorViewHolder(parent)
      else -> throw IllegalArgumentException("Unknown view type $viewType")
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    when (getItemViewType(position)) {
      VIEW_TYPE_HEADER -> {
        val header = items[position] as ColorHeaderItem
        (holder as ColorHeaderViewHolder).bind(header)
      }

      VIEW_TYPE_COLOR -> {
        val item = items[position] as ColorItem
        (holder as ColorViewHolder).bind(item)
      }
    }
  }

  override fun getItemViewType(position: Int): Int {
    return if (items[position] is ColorHeaderItem) VIEW_TYPE_HEADER else VIEW_TYPE_COLOR
  }

  override fun getItemCount(): Int {
    return items.size
  }

  private fun getColorsFromArrayResource(@ArrayRes arrayRes: Int): List<ColorItem> {
    val colors = ArrayList<ColorItem>()
    val colorsArray = context.resources.obtainTypedArray(arrayRes)
    for (i in 0 until colorsArray.length()) {
      val color = colorsArray.getResourceId(i, 0)
      colors.add(ColorItem(context, color))
    }
    colorsArray.recycle()
    return colors
  }

}

class ColorViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
  LayoutInflater.from(parent.context).inflate(
    R.layout.cat_colors_palette_item, parent, false
  )
) {
  private val context = itemView.context
  private val nameView = itemView.findViewById<TextView>(R.id.name)
  private val descriptionView = itemView.findViewById<TextView>(R.id.description)
  fun bind(colorItem: ColorItem) {
    val value = ContextCompat.getColor(context, colorItem.colorRes)
    val colorResName = colorItem.colorResName
    val resQualifier =
      if (colorResName.startsWith(ColorHeaderItem.SYSTEM_PREFIX)) "@android:color/" else "@color/"
    nameView.text = context.resources.getString(R.string.cat_color_res, resQualifier, colorResName)
    descriptionView.text = "#%06x".format(value and 0xFFFFFF)
    val textColor = getTextColor(colorItem)
    nameView.setTextColor(textColor)
    descriptionView.setTextColor(textColor)
    itemView.setBackgroundResource(colorItem.colorRes)
  }

  private fun getTextColor(colorItem: ColorItem): Int {
    return ColorDemoUtils.getTextColor(colorItem.colorValue)
  }
}
