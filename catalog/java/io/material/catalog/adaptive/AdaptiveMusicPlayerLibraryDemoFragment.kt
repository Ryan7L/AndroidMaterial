package io.material.catalog.adaptive

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.core.view.ViewCompat
import androidx.window.java.layout.WindowInfoTrackerCallbackAdapter
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import io.material.catalog.R
import io.material.catalog.musicplayer.Album
import io.material.catalog.musicplayer.MusicPlayerLibraryDemoFragment
import java.util.concurrent.Executor

class AdaptiveMusicPlayerLibraryDemoFragment : MusicPlayerLibraryDemoFragment() {

  private var windowInfoTracker: WindowInfoTrackerCallbackAdapter? = null
  private val stateContainer = StateContainer()
  private val handler = Handler(Looper.getMainLooper())
  private val executor = Executor { command -> handler.post { handler.post(command) } }
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(demoLayoutResId, container, false)
    windowInfoTracker = WindowInfoTrackerCallbackAdapter(WindowInfoTracker.getOrCreate(requireActivity()))
    return view
  }

  override fun onStart() {
    super.onStart()
    windowInfoTracker?.addWindowLayoutInfoListener(requireActivity(), executor, stateContainer)
  }

  override fun onAlbumClicked(view: View, album: Album) {
    val fragment = AdaptiveMusicPlayerAlbumDemoFragment.newInstance(album.id)
    val transform = MaterialContainerTransform(requireContext(), true)
    fragment.sharedElementEnterTransition = transform
    Hold().run {
      addTarget(R.id.sliding_pane_layout)
      duration = transform.duration
      exitTransition = this
    }
    parentFragmentManager.beginTransaction().addSharedElement(view,ViewCompat.getTransitionName(view)!!)
      .replace(R.id.fragment_container,fragment,AdaptiveMusicPlayerAlbumDemoFragment.TAG)
      .addToBackStack(AdaptiveMusicPlayerAlbumDemoFragment.TAG)
      .commit()
  }

  override fun onStop() {
    super.onStop()
    windowInfoTracker?.removeWindowLayoutInfoListener(stateContainer)
  }
  inner class StateContainer : Consumer<WindowLayoutInfo> {
    override fun accept(value: WindowLayoutInfo) {
      val displayFeatures = value.displayFeatures
      val fadeThrough = MaterialFadeThrough()
      var listTypeGrid = false
      displayFeatures.forEach {
        if ((it as FoldingFeature).state == FoldingFeature.State.HALF_OPENED || it.state == FoldingFeature.State.FLAT) {
          listTypeGrid = true
        }
      }
      setListType(listTypeGrid, fadeThrough)
    }
  }
}
