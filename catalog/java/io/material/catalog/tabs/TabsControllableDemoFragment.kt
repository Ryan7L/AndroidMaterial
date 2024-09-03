package io.material.catalog.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class TabsControllableDemoFragment : DemoFragment() {
  private val TAB_COUNT = 3
  private val ICON_DRAWABLE_RES = R.drawable.ic_tabs_24px
  private val LABEL_STRING_RES = R.string.cat_tab_item_label
  private var showIcons = true
  private var showLabels = true
  private lateinit var tabLayouts: List<TabLayout>
  private lateinit var pager: ViewPager

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_tabs_controllable_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val content = view.findViewById<ViewGroup>(R.id.content)
    val tabsContent = layoutInflater.inflate(R.layout.cat_tabs_controllable_content, content, false)
    content.addView(tabsContent, 0)
    tabLayouts = DemoUtils.findViewsWithType(view, TabLayout::class.java)
    pager = view.findViewById(R.id.viewpager)
    val coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.coordinator_layout)
    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insetsCompat ->
      val scrollable = coordinatorLayout.findViewById<View>(R.id.cat_tabs_controllable_scrollview)
      scrollable.setPadding(
        scrollable.paddingLeft,
        0,
        scrollable.paddingRight,
        scrollable.paddingBottom
      )
      insetsCompat
    }
    setUpViewPager()
    setAllTabLayoutIcons(ICON_DRAWABLE_RES)
    setAllTabLayoutText(LABEL_STRING_RES)
    setAllTabLayoutBadges()

    val iconsToggle = view.findViewById<SwitchCompat>(R.id.toggle_icons_switch)
    iconsToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      showIcons = isChecked
      setAllTabLayoutIcons(ICON_DRAWABLE_RES)
    }
    val labelToggle = view.findViewById<SwitchCompat>(R.id.toggle_labels_switch)
    labelToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      showLabels = isChecked
      if (isChecked){
        tabLayouts.forEach {
          setLabelVisibility(it,TabLayout.TAB_LABEL_VISIBILITY_LABELED)
        }
      }else{
        tabLayouts.forEach {
          setLabelVisibility(it,TabLayout.TAB_LABEL_VISIBILITY_UNLABELED)
        }
      }
    }
    val tabGravityFillButton = view.findViewById<RadioButton>(R.id.tabs_gravity_fill_button)
    tabGravityFillButton.setOnClickListener {
      setAllTabLayoutGravity(TabLayout.GRAVITY_FILL)
    }
    val tabGravityCenterButton = view.findViewById<RadioButton>(R.id.tabs_gravity_center_button)
    tabGravityCenterButton.setOnClickListener {
      setAllTabLayoutGravity(TabLayout.GRAVITY_CENTER)
    }
    val tabAnimationModeLinearButton = view.findViewById<RadioButton>(R.id.tabs_animation_mode_linear_button)
    tabAnimationModeLinearButton.setOnClickListener {
      setAllTabAnimationModes(TabLayout.INDICATOR_ANIMATION_MODE_LINEAR)
    }
    val tabsAnimationModeElasticButton = view.findViewById<RadioButton>(R.id.tabs_animation_mode_elastic_button)
    tabsAnimationModeElasticButton.setOnClickListener {
      setAllTabAnimationModes(TabLayout.INDICATOR_ANIMATION_MODE_ELASTIC)
    }
    val tabsAnimationModeFadeButton = view.findViewById<RadioButton>(R.id.tabs_animation_mode_fade_button)
    tabsAnimationModeFadeButton.setOnClickListener {
      setAllTabAnimationModes(TabLayout.INDICATOR_ANIMATION_MODE_FADE)
    }
    val inlineToggle = view.findViewById<SwitchCompat>(R.id.toggle_inline_switch)
    inlineToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      setAllTabLayoutInline(isChecked)
    }
    val fullWidthToggle = view.findViewById<SwitchCompat>(R.id.toggle_full_width_switch)
    fullWidthToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      setAllTabLayoutFullWidthIndicators(isChecked)
    }
    val selectedIndicatorSpinner = view.findViewById<Spinner>(R.id.selector_spinner)

    val adapter = ArrayAdapter.createFromResource(
      selectedIndicatorSpinner.context,
      R.array.cat_tabs_selected_indicator_drawable_titles,
      android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    selectedIndicatorSpinner.adapter = adapter
    selectedIndicatorSpinner.onItemSelectedListener = object : OnItemSelectedListener{
      override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        setAllTabLayoutSelectedIndicators(position)
      }

      override fun onNothingSelected(parent: AdapterView<*>?) {
        setAllTabLayoutSelectedIndicators(0)
      }
    }

  }

  private fun setUpViewPager() {
    pager.adapter = TabsPagerAdapter(childFragmentManager, requireContext(), TAB_COUNT)
    tabLayouts.forEach {
      it.setupWithViewPager(pager)
    }
  }

  private fun setAllTabLayoutIcons(iconResId: Int) {
    tabLayouts.forEach {
      setTabLayoutIcons(it, iconResId)
    }
  }

  private fun setTabLayoutIcons(tabLayout: TabLayout, iconResId: Int) {
    for (i in 0 until tabLayout.tabCount) {
      if (showIcons) {
        tabLayout.getTabAt(i)?.setIcon(iconResId)
      } else {
        tabLayout.getTabAt(i)?.setIcon(null)
      }
    }
  }

  private fun setAllTabLayoutText(stringResId: Int) {
    tabLayouts.forEach {
      setTabLayoutText(it, stringResId)
    }
  }

  private fun setTabLayoutText(tabLayout: TabLayout, stringResId: Int) {
    for (i in 0 until tabLayout.tabCount) {
      tabLayout.getTabAt(i)?.text = resources.getString(stringResId, i + 1)
    }
  }

  private fun setAllTabLayoutBadges() {
    tabLayouts.forEach {
      setBadging(it)
      it.addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
          tab?.removeBadge()
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
          tab?.removeBadge()
        }
      })
    }
  }

  private fun setBadging(tabLayout: TabLayout) {
    val badgeNumbers = intArrayOf(1, 88, 99)
    for (i in badgeNumbers.indices) {
      val badge = tabLayout.getTabAt(i)?.orCreateBadge
      badge?.isVisible = true
      badge?.number = badgeNumbers[i]
    }
  }

  private fun setLabelVisibility(tabLayout: TabLayout, mode: Int) {
    for (i in 0 until tabLayout.tabCount) {
      tabLayout.getTabAt(i)?.tabLabelVisibility = mode
    }
  }

  private fun setAllTabLayoutGravity(gravity: Int) {
    tabLayouts.forEach {
      it.tabGravity = gravity
    }
  }

  private fun setAllTabAnimationModes(mode: Int) {
    tabLayouts.forEach {
      it.tabIndicatorAnimationMode = mode
    }
  }

  private fun setAllTabLayoutInline(inline: Boolean) {
    tabLayouts.forEach {
      it.isInlineLabel = inline
    }
  }

  private fun setAllTabLayoutFullWidthIndicators(fullWidth: Boolean) {
    tabLayouts.forEach {
      it.setTabIndicatorFullWidth(fullWidth)
    }
  }

  private fun setAllTabLayoutSelectedIndicators(position: Int) {
    val drawables = resources.obtainTypedArray(R.array.cat_tabs_selected_indicator_drawables)
    val drawableResId = drawables.getResourceId(position, 0)
    drawables.recycle()

    val drawableGravities =
      resources.obtainTypedArray(R.array.cat_tabs_selected_indicator_drawable_gravities)
    val drawableGravity = drawableGravities.getInt(position, 0)
    drawableGravities.recycle()
    tabLayouts.forEach {
      it.setSelectedTabIndicator(drawableResId)
      it.setSelectedTabIndicatorGravity(drawableGravity)
    }
  }

}
