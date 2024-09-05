package io.material.catalog.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class AdaptiveHeroDemoActivity : DemoActivity() {
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var modalNavDrawer: NavigationView
  private lateinit var bottomNav: BottomNavigationView
  private lateinit var navRail: NavigationRailView
  private lateinit var navDrawer: NavigationView
  private lateinit var navFab: ExtendedFloatingActionButton
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_adaptive_hero_activity, viewGroup, false)
    drawerLayout = view.findViewById(R.id.drawer_layout)
    modalNavDrawer = view.findViewById(R.id.modal_nav_drawer)
    bottomNav = view.findViewById(R.id.bottom_nav)
    navRail = view.findViewById(R.id.nav_rail)
    navDrawer = view.findViewById(R.id.nav_drawer)
    navFab = view.findViewById(R.id.nav_fab)
    return view
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val configuration = resources.configuration
    AdaptiveUtils.updateNavigationViewLayout(
      configuration.screenWidthDp,
      drawerLayout,
      modalNavDrawer,
      null,
      bottomNav,
      navRail,
      navDrawer, navFab
    )
    supportFragmentManager
      .beginTransaction()
      .replace(R.id.fragment_container, AdaptiveHeroDemoFragment())
      .commit()
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
