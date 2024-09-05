package io.material.catalog.adaptive

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ReactiveGuide
import androidx.core.util.Consumer
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity
import java.util.concurrent.Executor

class AdaptiveListViewDemoActivity: DemoActivity() {
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var modalNavDrawer: NavigationView
  private lateinit var detailViewContainer: View
  private lateinit var guideline: ReactiveGuide
  private lateinit var bottomNav: BottomNavigationView
  private lateinit var fab: FloatingActionButton
  private lateinit var navRail: NavigationRailView
  private lateinit var navDrawer: NavigationView
  private lateinit var navFab: ExtendedFloatingActionButton

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

  private lateinit var constraintLayout: ConstraintLayout
  private lateinit var configuration: Configuration
  private lateinit var fragmentManager: FragmentManager
  private lateinit var listViewFragment: AdaptiveListViewDemoFragment
  private lateinit var detailViewFragment: AdaptiveListViewDetailDemoFragment
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_adaptive_list_view_activity, viewGroup, false)
    windowInfoTracker = WindowInfoTrackerCallbackAdapter(WindowInfoTracker.getOrCreate(this))
    drawerLayout = view.findViewById(R.id.drawer_layout)
    constraintLayout = view.findViewById(R.id.list_view_activity_constraint_layout)
    modalNavDrawer = view.findViewById(R.id.modal_nav_drawer)
    detailViewContainer = view.findViewById(R.id.list_view_detail_fragment_container)
    guideline = view.findViewById(R.id.guideline)
    bottomNav = view.findViewById(R.id.bottom_nav)
    fab = view.findViewById(R.id.fab)
    navRail = view.findViewById(R.id.nav_rail)
    navDrawer = view.findViewById(R.id.nav_drawer)
    navFab = view.findViewById(R.id.nav_fab)
    return view
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    configuration = resources.configuration
    fragmentManager = supportFragmentManager
    listViewFragment = AdaptiveListViewDemoFragment()
    detailViewFragment = AdaptiveListViewDetailDemoFragment()
    //根据屏幕宽度尺寸更新导航视图.
    val screenWidth = configuration.screenWidthDp
    AdaptiveUtils.updateNavigationViewLayout(
      screenWidth, drawerLayout, modalNavDrawer, fab, bottomNav, navRail, navDrawer, navFab
    )
    //清除后退堆栈以防止按下后退按钮时发生意外行为
    val backStackEntryCount = fragmentManager.backStackEntryCount
    for (entry in 0 until backStackEntryCount) {
      fragmentManager.popBackStack()
    }
  }

  override fun onStart() {
    super.onStart()
    windowInfoTracker?.addWindowLayoutInfoListener(this,executor,stateContainer)
  }

  override fun onStop() {
    super.onStop()
    windowInfoTracker?.removeWindowLayoutInfoListener(stateContainer)
  }
  private fun updatePortraitLayout(){
    val listViewFragmentId = R.id.list_view_fragment_container
    guideline.setGuidelineEnd(0)
    detailViewContainer.visibility = View.GONE
    listViewFragment.detailViewContainerId = listViewFragmentId
    fragmentManager.beginTransaction().replace(listViewFragmentId, listViewFragment).commit()
  }
  private fun updateLandscapeLayout(guidelinePosition: Int, foldWidth: Int){
    val listViewFragmentId = R.id.list_view_fragment_container
    val detailViewFragmentId = R.id.list_view_detail_fragment_container
    ConstraintSet().apply {
      clone(constraintLayout)
      setMargin(detailViewFragmentId, ConstraintSet.START, foldWidth)
      applyTo(constraintLayout)
    }
    guideline.setGuidelineEnd(guidelinePosition)
    listViewFragment.detailViewContainerId = detailViewFragmentId
    fragmentManager.beginTransaction().replace(listViewFragmentId,listViewFragment)
      .replace(detailViewFragmentId,detailViewFragment)
      .commit()
  }
  inner class StateContainer: Consumer<WindowLayoutInfo>{
    override fun accept(value: WindowLayoutInfo) {
      val displayFeatures = value.displayFeatures
      var hasVerticalFold = false
      if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
        updatePortraitLayout()
      }else{
        displayFeatures.forEach {
          if (it is FoldingFeature){
            if (it.orientation == FoldingFeature.Orientation.VERTICAL){
              val foldPosition = it.bounds.left
              val foldWidth = it.bounds.right - foldPosition
              updateLandscapeLayout(foldPosition, foldWidth)
              hasVerticalFold = true
            }
          }
        }
        if (!hasVerticalFold){
          updateLandscapeLayout(constraintLayout.width / 2, 0)
        }
      }
    }
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
