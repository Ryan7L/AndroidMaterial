package io.material.catalog.draggable

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.customview.widget.ViewDragHelper
import androidx.customview.widget.ViewDragHelper.Callback

/**
 * 一个 CoordinatorLayout，其子项可以被拖动。
 */
class DraggableCoordinatorLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : CoordinatorLayout(context, attrs) {
  /**
   * V拖动子视图时使用的侦听器r
   */
  interface ViewDragListener {
    fun onViewCaptured(view: View, i: Int)
    fun onViewReleased(view: View, v: Float, v1: Float)
  }

  private val dragCallback = object : Callback() {
    /**
     * Called when the user's input indicates that they want to capture the given child view
     * with the pointer indicated by pointerId. The callback should return true if the user
     * is permitted to drag the given view with the indicated pointer.
     *
     *
     * ViewDragHelper may call this method multiple times for the same view even if
     * the view is already captured; this indicates that a new pointer is trying to take
     * control of the view.
     *
     *
     * If this method returns true, a call to [.onViewCaptured]
     * will follow if the capture is successful.
     *
     * @param child Child the user is attempting to capture
     * @param pointerId ID of the pointer attempting the capture
     * @return true if capture should be allowed, false otherwise
     */
    override fun tryCaptureView(child: View, pointerId: Int): Boolean {
      return child.visibility == View.VISIBLE && viewIsDraggableChild(child)
    }

    override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
      viewDragListener?.onViewCaptured(capturedChild, activePointerId)
    }

    override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
      viewDragListener?.onViewReleased(releasedChild, xvel, yvel)
    }

    override fun getViewHorizontalDragRange(child: View): Int {
      return child.width
    }

    override fun getViewVerticalDragRange(child: View): Int {
      return child.height
    }

    override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
      return left
    }

    override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
      return top
    }
  }
  private val viewDragHelper: ViewDragHelper = ViewDragHelper.create(this, dragCallback)
  private val draggableChildren = mutableListOf<View>()
  private var viewDragListener: ViewDragListener? = null

  fun addDraggableChild(child: View) {
    if (child.parent != this) {
      throw IllegalArgumentException()
    }
    draggableChildren.add(child)
  }

  fun removeDraggableChild(child: View) {
    if (child.parent != this) {
      throw IllegalArgumentException()
    }
    draggableChildren.remove(child)
  }

  override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
    return viewDragHelper.shouldInterceptTouchEvent(ev) || super.onInterceptTouchEvent(ev)
  }

  override fun onTouchEvent(ev: MotionEvent): Boolean {
    viewDragHelper.processTouchEvent(ev)
    return super.onTouchEvent(ev)
  }

  private fun viewIsDraggableChild(view: View) =
    draggableChildren.isEmpty() || draggableChildren.contains(view)

  fun setViewDragListener(viewDragListener: ViewDragListener) {
    this.viewDragListener = viewDragListener
  }
}
