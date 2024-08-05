package io.material.catalog.feature

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat

/**
 * 一个视图，其自身高度与底部窗口插图一样高,避免与屏幕底部的系统 UI 元素重叠
 * @property systemWindowInsetBottom Int
 * @constructor
 */
class BottomWindowInsetView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

  init {
    super.setVisibility(GONE)
  }

  private var systemWindowInsetBottom = 0

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    var parent = parent as? ViewGroup
    while (parent != null && !parent.fitsSystemWindows) {
      parent = parent.parent as? ViewGroup
    }
    if (parent == null) {
      return
    }
    ViewCompat.setOnApplyWindowInsetsListener(parent) { _, insets ->
      systemWindowInsetBottom = insets.systemWindowInsetBottom
      super.setVisibility(if (systemWindowInsetBottom == 0) GONE else VISIBLE)
      if (systemWindowInsetBottom > 0) {
        requestLayout()
      }
      return@setOnApplyWindowInsetsListener insets
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = MeasureSpec.getSize(widthMeasureSpec)
    setMeasuredDimension(width, systemWindowInsetBottom)
  }

  override fun setVisibility(visibility: Int) {
    throw UnsupportedOperationException("Use ViewCompat.setOnApplyWindowInsetsListener instead")
  }
}
//系统窗口插图是指 Android 系统在屏幕某些区域显示的 UI 元素，例如状态栏、导航栏、键盘等，这些元素可能会遮挡应用程序的内容。  WindowInsets 提供了关于这些系统窗口的信息，例如它们的大小和位置， 以便应用程序可以相应地调整其布局。
//作用:
//避免内容被遮挡: 应用程序可以使用 WindowInsets 来确定系统窗口的位置和大小，并相应地调整其布局， 以确保内容不会被遮挡。  例如，应用程序可以将内容内边距设置为状态栏的高度， 以防止内容被状态栏遮挡。
//实现沉浸式体验: 应用程序可以使用 WindowInsets 来实现沉浸式体验，例如在状态栏和导航栏后面显示内容。
//处理键盘弹出: 应用程序可以使用 WindowInsets 来处理键盘弹出事件，例如调整布局以避免内容被键盘遮挡。
//类型: WindowInsets 包含不同类型的插图信息，例如：
//systemWindowInsetTop: 状态栏高度
//systemWindowInsetBottom: 导航栏高度
//systemWindowInsetLeft: 左侧系统窗口的宽度 (通常为 0)
//systemWindowInsetRight: 右侧系统窗口的宽度 (通常为 0)
//imeInsets: 输入法编辑器 (IME) 窗口 (例如键盘) 的信息
//获取 WindowInsets: 你可以通过 View.getRootWindowInsets() 方法或 ViewCompat.getRootWindowInsets(view) 方法来获取 WindowInsets 对象。
//使用 WindowInsets: 你可以使用 WindowInsets 对象中的方法来获取不同类型的插图信息，例如 getSystemWindowInsetTop()、getSystemWindowInsetBottom() 等
