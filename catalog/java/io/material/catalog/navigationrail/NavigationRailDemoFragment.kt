package io.material.catalog.navigationrail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.core.math.MathUtils
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.android.material.navigationrail.NavigationRailView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

open class NavigationRailDemoFragment : DemoFragment() {
  private val MAX_NAVIGATION_RAIL_CHILDREN = 7

  private val badgeGravityValues = intArrayOf(
    BadgeDrawable.TOP_END,
    BadgeDrawable.TOP_START
  )
  private var numVisibleChildren = 3
  open var navigationRailView: NavigationRailView? = null
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_navigation_rail_fragment, container, false)
    initViews(view, savedInstanceState)
    return view
  }

  private fun initViews(view: View, savedInstanceState: Bundle?) {
    initNavigationRail(requireContext(), view)
    initNavigationRailDemoControls(view)
    val onItemSelectedListener =
      OnItemSelectedListener { item ->
        handleNavigationRailItemsSelections(item.itemId)
        val tvIds = intArrayOf(
          R.id.page_1,
          R.id.page_2,
          R.id.page_3,
          R.id.page_4,
          R.id.page_5,
          R.id.page_6,
          R.id.page_7
        )
        for (id in tvIds) {
          view.findViewById<View>(id).visibility =
            if (item.itemId == id) View.VISIBLE else View.GONE
        }
        clearAndHideBadge(item.itemId)
        false
      }
    setNavigationRailListeners(onItemSelectedListener)
    if (savedInstanceState == null) {
      setUpBadging()
    }

  }

  private fun setUpBadging() {
    val badgeNum = intArrayOf(0, 99, 999)
    for (i in 0..2) {
      val itemId = navigationRailView?.menu?.getItem(i)?.itemId ?: continue
      val badgeDrawable = navigationRailView?.getOrCreateBadge(itemId)
      badgeDrawable?.isVisible = true
      if (i != 0) {
        badgeDrawable?.number = badgeNum[i]
      }
    }
  }

  private fun clearAndHideBadge(menuItemId: Int) {
    val item = navigationRailView?.menu?.getItem(0) ?: return
    if (item.itemId == menuItemId) {
      //隐藏而不是删除与第一个菜单项关联的标记，因为用户可以触发它再次显示。
      val badgeDrawable = navigationRailView?.getBadge(menuItemId)
      badgeDrawable?.setVisible(false)
      badgeDrawable?.clearNumber()
    } else {
      //删除与此菜单项关联的徽章，因为无法再次显示。
      navigationRailView?.removeBadge(menuItemId)
    }
  }

  private fun handleNavigationRailItemsSelections(itemId: Int) {
    navigationRailView?.let { handleNavigationRailItemsSelections(it, itemId) }
  }

  private fun handleNavigationRailItemsSelections(view: NavigationRailView, itemId: Int) {
    view.menu.findItem(itemId).isChecked = true
  }

  private fun updateBadgeGravity(badgeGravity: Int) {
    val size = navigationRailView?.menu?.size() ?: return
    for (i in 0 until size) {
      val item = navigationRailView?.menu?.getItem(i) ?: continue
      val itemId = item.itemId
      val badgeDrawable = navigationRailView?.getBadge(itemId)
      badgeDrawable?.badgeGravity = badgeGravity
    }
  }

  protected open fun initNavigationRailDemoControls(view: View) {
    initAddNavItemsButton(view.findViewById(R.id.add_button))
    initRemoveNavItemButton(view.findViewById(R.id.remove_button))
    initAddIncreaseBadgeNumberButton(view.findViewById(R.id.increment_badge_number_button))
    val spinner = view.findViewById<Spinner>(R.id.badge_gravity_spinner)
    val adapter = ArrayAdapter.createFromResource(
      spinner.context,
      R.array.cat_navigation_rail_badge_gravity_titles,
      android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    spinner.adapter = adapter

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateBadgeGravity(
          badgeGravityValues[MathUtils.clamp(
            position,
            0,
            badgeGravityValues.size - 1
          )]
        )
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {

      }
    }
    val switch = view.findViewById<MaterialSwitch>(R.id.bold_text_switch)
    switch.isChecked = true
    switch.setOnCheckedChangeListener { buttonView, isChecked ->
      navigationRailView?.setItemTextAppearanceActiveBoldEnabled(isChecked)
    }
  }

  private fun initAddIncreaseBadgeNumberButton(btn: Button) {
    btn.setOnClickListener {
      updateBadgeNumber(1)
    }
  }

  private fun updateBadgeNumber(delta: Int) {
    val item = navigationRailView?.menu?.getItem(0) ?: return
    val itemId = item.itemId
    val badgeDrawable = navigationRailView?.getOrCreateBadge(itemId)
    badgeDrawable?.setVisible(true)
    badgeDrawable?.setNumber(badgeDrawable.number + delta)
  }

  private fun initAddNavItemsButton(btn: Button) {
    btn.setOnClickListener {
      if (numVisibleChildren < MAX_NAVIGATION_RAIL_CHILDREN) {
        addNavItemsToNavigationRails()
        numVisibleChildren++
      }
    }
  }

  private fun initRemoveNavItemButton(btn: Button) {
    btn.setOnClickListener {
      if (numVisibleChildren > 0) {
        numVisibleChildren--
        removeNavItemsFromNavigationRails()
      }
    }
  }

  private fun setNavigationRailListeners(listener: OnItemSelectedListener) {
    navigationRailView?.setOnItemSelectedListener(listener)
  }

  private fun removeNavItemsFromNavigationRails() {
    navigationRailView?.menu?.getItem(numVisibleChildren)?.setVisible(false)
  }

  private fun addNavItemsToNavigationRails() {
    navigationRailView?.menu?.getItem(numVisibleChildren)?.setVisible(true)
  }

  private fun initNavigationRail(context: Context, view: View) {
    navigationRailView = view.findViewById(R.id.cat_navigation_rail)
    if (navigationRailDemoControlsLayout != 0) {
      val controlsView = view.findViewById<ViewGroup>(R.id.demo_controls)
      View.inflate(context, navigationRailDemoControlsLayout, controlsView)
    }
  }

  @get:LayoutRes
  protected open val navigationRailDemoControlsLayout: Int
    get() = 0
}
