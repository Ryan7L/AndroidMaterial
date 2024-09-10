package io.material.catalog.card

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityViewCommand
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.material.catalog.R
import io.material.catalog.card.SelectableCardsAdapter.ItemViewHolder

class SelectableCardsAdapter : RecyclerView.Adapter<ItemViewHolder>() {

  private var selectionTracker: SelectionTracker<Long>? = null

  var items = emptyList<Item>()

  override fun getItemViewType(position: Int): Int {
    return 0
  }

  fun setSelectionTracker(selectionTracker: SelectionTracker<Long>) {
    this.selectionTracker = selectionTracker
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      LayoutInflater.from(parent.context).inflate(R.layout.cat_card_item_view, parent, false)
    )
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(items[position], position)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  inner class ItemViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemDetails = Details()
    private val cardView = itemView.findViewById<MaterialCardView>(R.id.item_card)
    private val titleView = itemView.findViewById<TextView>(R.id.cat_card_title)
    private val subtitleView = itemView.findViewById<TextView>(R.id.cat_card_subtitle)

    internal fun bind(item: Item, position: Int) {
      itemDetails.position = position.toLong()
      titleView.text = item.title
      subtitleView.text = item.subTitle
      selectionTracker?.let {
        bindSelectedState()
      }
    }

    private fun bindSelectedState() {
      val selectionKey = itemDetails.selectionKey
      cardView.isChecked = selectionTracker?.isSelected(selectionKey) ?: false
      addAccessibilityActions(selectionKey)
    }

    private fun addAccessibilityActions(selectionKey: Long) {
      ViewCompat.addAccessibilityAction(
        cardView,
        cardView.context.resources.getString(R.string.cat_card_action_select),
        object :
          AccessibilityViewCommand {
          /**
           * 执行该操作。
           *
           * @return 如果操作已处理，则为“true”，否则为“false”
           *
           * @param view 采取行动的View
           * @param arguments 可选操作参数
           */
          override fun perform(
            view: View,
            arguments: AccessibilityViewCommand.CommandArguments?
          ): Boolean {
            selectionTracker?.deselect(selectionKey)
            return true
          }

        })
    }
  }
}


class DetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
  /**
   * @return 事件下Item的 ItemDetails，或 null。
   */
  override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
    val view = recyclerView.findChildViewUnder(e.x, e.y)
    return view?.let {
      val viewHolder = recyclerView.getChildViewHolder(it)
      if (viewHolder is SelectableCardsAdapter.ItemViewHolder) {
        viewHolder.itemDetails
      } else {
        null
      }
    }
  }

}

class KeyProvider(adapter: SelectableCardsAdapter) :
  ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_MAPPED) {
  override fun getKey(position: Int): Long {
    return position.toLong()
  }

  override fun getPosition(key: Long): Int {
    return key.toInt()
  }
}

data class Item(val title: String, val subTitle: String)
class Details : ItemDetailsLookup.ItemDetails<Long>() {

  var position = 0L


  /**
   * 返回Item的适配器位置。请参阅[ViewHolder.getAdapterPosition][RecyclerView.ViewHolder.getAdapterPosition]
   *
   * @return Item的位置。
   */
  override fun getPosition(): Int {
    return position.toInt()
  }

  /**
   * @return Item的选择键。
   */
  override fun getSelectionKey(): Long {
    return position
  }

  override fun inSelectionHotspot(e: MotionEvent): Boolean {
    return false
  }

  override fun inDragRegion(e: MotionEvent): Boolean {
    return true
  }

}
