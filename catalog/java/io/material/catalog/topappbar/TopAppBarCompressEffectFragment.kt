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
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.tabs.TabLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

/**
 * 显示应用程序的滚动顶部应用程序栏演示的Fragment
 */
class TopAppBarCompressEffectFragment : DemoFragment() {
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
    val view = inflater.inflate(R.layout.cat_topappbar_compress_effect_fragment, container, false)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    val appbarLayout = view.findViewById<AppBarLayout>(R.id.appbarlayout)
    appbarLayout.setStatusBarForegroundColor(
      MaterialColors.getColor(
        appbarLayout,
        R.attr.colorSurface
      )
    )

    val tabLayout = view.findViewById<TabLayout>(R.id.tabs)
    val showHideTabsButton = view.findViewById<MaterialSwitch>(R.id.show_hide_tabs_button)
    updateTabVisibility(showHideTabsButton, tabLayout, showHideTabsButton.isChecked)
    showHideTabsButton.setOnCheckedChangeListener { buttonView, isChecked ->
      updateTabVisibility(showHideTabsButton, tabLayout, isChecked)
    }
    val showHideToolbarButton = view.findViewById<MaterialSwitch>(R.id.show_hide_toolbar_button)
    showHideToolbarButton.setOnCheckedChangeListener { buttonView, isChecked ->
      showHideToolbarButton.text =
        getString(if (isChecked) R.string.cat_topappbar_compress_hide_toolbar_toggle_label else R.string.cat_topappbar_compress_show_toolbar_toggle_label)
      if (isChecked) {
        (activity as AppCompatActivity).supportActionBar?.show()
      } else {
        (activity as AppCompatActivity).supportActionBar?.hide()
      }
    }

    return view
  }

  private fun updateTabVisibility(button: MaterialSwitch, tabLayout: TabLayout, show: Boolean) {

    button.text =
      getString(if (show) R.string.cat_topappbar_compress_hide_tabs_toggle_label else R.string.cat_topappbar_compress_show_tabs_toggle_label)
    tabLayout.visibility = if (show) View.VISIBLE else View.GONE
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

