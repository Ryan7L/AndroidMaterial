package io.material.catalog.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.color.MaterialColors
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class TopAppBarScrollingDemoFragment : DemoFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION")
    setHasOptionsMenu(true)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_topappbar_scrolling_fragment, container, false)
    (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
    val appbarLayout = view.findViewById<AppBarLayout>(R.id.appbarlayout)
    appbarLayout.setStatusBarForegroundColor(
      MaterialColors.getColor(appbarLayout, R.attr.colorSurface)
    )
    view.findViewById<Toolbar>(R.id.toolbar)
    return view
  }

  @Deprecated("Deprecated in Java")
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.cat_topappbar_menu, menu)
    @Suppress("DEPRECATION")
    super.onCreateOptionsMenu(menu, inflater)
  }

  @Deprecated("Deprecated in Java")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    @Suppress("DEPRECATION")
    return super.onOptionsItemSelected(item) || DemoUtils.showSnackBar(requireActivity(), item)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
