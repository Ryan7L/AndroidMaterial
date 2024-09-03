package io.material.catalog.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.math.MathUtils
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class TabsMainDemoFragment : DemoFragment() {
  private lateinit var tabLayouts: List<TabLayout>
  private val badgeGravityValues = intArrayOf(
    BadgeDrawable.TOP_END,
    BadgeDrawable.TOP_START,
    BadgeDrawable.BOTTOM_END,
    BadgeDrawable.BOTTOM_START
  )

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_tabs_main_content, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    tabLayouts = DemoUtils.findViewsWithType(view, TabLayout::class.java)
    val incrementBadgeNumberButton = view.findViewById<View>(R.id.increment_badge_number_button)
    incrementBadgeNumberButton.setOnClickListener { incrementBadgeNumber() }
    val badgeGravitySpinner = view.findViewById<Spinner>(R.id.badge_gravity_spinner)
    val adapter = ArrayAdapter.createFromResource(
      badgeGravitySpinner.context,
      R.array.cat_tabs_badge_gravity_titles,
      android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    badgeGravitySpinner.adapter = adapter
    badgeGravitySpinner.onItemSelectedListener = object : OnItemSelectedListener {
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
    setUpBadging()
  }

  private fun setUpBadging() {
    val badgeNumbers = intArrayOf(0, 99, 9999)
    tabLayouts.forEach {
      for (i in badgeNumbers.indices) {
        val badge = it.getTabAt(0)?.orCreateBadge
        badge?.isVisible = true
        if (i != 0) {
          badge?.number = i
        }
      }
      it.addOnTabSelectedListener(object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
          clearAndHideBadges(tab?.position ?: 0)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
          clearAndHideBadges(tab?.position ?: 0)
        }
      })

    }
  }

  private fun incrementBadgeNumber() {
    tabLayouts.forEach {
      val badge = it.getTabAt(0)?.orCreateBadge
      badge?.isVisible = true
      badge?.number = badge?.number?.plus(1) ?: 0
    }
  }

  private fun clearAndHideBadges(tabPosition: Int) {
    tabLayouts.forEach {
      if (tabPosition == 0) {
        val badge = it.getTabAt(tabPosition)?.badge
        badge?.isVisible = false
        badge?.clearNumber()
      } else {
        it.getTabAt(tabPosition)?.removeBadge()
      }
    }
  }

  private fun updateBadgeGravity(badgeGravity: Int) {
    tabLayouts.forEach {
      for (i in 0 until it.tabCount) {
        val badge = it.getTabAt(i)?.badge
        badge?.badgeGravity = badgeGravity
      }
    }
  }
}
