package io.material.catalog.navigationdrawer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.navigation.NavigationView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class NavigationDrawerDemoActivity : DemoActivity() {

  private val drawerOnBackPressedCallback = object : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
      drawerLayout.closeDrawers()
    }
  }
  private lateinit var drawerLayout: DrawerLayout
  private lateinit var autoCloseSwitch: MaterialSwitch

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_navigationdrawer, viewGroup, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    onBackPressedDispatcher.addCallback(this, drawerOnBackPressedCallback)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    setSupportActionBar(toolbar)
    drawerLayout = view.findViewById(R.id.drawer)
    drawerLayout.addDrawerListener(
      ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.cat_navigationdrawer_button_show_content_description,
        R.string.cat_navigationdrawer_button_hide_content_description
      )
    )
    drawerLayout.addDrawerListener(object : SimpleDrawerListener() {
      override fun onDrawerOpened(drawerView: View) {
        drawerOnBackPressedCallback.isEnabled = true
      }

      override fun onDrawerClosed(drawerView: View) {
        drawerOnBackPressedCallback.isEnabled = false
      }
    })
    val navigationViewStart = view.findViewById<NavigationView>(R.id.navigation_view_start)
    initNavigationView(navigationViewStart)
    val navigationViewEnd = view.findViewById<NavigationView>(R.id.navigation_view_end)
    initNavigationView(navigationViewEnd)
    view.findViewById<Button>(R.id.show_end_drawer_gravity).setOnClickListener {
      drawerLayout.openDrawer(navigationViewEnd)
    }
    val boldTextSwitch = view.findViewById<MaterialSwitch>(R.id.bold_text_switch)
    boldTextSwitch.setOnCheckedChangeListener { _, isChecked ->
      navigationViewStart.setItemTextAppearanceActiveBoldEnabled(isChecked)
      navigationViewEnd.setItemTextAppearanceActiveBoldEnabled(isChecked)
    }
    autoCloseSwitch = view.findViewById(R.id.auto_close_switch)
    drawerLayout.post {
      if (drawerLayout.isDrawerOpen(GravityCompat.START) || drawerLayout.isDrawerOpen(GravityCompat.END)) {
        drawerOnBackPressedCallback.isEnabled = true
      }
    }
  }

  private fun initNavigationView(navigationView: NavigationView) {
    navigationView.apply {
      setCheckedItem(R.id.search_item)
      setNavigationItemSelectedListener {
        setCheckedItem(it)
        if (autoCloseSwitch.isChecked) {
          drawerLayout.closeDrawer(navigationView)
        }
        true
      }
    }
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
