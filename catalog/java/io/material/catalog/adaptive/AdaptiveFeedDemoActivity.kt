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

class AdaptiveFeedDemoActivity : DemoActivity() {
  private lateinit var container: View
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var modalNavDrawer: NavigationView
  private lateinit var bottomNav: BottomNavigationView
  private lateinit var navRail: NavigationRailView
  private lateinit var navDrawer: NavigationView
  private lateinit var navFab: ExtendedFloatingActionButton
  private var feedFragment: AdaptiveFeedDemoFragment? = null

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
    val view = layoutInflater.inflate(R.layout.cat_adaptive_feed_activity, viewGroup, false)
    container = view.findViewById(R.id.feed_activity_container)
    drawerLayout = view.findViewById(R.id.drawer_layout)
    modalNavDrawer = view.findViewById(R.id.modal_nav_drawer)
    windowInfoTracker = WindowInfoTrackerCallbackAdapter(WindowInfoTracker.getOrCreate(this))
    configuration = getResources().configuration
    bottomNav = view.findViewById(R.id.bottom_nav)
    navRail = view.findViewById(R.id.nav_rail)
    navDrawer = view.findViewById(R.id.nav_drawer)
    navFab = view.findViewById(R.id.nav_fab)
    return view
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    feedFragment = AdaptiveFeedDemoFragment()
    AdaptiveUtils.updateNavigationViewLayout(
      configuration.screenWidthDp,
      drawerLayout,
      modalNavDrawer,
      null,
      bottomNav,
      navRail,
      navDrawer,
      navFab
    )
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, feedFragment!!)
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

  inner class StateContainer : Consumer<WindowLayoutInfo> {
    override fun accept(value: WindowLayoutInfo) {
      feedFragment ?: return
      if (configuration.screenWidthDp < AdaptiveUtils.MEDIUM_SCREEN_WIDTH_SIZE) {
        feedFragment!!.setClosedLayout()
      } else {
        val displayFeatures = value.displayFeatures
        var isClosed = true
        displayFeatures.forEach {
          if (it is FoldingFeature) {
            if (it.state == FoldingFeature.State.FLAT || it.state == FoldingFeature.State.HALF_OPENED) {
              if (it.orientation == FoldingFeature.Orientation.VERTICAL) {
                val foldPosition = it.bounds.left
                val foldWidth = it.bounds.right - foldPosition
                feedFragment!!.setOpenLayout(foldPosition, foldWidth)
              } else {
                feedFragment!!.setOpenLayout(container.width / 2, 0)
              }
              isClosed = false
            }
          }
        }
        if (isClosed) {
          if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            feedFragment!!.setClosedLayout()
          } else {
            feedFragment!!.setOpenLayout(container.width / 2, 0)
          }
        }
      }
    }
  }
}
