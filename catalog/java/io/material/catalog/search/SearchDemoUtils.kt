package io.material.catalog.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R

object SearchDemoUtils {

  fun setUpSearchBar(activity: Activity, searchBar: SearchBar) {
    searchBar.inflateMenu(R.menu.cat_searchbar_menu)
    searchBar.setOnMenuItemClickListener {
      showSnackBar(activity, it)
      return@setOnMenuItemClickListener true
    }
  }

  fun setUpSearchView(activity: AppCompatActivity, searchBar: SearchBar, searchView: SearchView) {
    searchView.inflateMenu(R.menu.cat_searchview_menu)
    searchView.setOnMenuItemClickListener {
      showSnackBar(activity, it)
      return@setOnMenuItemClickListener true
    }
    searchView.editText.setOnEditorActionListener { v, actionId, event ->
      submitSearchQuery(searchBar, searchView, searchView.text.toString())
      return@setOnEditorActionListener false
    }
    val onBackPressedCallback = object : OnBackPressedCallback(false) {
      /**
       * 用于处理 [OnBackPressedDispatcher.onBackPressed] 事件的回调.
       */
      override fun handleOnBackPressed() {
        searchView.hide()
      }
    }
    activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)
    searchView.addTransitionListener { searchView, previousState, newState ->
      onBackPressedCallback.isEnabled = newState == SearchView.TransitionState.SHOWN
    }
  }

  fun startOnLoadAnimation(searchBar: SearchBar, bundle: Bundle?) {
//    不要在旋转时启动动画。仅在演示中需要，因为 minIntervalSeconds 为 0。
    if (bundle == null) {
      searchBar.startOnLoadAnimation()
    }
  }

  fun setUpSuggestions(
    suggestionContainer: ViewGroup,
    searchBar: SearchBar,
    searchView: SearchView
  ) {
    addSuggestionTitleView(
      suggestionContainer,
      R.string.cat_searchview_suggestion_section_title_yesterday
    )
    addSuggestionItemViews(suggestionContainer, getYesterdaySuggestions(), searchBar, searchView)
    addSuggestionTitleView(
      suggestionContainer,
      R.string.cat_searchview_suggestion_section_title_this_week
    )
    addSuggestionItemViews(suggestionContainer, getThisWeekSuggestions(), searchBar, searchView)
  }

  private fun submitSearchQuery(searchBar: SearchBar, searchView: SearchView, query: String) {
    searchBar.setText(query)
    searchView.hide()
  }

  private fun addSuggestionTitleView(parent: ViewGroup, @StringRes titleResId: Int) {
    (LayoutInflater.from(parent.context)
      .inflate(R.layout.cat_search_suggestion_title, parent, false) as TextView).let {
      it.setText(titleResId)
      parent.addView(it)
    }
  }

  private fun addSuggestionItemViews(
    parent: ViewGroup, suggestionItems: List<SuggestionItem>,
    searchBar: SearchBar, searchView: SearchView
  ) {
    suggestionItems.forEach {
      addSuggestionItemView(parent, it, searchBar, searchView)
    }
  }

  private fun addSuggestionItemView(
    parent: ViewGroup,
    suggestionItem: SuggestionItem,
    searchBar: SearchBar,
    searchView: SearchView
  ) {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.cat_search_suggestion_item, parent, false)
    view.findViewById<TextView>(R.id.cat_searchbar_suggestion_title).text = suggestionItem.title
    view.findViewById<TextView>(R.id.cat_searchbar_suggestion_subtitle).text =
      suggestionItem.subtitle
    view.findViewById<ImageView>(R.id.cat_searchbar_suggestion_icon)
      .setImageResource(suggestionItem.iconResId)
    view.setOnClickListener {
      submitSearchQuery(searchBar, searchView, suggestionItem.title)
    }
    parent.addView(view)
  }

  private fun getYesterdaySuggestions(): List<SuggestionItem> {
    return listOf(
      SuggestionItem(
        R.drawable.ic_schedule_vd_theme_24,
        "481 Van Brunt Street",
        "Brooklyn, NY"
      ),
      SuggestionItem(
        R.drawable.ic_home_vd_theme_24,
        "Home",
        "199 Pacific Street, Brooklyn, NY"
      )
    )
  }

  private fun getThisWeekSuggestions(): List<SuggestionItem> {
    return listOf(
      SuggestionItem(
        R.drawable.ic_schedule_vd_theme_24,
        "BEP GA",
        "Forsyth Street, New York, NY"
      ),
      SuggestionItem(
        R.drawable.ic_schedule_vd_theme_24,
        "Sushi Nakazawa",
        "Commerce Street, New York, NY"
      ),
      SuggestionItem(R.drawable.ic_schedule_vd_theme_24, "IFC Center", "6th Avenue, New York, NY")
    )
  }

  fun showSnackBar(activity: Activity, menuItem: MenuItem) {
    Snackbar.make(
      activity.findViewById(R.id.content),
      menuItem.title.toString(),
      Snackbar.LENGTH_SHORT
    ).show()
  }
}

data class SuggestionItem(@DrawableRes val iconResId: Int, val title: String, val subtitle: String)
