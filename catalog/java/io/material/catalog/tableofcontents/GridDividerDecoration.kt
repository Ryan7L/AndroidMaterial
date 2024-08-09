package io.material.catalog.tableofcontents

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * RecyclerView的item之间的分割线
 */
class GridDividerDecoration(
  @Px private val dividerSize: Int,
  @ColorInt private val dividerColor: Int,
  private val spanCount: Int
) : RecyclerView.ItemDecoration() {

  private val dividerPaint = Paint().apply {
    color = dividerColor
    strokeWidth = dividerSize.toFloat()
    style = Paint.Style.STROKE
    isAntiAlias = true
  }
  private val bounds = Rect()

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
    drawHorizontal(c, parent)
    drawVertical(c, parent)
  }

  private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
    val childCount = parent.childCount
    for (i in 0 until childCount) {
      val childView = parent.getChildAt(i)
      parent.getDecoratedBoundsWithMargins(childView, bounds)
      canvas.drawLine(
        bounds.left.toFloat(),
        bounds.bottom.toFloat(),
        bounds.right.toFloat(),
        bounds.bottom.toFloat(),
        dividerPaint
      )
    }
  }

  private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
    val childCount = parent.childCount
    val isRTL = parent.layoutDirection == View.LAYOUT_DIRECTION_RTL
    for (i in 0 until childCount) {
      val childView = parent.getChildAt(i)
      if (isChildInLastColumn(parent, childView)) {
        continue
      }
      parent.getDecoratedBoundsWithMargins(childView, bounds)
      val x = if (isRTL) bounds.left else bounds.right
      canvas.drawLine(
        x.toFloat(),
        bounds.top.toFloat(),
        x.toFloat(),
        bounds.bottom.toFloat(),
        dividerPaint
      )
    }
  }

  private fun isChildInLastColumn(parent: RecyclerView, child: View): Boolean {
    return parent.getChildAdapterPosition(child) % spanCount == spanCount - 1
  }
}
