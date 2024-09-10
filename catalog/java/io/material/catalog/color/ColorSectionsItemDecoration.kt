package io.material.catalog.color

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import io.material.catalog.R

class ColorSectionsItemDecoration(context: Context, private val adapter: ColorsAdapter) :
  ItemDecoration() {
  private val space: Int = context.resources.getDimensionPixelSize(R.dimen.cat_colors_header_space)
  override fun getItemOffsets(
    outRect: Rect,
    view: View,
    parent: RecyclerView,
    state: RecyclerView.State
  ) {
    super.getItemOffsets(outRect, view, parent, state)
    //除第一个标题之外，在每个标题上方添加空格。
    val position = parent.getChildAdapterPosition(view)
    if (position != 0 && adapter.getItemViewType(position) == ColorsAdapter.VIEW_TYPE_HEADER) {
      outRect.set(0, space, 0, 0)
    }
  }
}
