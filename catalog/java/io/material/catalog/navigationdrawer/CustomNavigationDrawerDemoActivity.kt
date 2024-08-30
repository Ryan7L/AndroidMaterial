package io.material.catalog.navigationdrawer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.window.BackEvent
import android.window.OnBackAnimationCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.google.android.material.motion.MaterialSideContainerBackHelper
import com.google.android.material.navigation.DrawerLayoutUtils
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class CustomNavigationDrawerDemoActivity : DemoActivity() {
  private var drawerLayout: DrawerLayout? = null
  private var currentDrawerView: View? = null
  private val drawerOnBackPressedCallback = object : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
      drawerLayout?.closeDrawers()
    }
  }
  private var drawerOnBackAnimationCallback: OnBackAnimationCallback? = null
  private var sideContainerBackHelper: MaterialSideContainerBackHelper? = null
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_navigationdrawer_custom, viewGroup, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    drawerLayout = view.findViewById(R.id.drawer)
    drawerLayout?.addDrawerListener(
      ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.cat_navigationdrawer_button_show_content_description,
        R.string.cat_navigationdrawer_button_hide_content_description
      )
    )
    drawerLayout?.addDrawerListener(object : SimpleDrawerListener() {
      override fun onDrawerOpened(drawerView: View) {
        registerBackCallback(view)
      }

      override fun onDrawerClosed(drawerView: View) {
        unregisterBackCallback()
      }
    })
    val endDrawer = view.findViewById<View>(R.id.custom_drawer_end)
    view.findViewById<View>(R.id.show_end_drawer_gravity)
      .setOnClickListener { drawerLayout?.openDrawer(endDrawer) }
    drawerLayout?.post {
      val startDrawer = view.findViewById<View>(R.id.custom_drawer_start)
      if (drawerLayout!!.isDrawerOpen(startDrawer)) {
        registerBackCallback(startDrawer)
      } else if (drawerLayout!!.isDrawerOpen(endDrawer)) {
        registerBackCallback(endDrawer)
      }
    }
  }

  private fun registerBackCallback(drawerView: View) {
    currentDrawerView = drawerView
    sideContainerBackHelper = MaterialSideContainerBackHelper(drawerView)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      if (drawerOnBackAnimationCallback == null) {
        drawerOnBackAnimationCallback = onBackAnimationCallback
      }
      drawerLayout?.post {
        onBackInvokedDispatcher.registerOnBackInvokedCallback(
          OnBackInvokedDispatcher.PRIORITY_DEFAULT,
          drawerOnBackAnimationCallback!!
        )
      }
    } else {
      onBackPressedDispatcher.addCallback(this, drawerOnBackPressedCallback)
    }
  }

  private fun unregisterBackCallback() {
    currentDrawerView = null
    sideContainerBackHelper = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
      drawerOnBackAnimationCallback?.let {
        onBackInvokedDispatcher.unregisterOnBackInvokedCallback(
          it
        )
      }
      drawerOnBackAnimationCallback = null
    } else {
      drawerOnBackPressedCallback.remove()
    }
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

  @get:RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
  private val onBackAnimationCallback: OnBackAnimationCallback
    get() = object : OnBackAnimationCallback {
      override fun onBackStarted(backEvent: BackEvent) {
        sideContainerBackHelper?.startBackProgress(BackEventCompat(backEvent))
      }

      override fun onBackProgressed(backEvent: BackEvent) {
        val drawerLayoutParams = currentDrawerView?.layoutParams as DrawerLayout.LayoutParams
        sideContainerBackHelper?.updateBackProgress(
          BackEventCompat(backEvent),
          drawerLayoutParams.gravity
        )
      }

      override fun onBackInvoked() {
        drawerLayout ?: return
        currentDrawerView ?: return
        val backEvent = sideContainerBackHelper?.onHandleBackInvoked()
        if (backEvent == null) {
          drawerLayout!!.closeDrawers()
          return
        }
        val drawerLayoutParams = currentDrawerView!!.layoutParams as DrawerLayout.LayoutParams
        val gravity = drawerLayoutParams.gravity
        val scrimCloseAnimatorListener =
          DrawerLayoutUtils.getScrimCloseAnimatorListener(drawerLayout!!, currentDrawerView!!)
        val scrimCloseAnimatorUpdateListener =
          DrawerLayoutUtils.getScrimCloseAnimatorUpdateListener(drawerLayout!!)
        sideContainerBackHelper?.finishBackProgress(
          backEvent,
          gravity,
          scrimCloseAnimatorListener,
          scrimCloseAnimatorUpdateListener
        )
      }

      override fun onBackCancelled() {
        sideContainerBackHelper?.cancelBackProgress()
      }
    }
}
