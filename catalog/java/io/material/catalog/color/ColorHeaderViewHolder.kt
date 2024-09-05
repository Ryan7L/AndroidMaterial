package io.material.catalog.color

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R

class ColorHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
  LayoutInflater.from(
    parent.context
  ).inflate(R.layout.cat_colors_palette_header, parent, false)
) {

  val header: TextView = itemView as TextView
  fun bind(headerItem: ColorHeaderItem) {
    header.text = headerItem.displayName
    header.setBackgroundResource(headerItem.backgroundColorRes)
  }
}
