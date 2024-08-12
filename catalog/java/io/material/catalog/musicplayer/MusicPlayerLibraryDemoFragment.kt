package io.material.catalog.musicplayer

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import io.material.catalog.R
import java.util.Collections

open class MusicPlayerLibraryDemoFragment : Fragment(), AlbumAdapterListener,
  OnMenuItemClickListener {
  companion object {
    private val GRID_SPAN_COUNT = 2
    private val ALBUM_RECYCLER_VIEW_ID = View.generateViewId()
  }

  private lateinit var listContainer: FrameLayout
  private var listState: Parcelable? = null
  private var listTypeGrid = true
  private var listSorted = true
  open val demoLayoutResId: Int = R.layout.cat_music_player_library_demo_fragment
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(demoLayoutResId, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
    listContainer = view.findViewById(R.id.list_container)
    toolBar.setOnMenuItemClickListener(this)
    val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    setList(listTypeGrid,listSorted,sharedAxis)
  }

  override fun onDestroyView() {
    val rv = requireView().findViewById<RecyclerView>(ALBUM_RECYCLER_VIEW_ID)
    if (rv != null) {
      listState = rv.layoutManager?.onSaveInstanceState()
    }
    super.onDestroyView()
  }
  override fun onAlbumClicked(view: View, album: Album) {
   val fragment = MusicPlayerAlbumDemoFragment.newInstance(album.id)
    val transform = MaterialContainerTransform(requireContext(),true)
    fragment.sharedElementEnterTransition = transform

    Hold().apply {
      addTarget(R.id.container)
      duration = transform.duration
      exitTransition = this
    }
    parentFragmentManager.beginTransaction().addSharedElement(view,ViewCompat.getTransitionName(view)!!)
      .replace(R.id.fragment_container,fragment,MusicPlayerAlbumDemoFragment.TAG)
      .addToBackStack(MusicPlayerAlbumDemoFragment.TAG)
      .commit()
  }

  override fun onMenuItemClick(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.item_list_type){
      val fadeThrough = MaterialFadeThrough()
      setList(!listTypeGrid,listSorted,fadeThrough)
      return true
    }
    val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y,true)
    setList(listTypeGrid,!listSorted,sharedAxis)
    return true
  }
  open fun setListType(listTypeGrid: Boolean,transition: androidx.transition.Transition){
    if (this.listTypeGrid != listTypeGrid){
      setList(listTypeGrid,listSorted,transition)
    }
  }
  private fun setList(listTypeGrid: Boolean, listSorted: Boolean, transition: androidx.transition.Transition) {
    this.listTypeGrid = listTypeGrid
    this.listSorted = listSorted
    val rv = createRecyclerView(listTypeGrid)
    if (listState != null){
      rv.layoutManager?.onRestoreInstanceState(listState)
      listState = null
    }
    transition.addTarget(rv)
    val currentRv = listContainer.getChildAt(0)
    if (currentRv != null){
      transition.addTarget(currentRv)
    }
   TransitionManager.beginDelayedTransition(listContainer,transition)
    val adapter = AlbumsAdapter(this,listTypeGrid)
    rv.adapter = adapter
    val list = MusicData.ALBUMS
    if (!listSorted){
      list.reversed()
    }
    adapter.submitList(list)
    listContainer.removeAllViews()
    listContainer.addView(rv)

  }
  private fun createRecyclerView(listTypeGrid: Boolean): RecyclerView{
    return RecyclerView(requireContext()).apply {
      id = ALBUM_RECYCLER_VIEW_ID
      layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
      setPadding(0,resources.getDimensionPixelSize(R.dimen.cat_music_player_album_list_padding_vertical),0,resources.getDimensionPixelSize(R.dimen.cat_music_player_album_list_padding_vertical))
      clipToPadding = false
      if (listTypeGrid){
        layoutManager = androidx.recyclerview.widget.GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
      }else{
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
      }
    }
  }
}
