package io.material.catalog.tabs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.material.catalog.R

class TabsPagerAdapter(
  manager: FragmentManager,
  private val context: Context,
  private val numTabs: Int
) : FragmentPagerAdapter(manager) {
  override fun getItem(position: Int): Fragment {
    return TabItemContentFragment.newInstance(getReadableTabPosition(position))
  }

  override fun getCount(): Int {
    return numTabs
  }

  override fun getPageTitle(position: Int): CharSequence {
    return String.format(
      context.getString(R.string.cat_tab_item_label),
      getReadableTabPosition(position)
    )
  }

  private fun getReadableTabPosition(position: Int) = position + 1
}
