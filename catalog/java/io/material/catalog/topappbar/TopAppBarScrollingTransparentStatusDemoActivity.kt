package io.material.catalog.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.color.MaterialColors
import io.material.catalog.R

class TopAppBarScrollingTransparentStatusDemoActivity : BaseTopAppBarActionBarDemoActivity() {
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(
      R.layout.cat_topappbar_scrolling_transparent_statusbar_activity,
      viewGroup,
      false
    )
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
//    view.findViewById<AppBarLayout>(R.id.appbarlayout).apply {
//      setStatusBarForegroundColor(
//      MaterialColors.getColor(
//        this,
//        R.attr.colorSurface
//      )
//    )
//    }
    return view
  }
}
