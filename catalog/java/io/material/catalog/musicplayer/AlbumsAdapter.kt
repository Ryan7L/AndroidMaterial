package io.material.catalog.musicplayer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R

/**
 * ListAdapter 是 RecyclerView.Adapter 的一个子类，旨在与 RecyclerView 协同工作，更高效地显示列表数据，尤其是在列表频繁更新的情况下。
 * 主要作用：
 * 自动计算列表差异： ListAdapter 使用 DiffUtil 类来自动计算新旧列表之间的差异，并只更新需要更改的项目。  这可以显著提高列表更新的效率，减少不必要的 UI 操作。
 * 异步更新列表： ListAdapter 可以在后台线程计算列表差异，并在主线程更新 UI，从而避免阻塞主线程，提高应用的响应速度。
 * 简化代码： ListAdapter 简化了列表更新的代码，您只需调用 submitList() 方法即可更新列表数据，无需手动调用 notifyDataSetChanged() 或其他通知方法。
 *
 * 使用方法：
 * 创建 ListAdapter： 创建一个继承自 ListAdapter 的类，并实现 onCreateViewHolder() 和 onBindViewHolder() 方法。
 * 实现 DiffUtil.ItemCallback： 创建一个 DiffUtil.ItemCallback 的实例，用于定义如何比较列表项。
 * 调用 submitList()： 调用 submitList() 方法将新列表提交给 ListAdapter。
 *
 * 注： List拥有两个泛型参数，第一个是列表接收的数据类型，第二个是ViewHolder
 */


/**
 * 以列表或网格布局显示专辑列表的适配器。
 * @param listener item点击监听
 * @param asGrid 是否是网格布局
 */
class AlbumsAdapter(val listener: AlbumAdapterListener, asGrid: Boolean) :
  ListAdapter<Album, AlbumViewHolder>(Album.DIFF_CALLBACK) {
  private val itemLayout =
    if (asGrid) R.layout.cat_music_player_album_grid_item else R.layout.cat_music_player_album_list_item

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
    return AlbumViewHolder(
      LayoutInflater.from(parent.context).inflate(itemLayout, parent, false),
      listener
    )
  }

  override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

}

fun interface AlbumAdapterListener {
  fun onAlbumClicked(view: View, album: Album)
}

class AlbumViewHolder(val view: View, val listener: AlbumAdapterListener) :
  RecyclerView.ViewHolder(view) {

  private val container = view.findViewById<View>(R.id.album_item_container)
  private val albumImage = view.findViewById<ImageView>(R.id.album_image)
  private val albumTitle = view.findViewById<TextView>(R.id.album_title)
  private val albumArtist = view.findViewById<TextView>(R.id.album_artist)

  //  可选字段仅在列表项布局中可用。
  private val albumDuration: TextView? = view.findViewById(R.id.album_duration)

  fun bind(album: Album) {
    //创建共享元素过渡，第一个参数为共享的元素，第二个参数为过渡的名称，过渡名称需要跳转处和目的处设置相同的名字
    ViewCompat.setTransitionName(container, album.title)
    container.setOnClickListener { listener.onAlbumClicked(container, album) }
    albumImage.setImageResource(album.cover)
    albumTitle.text = album.title
    albumArtist.text = album.artist
    albumDuration?.text = album.duration

  }

}
