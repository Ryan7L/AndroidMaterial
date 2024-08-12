package io.material.catalog.musicplayer

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import dagger.android.support.DaggerFragment
import io.material.catalog.R
import kotlin.math.abs

open class MusicPlayerAlbumDemoFragment : DaggerFragment() {
  companion object {
    @JvmField
    val TAG = "MusicPlayerAlbumDemoFragment"

    @JvmField
    val ALBUM_ID_KEY = "album_id_key"

    @JvmStatic
    open fun newInstance(albumId: Long): MusicPlayerAlbumDemoFragment {
      val args = Bundle().apply {
        putLong(ALBUM_ID_KEY, albumId)
      }
      return MusicPlayerAlbumDemoFragment().apply {
        arguments = args
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_music_player_album_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val container = view.findViewById<ViewGroup>(R.id.container)
    val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
    val albumImg = view.findViewById<ImageView>(R.id.album_image)
    val albumTitle = view.findViewById<TextView>(R.id.album_title)
    val albumArtist = view.findViewById<TextView>(R.id.album_artist)
    val rv = view.findViewById<RecyclerView>(R.id.song_recycler_view)
    setUpAlbumViews(container, toolBar, albumImg, albumTitle, albumArtist, rv)
    val appBarLayout = view.findViewById<AppBarLayout>(R.id.app_bar_layout)
    val fab = view.findViewById<FloatingActionButton>(R.id.fab)
    val musicPlayerContainer = view.findViewById<View>(R.id.music_player_container)
    appBarLayout.addOnOffsetChangedListener { layout, verticalOffset ->
      val verticalOffsetPercentage =  abs(verticalOffset) / layout.totalScrollRange.toFloat()
      if (verticalOffsetPercentage > 0.2f && fab.isOrWillBeShown){
        fab.hide()
      }else if (verticalOffsetPercentage <= 0.2f && fab.isOrWillBeHidden && musicPlayerContainer.visibility != View.VISIBLE){
        fab.show()
      }
    }
    //设置音乐播放器过渡
    val musicPlayerEnterTransform = createMusicPlayerTransform(requireContext(), true, fab, musicPlayerContainer)
    fab.setOnClickListener {
      TransitionManager.beginDelayedTransition(container,musicPlayerEnterTransform)
      fab.visibility = View.GONE
      musicPlayerContainer.visibility = View.VISIBLE
    }
    val musicPlayerExitTransform = createMusicPlayerTransform(requireContext(), false, musicPlayerContainer, fab)
    musicPlayerContainer.setOnClickListener {
      TransitionManager.beginDelayedTransition(container,musicPlayerExitTransform)
      musicPlayerContainer.visibility = View.GONE
      fab.visibility = View.VISIBLE
    }
  }

  open fun setUpAlbumViews(
    container: ViewGroup,
    toolbar: Toolbar,
    albumImage: ImageView,
    albumTitle: TextView,
    albumArtist: TextView,
    rv: RecyclerView
  ) {
    val albumId = arguments?.getLong(ALBUM_ID_KEY, 0L) ?: 0L
    val album = MusicData.getAlbumById(albumId)
    //设置与共享元素过渡的要过渡的列表网格项相匹配的过渡名称。
    ViewCompat.setTransitionName(container, album.title)

    ViewCompat.setElevation(toolbar, 0F)

    toolbar.setNavigationOnClickListener {
      activity?.onBackPressed()
    }

    albumImage.setImageResource(album.cover)
    albumTitle.text = album.title
    albumArtist.text = album.artist

    rv.layoutManager = LinearLayoutManager(requireContext())
    val adapter = TrackAdapter()
    rv.adapter = adapter
    adapter.submitList(album.tracks)
  }

  private fun createMusicPlayerTransform(
    context: Context, entering: Boolean, startView: View, endView: View
  ): MaterialContainerTransform {
    return MaterialContainerTransform(context, entering).apply {
      setPathMotion(MaterialArcMotion())
      scrimColor = Color.TRANSPARENT
      setStartView(startView)
      setEndView(endView)
      addTarget(endView)
    }
  }
}

class TrackAdapter : ListAdapter<Track, TrackViewHolder>(Track.DIFF_CALLBACK) {
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
    return TrackViewHolder(
      LayoutInflater.from(parent.context)
        .inflate(R.layout.cat_music_player_track_list_item, parent, false)
    )
  }

  override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

}

class TrackViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
  private val playingIcon = view.findViewById<ImageView>(R.id.playing_icon)
  private val trackNumber = view.findViewById<TextView>(R.id.track_number)
  private val trackTitle = view.findViewById<TextView>(R.id.track_title)
  private val trackDuration = view.findViewById<TextView>(R.id.track_duration)
  fun bind(track: Track) {
    playingIcon.visibility = if (track.playing) View.VISIBLE else View.INVISIBLE
    trackNumber.text = track.track.toString()
    trackTitle.text = track.title
    trackDuration.text = track.duration

  }
}
