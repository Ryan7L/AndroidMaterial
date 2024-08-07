package io.material.catalog.topappbar

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.util.forEach
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.color.MaterialColors
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

/**
 * 折叠顶部应用程序栏演示
 */
class TopAppBarCollapsingMultilineDemoFragment : DemoFragment() {
  private var linesMap: SparseIntArray? = null

  @ColorInt
  private var colorPrimary: Int = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION")
    setHasOptionsMenu(true)
    linesMap = SparseIntArray()
    linesMap!!.put(R.id.maxLines1, 1)
    linesMap!!.put(R.id.maxLines2, 2)
    linesMap!!.put(R.id.maxLines3, 3)
    linesMap!!.put(R.id.maxLines5, 5)
    linesMap!!.put(R.id.maxLines6, 6)
    linesMap!!.put(R.id.maxLines7, 7)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view =
      inflater.inflate(R.layout.cat_topappbar_collapsing_multiline_fragment, container, false)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    colorPrimary = MaterialColors.getColor(view, R.attr.colorPrimary)
    return view
  }

  @Deprecated("Deprecated in Java", ReplaceWith(
      "@Suppress(\"DEPRECATION\") super.onCreateOptionsMenu(menu, inflater)",
      "io.material.catalog.feature.DemoFragment"
  )
  )
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.cat_topappbar_menu_maxlines, menu)
    @Suppress("DEPRECATION")
    super.onCreateOptionsMenu(menu, inflater)
  }
  @Deprecated("Deprecated in Java")
  override fun onPrepareOptionsMenu(menu: Menu) {
    @Suppress("DEPRECATION")
    super.onPrepareOptionsMenu(menu)
    val collapsingToolbarLayout =
      requireView().findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarlayout)
    val maxLines = collapsingToolbarLayout.maxLines
    linesMap?.forEach { key, value ->
      val item = menu.findItem(key)
      item.title = getString(R.string.menu_max_lines, value).let {
        if (maxLines == value){
          val spannable = SpannableString(it)
          spannable.setSpan(ForegroundColorSpan(colorPrimary),0,it.length,0)
          spannable
        }else{
          it
        }
      }
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val collapsingToolbarLayout =
      requireView().findViewById<CollapsingToolbarLayout>(R.id.collapsingtoolbarlayout)
    collapsingToolbarLayout.maxLines = linesMap?.get(item.itemId, 1) ?: 1
    @Suppress("DEPRECATION")
    return super.onOptionsItemSelected(item)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
