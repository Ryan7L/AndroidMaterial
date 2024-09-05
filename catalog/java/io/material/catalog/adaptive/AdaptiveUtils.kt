package io.material.catalog.adaptive

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R

object AdaptiveUtils {
  const val MEDIUM_SCREEN_WIDTH_SIZE = 600
  const val LARGE_SCREEN_WIDTH_SIZE = 1240

  /**
   * 根据屏幕尺寸更新主导航视图组件的可见性。
   * 小屏幕布局应该有一个底部导航和一个可选的 fab。中等布局应该有一个带有 fab 的导航栏，
   * 大布局应该有一个带有扩展 fab 的抽屉式导航栏。
   * @param screenWidth Int
   * @param drawerLayout DrawerLayout
   * @param modalNavDrawer NavigationView
   * @param fab FloatingActionButton?
   * @param bottomNav BottomNavigationView
   * @param navRail NavigationRailView
   * @param navDrawer NavigationView
   * @param navFab ExtendedFloatingActionButton
   */
  fun updateNavigationViewLayout(
    screenWidth: Int,
    drawerLayout: DrawerLayout,
    modalNavDrawer: NavigationView,
    fab: FloatingActionButton?,
    bottomNav: BottomNavigationView,
    navRail: NavigationRailView,
    navDrawer: NavigationView,
    navFab: ExtendedFloatingActionButton
  ) {
    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    setNavRailButtonOnClickListener(
      drawerLayout,
      navRail.headerView?.findViewById(R.id.nav_button),
      modalNavDrawer
    )
    setModalDrawerButtonOnClickListener(
      drawerLayout,
      modalNavDrawer.getHeaderView(0)?.findViewById(R.id.nav_button),
      modalNavDrawer
    )
    modalNavDrawer.setNavigationItemSelectedListener {
      modalNavDrawer.setCheckedItem(it)
      drawerLayout.closeDrawer(modalNavDrawer)
      true
    }
    if (screenWidth < MEDIUM_SCREEN_WIDTH_SIZE) {
      fab?.visibility = View.VISIBLE
      bottomNav.visibility = View.VISIBLE
      navRail.visibility = View.GONE
      navDrawer.visibility = View.GONE
    } else if (screenWidth < LARGE_SCREEN_WIDTH_SIZE) {
      fab?.visibility = View.GONE
      bottomNav.visibility = View.GONE
      navRail.visibility = View.VISIBLE
      navDrawer.visibility = View.GONE
      navFab.shrink()
    } else {
      fab?.visibility = View.GONE
      bottomNav.visibility = View.GONE
      navRail.visibility = View.GONE
      navDrawer.visibility = View.VISIBLE
      navFab.extend()
    }
  }

  private fun setNavRailButtonOnClickListener(
    drawerLayout: DrawerLayout,
    navButton: View?,
    modalDrawer: NavigationView
  ) {
    navButton?.setOnClickListener {
      drawerLayout.openDrawer(modalDrawer)
    }
  }

  private fun setModalDrawerButtonOnClickListener(
    drawerLayout: DrawerLayout,
    button: View?,
    modalDrawer: NavigationView
  ) {
    button?.setOnClickListener {
      drawerLayout.closeDrawer(modalDrawer)
    }
  }
}
