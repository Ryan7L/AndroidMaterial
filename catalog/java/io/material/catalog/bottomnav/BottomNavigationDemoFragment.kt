package io.material.catalog.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.math.MathUtils
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeDrawable.BadgeGravity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

abstract class BottomNavigationDemoFragment : DemoFragment() {
  private val MAX_BOTTOM_NAV_CHILDREN = 5
  private var badgeGravityValues = arrayOf(
    BadgeDrawable.TOP_END, BadgeDrawable.TOP_START
  )

  private var numVisibleChildren = 3

  protected var bottomNavigationViews: MutableList<BottomNavigationView>? = null

  override fun onCreateDemoView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_bottom_nav_fragment, container, false)
    initBottomNavs(inflater, view)
    initBottomNavDemoControls(view)
    val navigationItemListener =
      /**
       * 当选择导航菜单中的项目时调用。
       *
       * @param item 所选项目
       * @return true 表示将该项目显示为所选项目， false 表示不应选择该项目。考虑预先将不可选择的项目设置为禁用，以使它们显示为非交互式。
       */
      OnItemSelectedListener { item ->
        handleAllBottomNavSelections(item.itemId)
        val page1Text = view.findViewById<TextView>(R.id.page_1)
        val page2Text = view.findViewById<TextView>(R.id.page_2)
        val page3Text = view.findViewById<TextView>(R.id.page_3)
        val page4Text = view.findViewById<TextView>(R.id.page_4)
        val page5Text = view.findViewById<TextView>(R.id.page_5)
        val itemId = item.itemId
        page1Text.visibility = if (itemId == R.id.action_page_1) View.VISIBLE else View.GONE
        page2Text.visibility = if (itemId == R.id.action_page_2) View.VISIBLE else View.GONE
        page3Text.visibility = if (itemId == R.id.action_page_3) View.VISIBLE else View.GONE
        page4Text.visibility = if (itemId == R.id.action_page_4) View.VISIBLE else View.GONE
        page5Text.visibility = if (itemId == R.id.action_page_5) View.VISIBLE else View.GONE
        clearAndHideBadge(item.itemId)
        false
      }
    setBottomNavListeners(navigationItemListener)
    if (savedInstanceState == null) {
      setUpBadging()
    }
    return view
  }

  private fun setUpBadging() {
    bottomNavigationViews?.forEach {
      var menuItemId = it.menu.getItem(0).itemId
      // 将显示仅图标徽章。
      var badge = it.getOrCreateBadge(menuItemId)
      badge.isVisible = true

      menuItemId = it.menu.getItem(1).itemId
      // 将显示带有文本“99”的徽章。
      badge = it.getOrCreateBadge(menuItemId)
      badge.isVisible = true
      badge.number = 99

      menuItemId = it.menu.getItem(2).itemId
      // 将显示带有文字“999+”的徽章。
      badge = it.getOrCreateBadge(menuItemId)
      badge.isVisible = true
      badge.number = 9999

    }
  }

  private fun clearAndHideBadge(menuItemId: Int) {
    bottomNavigationViews?.forEach {
      val menuItem = it.menu.getItem(0)
      if (menuItem.itemId == menuItemId) {
        //隐藏而不是删除与第一个菜单项关联的标记，因为用户可以触发它再次显示。
        val badgeDrawable = it.getBadge(menuItemId)
        badgeDrawable?.isVisible = false
        badgeDrawable?.clearNumber()
      } else {
        //删除与此菜单项关联的徽章，因为无法再次显示
        it.removeBadge(menuItemId)
      }
    }
  }

  private fun handleAllBottomNavSelections(itemId: Int) {
    bottomNavigationViews?.forEach {
      handleBottomNavItemSelections(it, itemId)
    }
  }

  private fun handleBottomNavItemSelections(navView: BottomNavigationView, itemId: Int) {
    navView.menu.findItem(itemId).isChecked = true
  }

  protected open fun initBottomNavDemoControls(view: View) {
    initAddNavItemButton(view.findViewById(R.id.add_button))
    initRemoveNavItemButton(view.findViewById(R.id.remove_button))
    initAddIncreaseBadgeNumberButton(view.findViewById(R.id.increment_badge_number_button))
    val badgeGravitySpinner = view.findViewById<Spinner>(R.id.badge_gravity_spinner)
    val adapter = ArrayAdapter.createFromResource(
      badgeGravitySpinner.context,
      R.array.cat_bottom_nav_badge_gravity_titles,
      android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    badgeGravitySpinner.adapter = adapter
    badgeGravitySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
      /**
       *
       * 当此视图中的项目被选中时调用的回调方法。仅当新选择的位置与之前选择的位置不同或没有选择任何项目时，才会调用此回调。
       * 如果实施者需要访问与所选项目相关的数据，他们可以调用 getItemAtPosition(position)。
       *
       * @param parent 发生选择的 AdapterView
       * @param view AdapterView 中被单击的视图
       * @param position 视图在适配器中的位置
       * @param id 所选项目的行 ID
       */
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        updateBadgeGravity(
          badgeGravityValues[MathUtils.clamp(
            position, 0, badgeGravityValues.size - 1
          )]
        )

      }

      /**
       * 当选择从此视图中消失时调用的回调方法。选择可能会在触摸激活或适配器变空时消失。
       *
       * @param parent 现在不包含选定项目的 AdapterView。
       */
      override fun onNothingSelected(parent: AdapterView<*>?) {
      }

    }
    val switch = view.findViewById<MaterialSwitch>(R.id.bold_text_switch)
    switch?.let {
      it.isChecked = true
      it.setOnCheckedChangeListener { _, isChecked ->
        bottomNavigationViews?.forEach { navView ->
          navView.setItemTextAppearanceActiveBoldEnabled(isChecked)
        }
      }
    }
  }

  private fun initAddIncreaseBadgeNumberButton(incrementBadgeNumberBtn: Button) {
    incrementBadgeNumberBtn.setOnClickListener {
      updateBadgeNumber(1)
    }
  }

  private fun updateBadgeGravity(@BadgeGravity badgeGravity: Int) {
    bottomNavigationViews?.forEach {
      for (i in 0 until MAX_BOTTOM_NAV_CHILDREN) {
        // 更新所有菜单项上的徽章Gravity
        val item = it.menu.getItem(i)
        val menuId = item.itemId
        val badgeDrawable = it.getBadge(menuId)
        badgeDrawable?.badgeGravity = badgeGravity
      }
    }
  }

  private fun updateBadgeNumber(delta: Int) {
    bottomNavigationViews?.forEach {
      // 增加第一个菜单项上的徽章编号。
      val item = it.menu.getItem(0)
      val menuId = item.itemId
      val badgeDrawable = it.getOrCreateBadge(menuId)
      //如果已选择第一个菜单项并且隐藏了徽章，请调用 BadgeDrawable.setVisible() 以确保徽章可见。
      badgeDrawable.isVisible = true
      badgeDrawable.number += delta
    }
  }

  private fun initAddNavItemButton(addNavItemBtn: Button) {
    addNavItemBtn.setOnClickListener {
      if (numVisibleChildren < MAX_BOTTOM_NAV_CHILDREN) {
        addNavItemsToBottomNavs()
        numVisibleChildren++
      }
    }
  }

  private fun initRemoveNavItemButton(removeNavItemBtn: Button) {
    removeNavItemBtn.setOnClickListener {
      if (numVisibleChildren > 0) {
        numVisibleChildren--
        removeNavItemsFromBottomNavs()
      }
    }
  }

  private fun setBottomNavListeners(listener: NavigationBarView.OnItemSelectedListener) {
    bottomNavigationViews?.forEach {
      it.setOnItemSelectedListener(listener)
    }
  }

  private fun removeNavItemsFromBottomNavs() {
    adjustAllBottomNavItemsVisibilities(false)
  }

  private fun addNavItemsToBottomNavs() {
    adjustAllBottomNavItemsVisibilities(true)
  }

  private fun adjustAllBottomNavItemsVisibilities(visibility: Boolean) {
    bottomNavigationViews?.forEach {
      adjustBottomNavItemsVisibility(it, visibility)
    }
  }

  private fun adjustBottomNavItemsVisibility(bn: BottomNavigationView, visibility: Boolean) {
    bn.menu.getItem(numVisibleChildren).isVisible = visibility
  }

  private fun initBottomNavs(layoutInflater: LayoutInflater, view: View) {
    inflateBottomNavs(layoutInflater, view.findViewById(R.id.bottom_navs))
    inflateBottomNavDemoControls(layoutInflater, view.findViewById(R.id.demo_controls))
    addBottomNavsToList(view)
  }

  private fun inflateBottomNavDemoControls(layoutInflater: LayoutInflater, content: ViewGroup) {
    if (bottomNavDemoControlsLayout != 0) {
      content.addView(layoutInflater.inflate(bottomNavDemoControlsLayout, content, false))
    }
  }

  private fun inflateBottomNavs(layoutInflater: LayoutInflater, content: ViewGroup) {
    content.addView(layoutInflater.inflate(bottomNavsContent, content, false))
  }

  private fun addBottomNavsToList(view: View) {
    bottomNavigationViews =
      DemoUtils.findViewsWithType(view, BottomNavigationView::class.java).toMutableList()
  }

  @get:LayoutRes
  protected open val bottomNavsContent: Int
    get() = R.layout.cat_bottom_nav

  @get:LayoutRes
  protected open val bottomNavDemoControlsLayout: Int
    get() = 0
}
