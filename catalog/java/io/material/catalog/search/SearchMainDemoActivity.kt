package io.material.catalog.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class SearchMainDemoActivity : DemoActivity() {
  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_search_fragment, viewGroup, false)
    val searchBar = view.findViewById<SearchBar>(R.id.cat_search_bar)
    val searchView = view.findViewById<SearchView>(R.id.cat_search_view)
    val suggestionContainer =
      view.findViewById<LinearLayout>(R.id.cat_search_view_suggestion_container)
    SearchDemoUtils.setUpSearchBar(this, searchBar)
    SearchDemoUtils.setUpSearchView(this, searchBar, searchView)
    SearchDemoUtils.setUpSuggestions(suggestionContainer, searchBar, searchView)
    SearchDemoUtils.startOnLoadAnimation(searchBar, bundle)
    return view
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
