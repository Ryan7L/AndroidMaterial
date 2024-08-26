package io.material.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class CardSelectionModeActivity : DemoActivity(), ActionMode.Callback {
  private val ITEM_COUNT = 20

  private var actionMode: ActionMode? = null
  private var adapter: SelectableCardsAdapter? = null
  private var selectionTracker: SelectionTracker<Long>? = null

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_card_selection_activity, viewGroup, false)
    val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
    setUpRecyclerView(rv)
    return view
  }

  private fun setUpRecyclerView(rv: RecyclerView) {
    adapter = SelectableCardsAdapter()
    adapter?.items = generateItems()
    rv.adapter = adapter
    selectionTracker = SelectionTracker.Builder(
      "card_selection",
      rv,
      KeyProvider(adapter!!),
      DetailsLookup(rv),
      StorageStrategy.createLongStorage()
    ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()

    adapter?.setSelectionTracker(selectionTracker!!)
    selectionTracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
      override fun onSelectionChanged() {
        if (selectionTracker?.selection?.size()!! > 0) {
          if (actionMode == null) {
            actionMode = startSupportActionMode(this@CardSelectionModeActivity)
          }
          actionMode?.title = selectionTracker?.selection?.size().toString()
        } else if (actionMode != null) {
          actionMode!!.finish()
        }
      }
    })
    rv.layoutManager = LinearLayoutManager(this)
  }

  private fun generateItems(): List<Item> {
    val titlePrefix = getString(R.string.cat_card_selectable_item_title)
    val list = mutableListOf<Item>()
    repeat(ITEM_COUNT) {
      list.add(Item("$titlePrefix $it", getString(R.string.cat_card_selectable_content)))
    }
    return list
  }

  override val demoTitleResId: Int
    get() = R.string.cat_card_selection_mode

  /**
   * 首次创建ActionMode时调用。提供的菜单将用于生成ActionMode的操作按钮。
   *
   * @param mode 正在创建 ActionMode
   * @param menu 用于填充Action button的菜单
   * @return 如果应创建ActionMode，则为 true；如果应中止进入此模式，则为 false。
   */
  override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    return true
  }

  /**
   * 每当ActionModel无效时调用以刷新操作模式的操作菜单。
   *
   * @param mode ActionMode 正在准备中
   * @param menu 用于填充Action button的菜单
   * @return 如果菜单或ActionModel已更新，则为 true，否则为 false。
   */
  override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
    return false
  }

  /**
   * 调用以报告用户单击action button.。
   *
   * @param mode 当前的ActionMode
   * @param item 被点击的Item
   * @return 如果此回调处理了事件，则为 true；如果应继续标准 MenuItem 调用，则为 false。
   */
  override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
    return false
  }

  /**
   * 当ActionMode即将退出并销毁时调用。
   *
   * @param mode 当前的 ActionMode 被销毁
   */
  override fun onDestroyActionMode(mode: ActionMode?) {
    selectionTracker?.clearSelection()
    this.actionMode = null
  }
}
