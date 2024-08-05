package io.material.catalog.preferences

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.material.catalog.R

/**
 *  用于支持主题切换器功能的帮助程序类。
 *
 */
class PreferencesDialogHelper private constructor() {

  companion object {

    private var fragmentManager: FragmentManager? = null
      set(value) {
        if (field == null) {
          field = value
        }
      }

    private var enabled: Boolean? = null
      set(value) {
        if (field == null) {
          field = value
        }
      }

    @JvmStatic
    fun createHelper(fragmentManager: FragmentManager): PreferencesDialogHelper {
      this.fragmentManager = fragmentManager
      this.enabled = true
      return PreferencesDialogHelper()
    }

    @JvmStatic
    fun <T> createHelper(fragment: T): PreferencesDialogHelper where T : Fragment, T : PreferencesFragment {
      fragmentManager = fragment.parentFragmentManager
      enabled = fragment.isShouldShowDefaultDemoActionBar
        && fragment.activity is BaseCatalogActivity
        && (fragment.activity as BaseCatalogActivity).isPreferencesEnabled
      if (enabled as Boolean) {
        fragment.setHasOptionsMenu(true)
      }
      return PreferencesDialogHelper()

    }
  }

  fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
    if (enabled as Boolean) {
      menuInflater.inflate(R.menu.mtrl_preferences_menu, menu)
    }
  }

  fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
    if (enabled as Boolean) {
      if (menuItem.itemId == R.id.preferences) {
        showPreferences()
        return true
      }
    }
    return false
  }

  private fun showPreferences() {
    CatalogPreferencesDialogFragment().show(fragmentManager!!, "preferences")
  }
}

/**
 * 现此接口以允许 Fragment 与 {@link CatalogPreferencesHelper} 一起使用
 *
 * @constructor Create empty Preferences fragment
 */
interface PreferencesFragment {
  /**
   * 默认操作栏是否带有打开设置支持
   * @return Boolean
   */
//  fun isShouldShowDefaultDemoActionBar(): Boolean
  val isShouldShowDefaultDemoActionBar: Boolean
}
