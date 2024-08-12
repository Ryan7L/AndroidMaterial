package io.material.catalog.musicplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

open class MusicPlayerDemoActivity : DemoActivity() {
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(R.layout.cat_music_player_activity, viewGroup, false)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, libraryDemoFragment)
      .commit()
  }

  open val libraryDemoFragment: Fragment
    get() = MusicPlayerLibraryDemoFragment()

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
