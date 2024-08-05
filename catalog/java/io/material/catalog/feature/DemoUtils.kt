package io.material.catalog.feature

import android.app.Activity
import android.view.MenuItem
import android.view.View
import android.view.View.OnLayoutChangeListener
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.core.widget.NestedScrollView
import com.google.android.material.internal.ContextUtils
import com.google.android.material.snackbar.Snackbar


object DemoUtils {
  @JvmStatic
  fun <T : View> findViewsWithType(view: View, type: Class<T>): List<T> {
    val views = mutableListOf<T>()
    findViewsWithType(view, type, views)
    return views
  }

  @JvmStatic
  private fun <T : View> findViewsWithType(view: View, type: Class<T>, views: MutableList<T>) {

    if (type.isInstance(view)) {
      views.add(type.cast(view)!!)
    }
    if (view is ViewGroup) {
      view.children.forEach {
        findViewsWithType(it, type, views)
      }
    }
  }

  @JvmStatic
  fun showSnackBar(activity: Activity, item: MenuItem) =
    if (item.itemId == android.R.id.home) false else {
      Snackbar.make(
        activity.findViewById(android.R.id.content),
        item.title.toString(),
        Snackbar.LENGTH_SHORT
      ).show()
      true
    }

  @JvmStatic
  fun addBottomSpaceInsetsIfNeeded(scrollableViewAncestor: ViewGroup, demoContainer: ViewGroup) {
    val scrollViews = findViewsWithType(scrollableViewAncestor, ScrollView::class.java)

    val nestedScrollViews = findViewsWithType(scrollableViewAncestor, NestedScrollView::class.java)

    val scrollingViews = ArrayList<ViewGroup>(scrollViews + nestedScrollViews)
    ViewCompat.setOnApplyWindowInsetsListener(demoContainer) { v, insets ->
      scrollingViews.forEach {
        object : OnLayoutChangeListener {
          /**
           * 当视图的布局边界由于布局处理而发生变化时调用。
           *
           * @param v 边界已更改的视图。
           * @param left 视图的 left 属性的新值。
           * @param top 视图顶部属性的新值。
           * @param right 视图右部属性的新值。
           * @param bottom 视图底部属性的新值。
           * @param oldLeft 视图的 left 属性的前
           * @param oldTop 视图顶部属性的先前值。
           * @param oldRight 视图右侧属性的前一个值。
           * @param oldBottom 视图底部属性的先前值。
           */
          override fun onLayoutChange(
            v: View?,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
          ) {
            it.removeOnLayoutChangeListener(this)
            val systemWindowInsetBottom = insets.systemWindowInsetBottom
            if (!isShouldApplyBottomInset(it, systemWindowInsetBottom)) {
              return
            }
            val insetsBottom = calculateBottomInset(it, systemWindowInsetBottom)
            val scrollableContent = it.getChildAt(0)
            scrollableContent.setPadding(
              scrollableContent.paddingLeft,
              scrollableContent.paddingTop,
              scrollableContent.paddingRight,
              insetsBottom
            )
          }

        }
      }
      return@setOnApplyWindowInsetsListener insets
    }
  }

  private fun calculateBottomInset(scrollView: ViewGroup, systemWindowInsetBottom: Int): Int {
    val scrollableContent = scrollView.getChildAt(0)
    val calculatedInset =
      systemWindowInsetBottom.coerceAtMost(scrollableContent.height + systemWindowInsetBottom - scrollView.height)
    return calculatedInset.coerceAtLeast(0)
  }

  private fun isShouldApplyBottomInset(
    scrollView: ViewGroup,
    systemWindowInsetBottom: Int
  ): Boolean {
    val scrollableContent = scrollView.getChildAt(0)
    val scrollableContentHeight = scrollableContent.height
    val scrollViewHeight = scrollView.height

    val scrollViewLocation = IntArray(2)
    scrollView.getLocationOnScreen(scrollViewLocation)
    val activity = ContextUtils.getActivity(scrollView.context)
    return scrollViewHeight + scrollViewLocation[1] >= getContentViewHeight(activity)
      && scrollableContentHeight + systemWindowInsetBottom >= scrollViewHeight
  }

  private fun getContentViewHeight(activity: Activity?): Int {
    return activity?.findViewById<ViewGroup>(android.R.id.content)?.height ?: 0
  }
}
