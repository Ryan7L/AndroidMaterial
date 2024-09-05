package io.material.catalog.feature

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.format.Formatter
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.google.common.collect.EvictingQueue
import io.material.catalog.R
/**
 * 显示应用程序内存使用情况的小部件。
 */
class MemoryView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
  private val PLOT_MARGIN = 10
  private val STROKE_WIDTH = 5
  private val MIN_PIXELS_FOR_GRAPH = 60

  private val memSnapshots = EvictingQueue.create<Long>(5)
  private val paint = Paint().apply {
    style = Paint.Style.FILL_AND_STROKE
    strokeWidth = STROKE_WIDTH.toFloat()
    isAntiAlias = true
  }
  private val path = Path()
  private var maxMemoryInBytes = 0L

  /**
   * 根据最后的内存快照数据点在文本旁边绘制图表。
   * @param canvas Canvas
   */
  override fun onDraw(canvas: Canvas) {
    path.reset()
    val textWidth = paint.measureText(text.toString()).toInt()
    val availableWidth = measuredWidth - textWidth - paddingLeft - paddingRight
    if (availableWidth < MIN_PIXELS_FOR_GRAPH || memSnapshots.isEmpty()) return
    val startX = textWidth + paddingLeft + PLOT_MARGIN
    val availableHeight = measuredHeight / 2
    path.moveTo(startX.toFloat(), availableHeight.toFloat())
    var prevPercentage = 0f
    var index: Byte = 0
    memSnapshots.forEach {
      index++
      val percentage = it / maxMemoryInBytes.toFloat()
      val x = startX + (availableWidth / memSnapshots.size) * index
      var amplificationFactor = 1f
      if (prevPercentage > percentage) {
        amplificationFactor = 5f
      } else if (prevPercentage < percentage) {
        amplificationFactor = .2f
      }
      val y = availableHeight - (percentage * availableHeight) * amplificationFactor
      prevPercentage = percentage
      path.lineTo(x.toFloat(), y)
      path.addCircle(x.toFloat(), y, 4f, Path.Direction.CCW)
    }
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    val typedValue = TypedValue()
    context.theme.resolveAttribute(R.attr.colorPrimary, typedValue, true)
    paint.color = typedValue.data
  }

  fun refreshMemStats(runtime: Runtime) {
    maxMemoryInBytes = runtime.maxMemory()
    val availableMemInBytes = maxMemoryInBytes - (runtime.totalMemory() - runtime.freeMemory())
    val usedMemInBytes = maxMemoryInBytes - availableMemInBytes
    val usedMemInPercentage = usedMemInBytes * 100 / maxMemoryInBytes
    memSnapshots.add(usedMemInBytes)
    text = context.getString(
      R.string.cat_demo_memory_usage,
      Formatter.formatShortFileSize(context, usedMemInBytes),
      Formatter.formatShortFileSize(context, maxMemoryInBytes),
      usedMemInPercentage
    )
  }
}
