package io.material.catalog.carousel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R

class CarouselAdapter @JvmOverloads constructor(
  private val listener: CarouselItemListener,
  private val itemLayoutResId: Int = R.layout.cat_carousel_item
) : ListAdapter<CarouselItem, CarouselItemViewHolder>(DIFF_CALLBACK) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
    return CarouselItemViewHolder(
      LayoutInflater.from(parent.context).inflate(itemLayoutResId, parent, false), listener
    )
  }

  override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  companion object {
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CarouselItem>() {
      /**
       * Called to check whether two objects represent the same item.
       *
       *
       * For example, if your items have unique ids, this method should check their id equality.
       *
       *
       * Note: `null` items in the list are assumed to be the same as another `null`
       * item and are assumed to not be the same as a non-`null` item. This callback will
       * not be invoked for either of those cases.
       *
       * @param oldItem The item in the old list.
       * @param newItem The item in the new list.
       * @return True if the two items represent the same object or false if they are different.
       * @see Callback.areItemsTheSame
       */
      override fun areItemsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
        //如果从数据库重新加载，用户属性可能会发生变化，但 ID 是固定的
        return oldItem == newItem
      }

      /**
       * Called to check whether two items have the same data.
       *
       *
       * This information is used to detect if the contents of an item have changed.
       *
       *
       * This method to check equality instead of [Object.equals] so that you can
       * change its behavior depending on your UI.
       *
       *
       * For example, if you are using DiffUtil with a
       * [RecyclerView.Adapter], you should
       * return whether the items' visual representations are the same.
       *
       *
       * This method is called only if [.areItemsTheSame] returns `true` for
       * these items.
       *
       *
       * Note: Two `null` items are assumed to represent the same contents. This callback
       * will not be invoked for this case.
       *
       * @param oldItem The item in the old list.
       * @param newItem The item in the new list.
       * @return True if the contents of the items are the same or false if they are different.
       * @see Callback.areContentsTheSame
       */
      override fun areContentsTheSame(oldItem: CarouselItem, newItem: CarouselItem): Boolean {
        return false
      }

    }
  }

}
