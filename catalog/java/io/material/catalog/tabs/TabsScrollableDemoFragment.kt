package io.material.catalog.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import androidx.core.view.indices
import com.google.android.material.tabs.TabLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import kotlin.math.max

class TabsScrollableDemoFragment : DemoFragment() {
  private val KEY_TABS = "TABS"
  private val KEY_TAB_GRAVITY = "TAB_GRAVITY"
  private var numTabs = 0
  private lateinit var tabTitles: Array<String>
  private lateinit var scrollableTabLayout: TabLayout

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_tabs_scrollable_fragment, container, false)
    initViews(view,savedInstanceState)
    return view
  }

  private fun initViews(view: View,bundle: Bundle?){
    val content = view.findViewById<ViewGroup>(R.id.content)
    val tabsContent = layoutInflater.inflate(R.layout.cat_tabs_scrollable_content,content,false)
    content.addView(tabsContent,0)
    scrollableTabLayout = tabsContent.findViewById(R.id.scrollable_tab_layout)
    val tabGravityStartButton = view.findViewById<RadioButton>(R.id.tabs_gravity_start_button)
    tabGravityStartButton.setOnClickListener {
      scrollableTabLayout.tabGravity = TabLayout.GRAVITY_START
    }

    val tabGravityCenterButton = view.findViewById<RadioButton>(R.id.tabs_gravity_center_button)
    tabGravityCenterButton.setOnClickListener {
      scrollableTabLayout.tabGravity = TabLayout.GRAVITY_CENTER
    }
    bundle?.let {
      scrollableTabLayout.removeAllTabs()
      scrollableTabLayout.tabGravity = it.getInt(KEY_TAB_GRAVITY)
      val tabLabels = bundle.getStringArray(KEY_TABS)
      tabLabels?.forEach { label ->
        scrollableTabLayout.addTab(scrollableTabLayout.newTab().setText(label))
      }

    }
    numTabs = scrollableTabLayout.tabCount
    tabTitles = requireContext().resources.getStringArray(R.array.cat_tabs_titles)
    val addBtn = view.findViewById<Button>(R.id.add_tab_button)
    addBtn.setOnClickListener {
      scrollableTabLayout.addTab(scrollableTabLayout.newTab().setText(tabTitles[numTabs % tabTitles.size]))
      numTabs ++
    }
    val removeFirstBtn = view.findViewById<Button>(R.id.remove_first_tab_button)
    removeFirstBtn.setOnClickListener {
      if (scrollableTabLayout.tabCount > 0) {
        scrollableTabLayout.removeTabAt(0)
      }
      numTabs = max(0, numTabs - 1)
    }
    val removeLastBtn = view.findViewById<Button>(R.id.remove_last_tab_button)
    removeLastBtn.setOnClickListener {
      val tab = scrollableTabLayout.getTabAt(scrollableTabLayout.tabCount - 1)
      if (tab != null) {
        scrollableTabLayout.removeTab(tab)
      }
      numTabs = max(0, numTabs - 1)
    }
  }
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val tabLabels = arrayOfNulls<String>(scrollableTabLayout.tabCount)
    for (i in scrollableTabLayout.indices){
      tabLabels[i] = scrollableTabLayout.getTabAt(i)?.text.toString()
    }
    outState.putStringArray(KEY_TABS,tabLabels)
    outState.putInt(KEY_TAB_GRAVITY,scrollableTabLayout.tabGravity)
  }
}
