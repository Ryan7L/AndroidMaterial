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
 * 以列表或网格布局显示专辑列表的适配器。
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

  //  Optional field available only in the list item layout.
  private val albumDuration: TextView? = view.findViewById(R.id.album_duration)

  fun bind(album: Album) {
    //使用唯一的转换名称，以便在转换到相册详细信息屏幕时可以将此项用作共享元素。
    ViewCompat.setTransitionName(container, album.title)
    container.setOnClickListener { listener.onAlbumClicked(container, album) }
    albumImage.setImageResource(album.cover)
    albumTitle.text = album.title
    albumArtist.text = album.artist
    albumDuration?.text = album.duration

  }

}
