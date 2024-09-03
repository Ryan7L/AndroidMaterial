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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class TabsViewPagerDemoFragment : DemoFragment() {
  private val ICON_DRAWABLE_RES = R.drawable.ic_tabs_24px;
  private var showIcons = true
  private lateinit var tabLayouts: List<TabLayout>
  private lateinit var pager: ViewPager2

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
   val view = inflater.inflate(R.layout.cat_tabs_viewpager_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val content = view.findViewById<ViewGroup>(R.id.content)
    val tabsContent = layoutInflater.inflate(R.layout.cat_tabs_viewpager_content, content, false)
    content.addView(tabsContent, 0)
    tabLayouts = DemoUtils.findViewsWithType(view, TabLayout::class.java)
    pager = view.findViewById(R.id.viewpager)
    val coordinatorLayout = view.findViewById<CoordinatorLayout>(R.id.coordinator_layout)
    view.setOnApplyWindowInsetsListener { v, insets ->
      setScrollablePadding(coordinatorLayout)
      insets
    }
    setUpViewPager()
    setAllTabLayoutIcons(ICON_DRAWABLE_RES)
    val iconToggle = view.findViewById<SwitchCompat>(R.id.toggle_icons_switch)
    iconToggle.setOnCheckedChangeListener { _, isChecked ->
      showIcons = isChecked
      setAllTabLayoutIcons(ICON_DRAWABLE_RES)
    }
    val labelToggle = view.findViewById<SwitchCompat>(R.id.toggle_labels_switch)
    labelToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      if (isChecked){
        tabLayouts.forEach {
          setLabelVisibility(it, TabLayout.TAB_LABEL_VISIBILITY_LABELED)
        }
      }else{
        tabLayouts.forEach {
          setLabelVisibility(it, TabLayout.TAB_LABEL_VISIBILITY_UNLABELED)
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
      setAllTabAnimationMode(TabLayout.INDICATOR_ANIMATION_MODE_LINEAR)
    }
    val tabAnimationModeElasticButton = view.findViewById<RadioButton>(R.id.tabs_animation_mode_elastic_button)
    tabAnimationModeElasticButton.setOnClickListener {
      setAllTabAnimationMode(TabLayout.INDICATOR_ANIMATION_MODE_ELASTIC)
    }
    val tabAnimationModeFadeButton = view.findViewById<RadioButton>(R.id.tabs_animation_mode_fade_button)
    tabAnimationModeFadeButton.setOnClickListener {
      setAllTabAnimationMode(TabLayout.INDICATOR_ANIMATION_MODE_FADE)
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
    val adapter = ArrayAdapter.createFromResource(selectedIndicatorSpinner.context,
      R.array.cat_tabs_selected_indicator_drawable_titles,
      android.R.layout.simple_spinner_item)
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
  private fun setScrollablePadding(coordinatorLayout: CoordinatorLayout) {
    val scrollable = coordinatorLayout.findViewById<View>(R.id.cat_tabs_controllable_scrollview)
    scrollable.setPadding(
      scrollable.paddingLeft,
      0,
      scrollable.paddingRight,
      scrollable.paddingBottom
    )
  }

  private fun setUpViewPager() {
    val adapter = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
      addFragment(TabItemContentFragment.newInstance(1))
      addFragment(TabItemContentFragment.newInstance(2))
      addFragment(TabItemContentFragment.newInstance(3))
    }
    pager.adapter = adapter
    for (tabLayout in tabLayouts) {
      TabLayoutMediator(tabLayout, pager) { tab, position ->
        tab.text = "Tab ${position + 1}"
      }.attach()
    }
  }

  private fun setAllTabLayoutIcons(iconResId: Int) {
    for (tabLayout in tabLayouts) {
      setTabLayoutIcons(tabLayout, iconResId)
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

  private fun setLabelVisibility(tabLayout: TabLayout, mode: Int) {
    for (i in 0 until tabLayout.tabCount) {
      tabLayout.getTabAt(i)?.tabLabelVisibility = mode
    }
  }

  private fun setAllTabLayoutGravity(gravity: Int) {
    for (tabLayout in tabLayouts) {
      tabLayout.tabGravity = gravity
    }
  }

  private fun setAllTabAnimationMode(mode: Int) {
    for (tabLayout in tabLayouts) {
      tabLayout.tabIndicatorAnimationMode = mode
    }
  }

  private fun setAllTabLayoutInline(inline: Boolean) {
    for (tabLayout in tabLayouts) {
      tabLayout.isInlineLabel = inline
    }
  }

  private fun setAllTabLayoutFullWidthIndicators(fullWidth: Boolean) {
    for (tabLayout in tabLayouts) {
      tabLayout.setTabIndicatorFullWidth(fullWidth)
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
    for (tabLayout in tabLayouts) {
      tabLayout.setSelectedTabIndicator(drawableResId)
      tabLayout.setSelectedTabIndicatorGravity(drawableGravity)
    }
  }

  class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    private val arrayList = arrayListOf<Fragment>()
    fun addFragment(fragment: Fragment) {
      arrayList.add(fragment)
    }

    override fun getItemCount(): Int {
      return arrayList.size
    }

    override fun createFragment(position: Int): Fragment {
      return arrayList[position]
    }
  }
}
