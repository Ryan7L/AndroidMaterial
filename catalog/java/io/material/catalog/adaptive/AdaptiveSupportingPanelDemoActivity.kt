package io.material.catalog.adaptive

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.drawerlayout.widget.DrawerLayout
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity
import java.util.concurrent.Executor

class AdaptiveSupportingPanelDemoActivity : DemoActivity() {
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var modalNavDrawer: NavigationView
  private lateinit var bottomNav: BottomNavigationView
  private lateinit var navRail: NavigationRailView
  private lateinit var navDrawer: NavigationView
  private lateinit var navFab: ExtendedFloatingActionButton

  private var demoFragment: AdaptiveSupportingPanelDemoFragment? = null
  private var windowInfoTracker: WindowInfoTrackerCallbackAdapter? = null
  private val stateContainer: Consumer<WindowLayoutInfo> = StateContainer()
  private val handler = Handler(Looper.getMainLooper())
  private val executor =
    Executor { command: Runnable? ->
      handler.post {
        handler.post(
          command!!
        )
      }
    }
  private lateinit var configuration: Configuration
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_adaptive_supporting_panel_activity, viewGroup, false)
    windowInfoTracker = WindowInfoTrackerCallbackAdapter(WindowInfoTracker.getOrCreate(this))
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
    configuration = resources.configuration
    demoFragment = AdaptiveSupportingPanelDemoFragment()
    AdaptiveUtils.updateNavigationViewLayout(
      configuration.screenWidthDp,
      drawerLayout,
      modalNavDrawer,
      null,
      bottomNav,
      navRail, navDrawer, navFab
    )
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, demoFragment!!)
      .commit()
  }

  override fun onStart() {
    super.onStart()
    windowInfoTracker?.addWindowLayoutInfoListener(this, executor, stateContainer)
  }

  override fun onStop() {
    super.onStop()
    windowInfoTracker?.removeWindowLayoutInfoListener(stateContainer)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

  private inner class StateContainer : Consumer<WindowLayoutInfo> {
    override fun accept(value: WindowLayoutInfo) {
      demoFragment ?: return
      val displayFeatures = value.displayFeatures
      var isTableTop = false
      displayFeatures.forEach {
        if (it is FoldingFeature) {
          if (it.state == FoldingFeature.State.HALF_OPENED && it.orientation == FoldingFeature.Orientation.HORIZONTAL) {
            val foldPosition = it.bounds.top
            val foldWidth = it.bounds.bottom - foldPosition
            demoFragment!!.updateTableTopLayout(foldPosition, foldWidth)
            isTableTop = true
          }
        }
      }
      if (!isTableTop) {
        if (configuration!!.orientation == Configuration.ORIENTATION_PORTRAIT) {
          demoFragment!!.updatePortraitLayout()
        } else {
          demoFragment!!.updateLandscapeLayout()
        }
      }
    }

  }
}
