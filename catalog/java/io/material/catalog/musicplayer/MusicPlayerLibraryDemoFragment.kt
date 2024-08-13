package io.material.catalog.musicplayer

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.FrameLayout.LayoutParams
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import io.material.catalog.R

private const val GRID_SPAN_COUNT = 2


open class MusicPlayerLibraryDemoFragment : Fragment(), AlbumAdapterListener,
  OnMenuItemClickListener {
  private val albumsRvId = View.generateViewId()
  private lateinit var listContainer: FrameLayout
  private var listState: Parcelable? = null
  private var listTypeGrid = true
  private var listSorted = true

  @get:LayoutRes
  open val demoLayoutResId: Int
    get() = R.layout.cat_music_player_library_demo_fragment

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(demoLayoutResId, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val toolBar = view.findViewById<Toolbar>(R.id.toolbar)
    listContainer = view.findViewById(R.id.list_container)
    toolBar.setOnMenuItemClickListener(this)
    val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    setList(listTypeGrid, listSorted, sharedAxis)
  }

  override fun onDestroyView() {
    val rv = requireView().findViewById<RecyclerView>(albumsRvId)
    if (rv != null) {
      listState = rv.layoutManager?.onSaveInstanceState()
    }
    super.onDestroyView()
  }

  override fun onAlbumClicked(view: View, album: Album) {
    val fragment = MusicPlayerAlbumDemoFragment.newInstance(album.id)
    val transform = MaterialContainerTransform(requireContext(), true)
    fragment.sharedElementEnterTransition = transform
    //使用Hold使该Fragment在过渡到专辑详细信息屏幕的 MaterialContainerTransform 下方保持可见。
    // 如果没有 Hold，一旦其容器被新的 Fragment 替换，该 Fragment 就会消失。
    Hold().run {
      //将根视图添加为Hold的目标，以便将整个视图层次结构作为一个整体保留，而不是单独保留每个子视图。有助于在过渡过程中保持阴影。
      addTarget(R.id.container)
      duration = transform.duration
      exitTransition = this
    }
    parentFragmentManager.beginTransaction()
      .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
      .replace(R.id.fragment_container, fragment, MusicPlayerAlbumDemoFragment.TAG)
      .addToBackStack(MusicPlayerAlbumDemoFragment.TAG).commit()
  }


  override fun onMenuItemClick(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.item_list_type) {
      //使用淡入淡出过渡在列表项视图类型之间交换
      val fadeThrough = MaterialFadeThrough()
      setList(!listTypeGrid, listSorted, fadeThrough)
      return true
    }
    //使用共享 Y 轴过渡对列表进行排序，显示传出视图和传入视图之间的空间关系。
    val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    setList(listTypeGrid, !listSorted, sharedAxis)
    return true
  }

  private fun createRecyclerView(listTypeGrid: Boolean): RecyclerView {

    return RecyclerView(requireContext()).apply {
      id = albumsRvId
      layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
      val verticalPadding =
        resources.getDimensionPixelSize(R.dimen.cat_music_player_album_list_padding_vertical)
      setPadding(0, verticalPadding, 0, verticalPadding)
      clipToPadding = false
      if (listTypeGrid) {
        layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
      } else {
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
      }
    }

  }

  /**
   * 添加或替换包含专辑列表的 RecyclerView，将其替换为新的 RecyclerView，该 RecyclerView 可以是列表/ 网格并且具有当前排序。
   * @param listTypeGrid Boolean 表示列表/网格
   * @param transition Transition 过渡动画
   */
  open fun setListType(listTypeGrid: Boolean, transition: Transition) {
    if (this.listTypeGrid != listTypeGrid) {
      setList(listTypeGrid, listSorted, transition)
    }
  }

  /**
   * 使用新的 RecyclerView 添加或替换包含专辑列表的 RecyclerView，该 RecyclerView 是列表/ 网格，并且根据给定的参数进行排序/ 未排序。
   * @param listTypeGrid Boolean 表示列表/网格
   * @param listSorted Boolean 表示排序/未排序
   * @param transition Transition 过渡动画
   */
  private fun setList(listTypeGrid: Boolean, listSorted: Boolean, transition: Transition) {
    this.listTypeGrid = listTypeGrid
    this.listSorted = listSorted
    //使用 Transition 为 RecyclerView 的删除和添加添加动画.
    val recyclerView = createRecyclerView(listTypeGrid)
    //恢复 RecyclerView 的滚动位置（如果可用）
    listState?.let {
      recyclerView.layoutManager?.onRestoreInstanceState(listState)
    }
    listState = null

    transition.addTarget(recyclerView)
    val currentRv = listContainer.getChildAt(0)
    currentRv?.let {
      transition.addTarget(it)
    }
    TransitionManager.beginDelayedTransition(listContainer, transition)

    val adapter = AlbumsAdapter(this, listTypeGrid)
    recyclerView.adapter = adapter
    val albums = MusicData.ALBUMS
    adapter.submitList(if (listSorted) albums else albums.reversed())
    listContainer.removeAllViews()
    listContainer.addView(recyclerView)
  }
}
