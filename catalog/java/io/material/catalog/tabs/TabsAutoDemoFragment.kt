package io.material.catalog.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.indices
import com.google.android.material.tabs.TabLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TabsAutoDemoFragment : DemoFragment() {
  private val KEY_TABS = "TABS"
  private var numTabs = 0
  private lateinit var tabTitles: Array<String>
  private lateinit var autoScrollableTabLayout: TabLayout

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_tabs_auto_fragment, container, false)

    initViews(view, savedInstanceState)
    return view
  }

  private fun initViews(view: View, bundle: Bundle?) {
    val content = view.findViewById<ViewGroup>(R.id.content)
    val tabsContent = layoutInflater.inflate(R.layout.cat_tabs_auto_content, content, false)
    content.addView(tabsContent, 0)
    autoScrollableTabLayout = tabsContent.findViewById(R.id.auto_tab_layout)
    bundle?.let {
      autoScrollableTabLayout.removeAllTabs()
      val tabLabels = it.getStringArray(KEY_TABS)
      tabLabels?.forEach { label ->
        autoScrollableTabLayout.addTab(autoScrollableTabLayout.newTab().setText(label))
      }
    }
    numTabs = autoScrollableTabLayout.tabCount
    tabTitles = requireContext().resources.getStringArray(R.array.cat_tabs_titles)
    val addBtn = view.findViewById<Button>(R.id.add_tab_button)
    addBtn.setOnClickListener {
      autoScrollableTabLayout.addTab(
        autoScrollableTabLayout.newTab().setText(tabTitles[numTabs % tabTitles.size])
      )
      numTabs++
    }
    val removeBtn = view.findViewById<Button>(R.id.remove_tab_button)
    removeBtn.setOnClickListener {
      if (autoScrollableTabLayout.tabCount > 0) {
        autoScrollableTabLayout.removeTabAt(0)
      }
      numTabs = Math.max(0, numTabs - 1)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    val tabLabels = arrayOfNulls<String>(autoScrollableTabLayout.tabCount)
    for (i in autoScrollableTabLayout.indices) {
      tabLabels[i] = autoScrollableTabLayout.getTabAt(i)?.text.toString()
    }
    outState.putStringArray(KEY_TABS, tabLabels)
  }
}
