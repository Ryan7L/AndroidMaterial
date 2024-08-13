package io.material.catalog.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R
import io.material.catalog.musicplayer.MusicPlayerAlbumDemoFragment

private const val TAG = "AdaptiveMusicPlayerAlbumDemoFragment"
private const val ALBUM_ID_KEY = "album_id_key"

class AdaptiveMusicPlayerAlbumDemoFragment : MusicPlayerAlbumDemoFragment() {

  companion object {
    @JvmStatic
    fun newInstance(albumId: Long): AdaptiveMusicPlayerAlbumDemoFragment {
      return AdaptiveMusicPlayerAlbumDemoFragment().apply {
        arguments = Bundle().apply {
          putLong(ALBUM_ID_KEY, albumId)
        }
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_adaptive_music_player_album_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val container = view.findViewById<ViewGroup>(R.id.container)
    val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
    val albumImg = view.findViewById<ImageView>(R.id.album_image)
    val albumTitle = view.findViewById<TextView>(R.id.album_title)
    val albumArtist = view.findViewById<TextView>(R.id.album_artist)
    val rv = view.findViewById<RecyclerView>(R.id.song_recycler_view)
    setUpAlbumViews(container, toolBar, albumImg, albumTitle, albumArtist, rv)
  }

}
