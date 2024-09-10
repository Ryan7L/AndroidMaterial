package io.material.catalog.topappbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import kotlin.math.abs

/**
 * 折叠顶部应用栏演示的基类Fragment。
 */
private const val TAG = "BaseTopAppBarCollapsing"

abstract class BaseTopAppBarCollapsingDemoFragment : DemoFragment() {

  /**
   * 折叠工具栏的布局ID
   */
  @get:LayoutRes
  protected abstract val collapsingToolBarLayoutResId: Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = layoutInflater.inflate(collapsingToolBarLayoutResId, container, false)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    val appbarLayout = view.findViewById<AppBarLayout>(R.id.appbarlayout)
    appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
      val totalScrollRange = appBarLayout.totalScrollRange
      val collapsePercentage = abs(verticalOffset) / totalScrollRange
      if (collapsePercentage == 0) {
        //完全展开
        Log.i(TAG, "onCreateDemoView: 完全展开")
      } else {
        //部分折叠或完全折叠
        Log.i(TAG, "onCreateDemoView: 部分折叠或完全折叠")
      }
    }
    return view
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.cat_topappbar_menu, menu)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
