package io.material.catalog.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import dagger.android.ContributesAndroidInjector
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.feature.DemoActivity

private const val TAG = "SearchRecyclerDemoActivity"
private const val ITEM_COUNT = 30

class SearchRecyclerDemoActivity : DemoActivity() {

  private lateinit var items: List<Item>

  private lateinit var adapter: Adapter

  private lateinit var rv: RecyclerView

  private lateinit var appBarLayout: AppBarLayout

  private lateinit var searchBar: SearchBar

  private lateinit var searchView: SearchView

  private lateinit var suggestionContainer: LinearLayout

  private lateinit var contextualToolbarContainer: ViewGroup

  private lateinit var contextualToolbar: Toolbar
  private lateinit var spinner: ProgressBar
  private val onBackPressedCallback = object : OnBackPressedCallback(false) {
    /**
     * 用于处理 [OnBackPressedDispatcher.onBackPressed] 事件的回调。
     */
    override fun handleOnBackPressed() {
      hideContextualToolbarAndClearSelection()
    }

  }

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
    val rootView = layoutInflater.inflate(R.layout.cat_search_recycler_fragment, viewGroup, false)
    rv = rootView.findViewById(R.id.recycler_view)
    appBarLayout = rootView.findViewById(R.id.app_bar_layout)
    searchBar = rootView.findViewById(R.id.open_search_bar)
    searchView = rootView.findViewById(R.id.open_search_view)
    suggestionContainer =
      rootView.findViewById(R.id.open_search_view_suggestion_container)
    contextualToolbarContainer = rootView.findViewById(R.id.contextual_toolbar_container)
    contextualToolbar = rootView.findViewById(R.id.contextual_toolbar)
    spinner = rootView.findViewById(R.id.spinner)
    setUpRv()
    setUpContextualToolbar()
    SearchDemoUtils.setUpSearchBar(this, searchBar)
    SearchDemoUtils.setUpSearchView(this, searchBar, searchView)
    SearchDemoUtils.setUpSuggestions(suggestionContainer, searchBar, searchView)
    SearchDemoUtils.startOnLoadAnimation(searchBar, bundle)
    ViewCompat.setOnApplyWindowInsetsListener(contextualToolbarContainer) { insetsView, insets ->
      val sysInsetTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
      insetsView.setPadding(0, sysInsetTop, 0, 0)
      return@setOnApplyWindowInsetsListener insets
    }
    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    return rootView
  }


  private fun setUpRv() {
    items = generateItem()
    adapter = Adapter(items)
    adapter.onItemSelectedStateChangedListener = OnItemSelectedStateChangedListener {
      val selectedItemCount = getSelectedItemCount()
      if (selectedItemCount > 0 && Adapter.selectionModeEnabled) {
        contextualToolbar.title = selectedItemCount.toString()
        expandContextualToolbar()
      } else {
        Adapter.selectionModeEnabled = false
        collapseContextualToolbar()
      }
    }
    rv.layoutManager = LinearLayoutManager(this)
    rv.adapter = adapter
    Handler(Looper.getMainLooper()).postDelayed({
      spinner.visibility = View.GONE
      adapter.items = items
      adapter.notifyDataSetChanged()
    }, 1500)
  }

  private fun setUpContextualToolbar() {
    contextualToolbar.setNavigationOnClickListener {
      hideContextualToolbarAndClearSelection()
    }
    contextualToolbar.inflateMenu(R.menu.cat_searchbar_contextual_toolbar_menu)
    contextualToolbar.setOnMenuItemClickListener {
      if (it.itemId == R.id.action_select_all) {
        setItemsSelected(true)
        contextualToolbar.title = getSelectedItemCount().toString()
        return@setOnMenuItemClickListener true
      }
      return@setOnMenuItemClickListener false
    }
  }

  private fun hideContextualToolbarAndClearSelection() {
    Adapter.selectionModeEnabled = false
    if (collapseContextualToolbar()) {
      setItemsSelected(false)
    }
  }

  private fun setItemsSelected(selected: Boolean) {
    items.forEach { it.selected = selected }
    adapter.notifyDataSetChanged()
  }

  private fun expandContextualToolbar() {
    onBackPressedCallback.isEnabled = true
    searchBar.expand(contextualToolbarContainer, appBarLayout)
  }

  private fun collapseContextualToolbar(): Boolean {
    onBackPressedCallback.isEnabled = false
    return searchBar.collapse(contextualToolbarContainer, appBarLayout)
  }

  private fun getSelectedItemCount(): Long {
    return items.count { it.selected }.toLong()
  }

  private fun generateItem(): List<Item> {
    val titlePrefix = getString(R.string.cat_searchbar_recycler_item_title_prefix)
    val fillerText = arrayOf(
      getString(R.string.cat_searchbar_lorem_ipsum_1),
      getString(R.string.cat_searchbar_lorem_ipsum_2),
      getString(R.string.cat_searchbar_lorem_ipsum_3),
      getString(R.string.cat_searchbar_lorem_ipsum_4),
      getString(R.string.cat_searchbar_lorem_ipsum_5)
    )
    val items = mutableListOf<Item>().apply {
      for (i in 1..ITEM_COUNT) {
        add(Item("$titlePrefix $i", fillerText[i % fillerText.size]))
      }
    }
    return items
  }


}

data class Item(val title: String, val subTitle: String, var selected: Boolean = false)

class Adapter(var items: List<Item> = emptyList()) : RecyclerView.Adapter<ItemViewHolder>() {
  companion object {
    var selectionModeEnabled = false
  }

  var onItemSelectedStateChangedListener: OnItemSelectedStateChangedListener? = null
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.cat_search_recycler_item, parent, false)
    return ItemViewHolder((view))
  }


  override fun getItemCount(): Int = items.size


  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(items[position], onItemSelectedStateChangedListener)
  }

}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  var cardView = itemView.findViewById<MaterialCardView>(R.id.cat_searchbar_recycler_card)
  var titleTv = itemView.findViewById<TextView>(R.id.cat_searchbar_recycler_title)
  var subTitleTv = itemView.findViewById<TextView>(R.id.cat_searchbar_recycler_subtitle)


  fun bind(item: Item, onItemSelectedStateChangedListener: OnItemSelectedStateChangedListener?) {
    titleTv.text = item.title
    subTitleTv.text = item.subTitle
    bindSelectedState(item)
    itemView.setOnLongClickListener {
      if (Adapter.selectionModeEnabled) {
        return@setOnLongClickListener false
      }
      Adapter.selectionModeEnabled = true
      toggleItem(item, onItemSelectedStateChangedListener)
      return@setOnLongClickListener true
    }
    itemView.setOnClickListener {
      if (Adapter.selectionModeEnabled) {
        toggleItem(item, onItemSelectedStateChangedListener)
      }
    }
  }

  private fun toggleItem(
    item: Item,
    onItemSelectedStateChangedListener: OnItemSelectedStateChangedListener?
  ) {
    item.selected = !item.selected
    bindSelectedState(item)
    onItemSelectedStateChangedListener?.onItemSelectedStateChanged(item)
  }

  private fun bindSelectedState(item: Item) {
    cardView.isChecked = item.selected
  }
}

fun interface OnItemSelectedStateChangedListener {
  fun onItemSelectedStateChanged(item: Item)
}

@dagger.Module
abstract class SearchRecyclerViewDemoModule {

  @ActivityScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): SearchRecyclerDemoActivity
}
