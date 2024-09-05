package io.material.catalog.adaptive

import androidx.fragment.app.Fragment
import io.material.catalog.musicplayer.MusicPlayerDemoActivity

class AdaptiveMusicPlayerDemoActivity: MusicPlayerDemoActivity() {
  override val libraryDemoFragment: Fragment
    get() = AdaptiveMusicPlayerLibraryDemoFragment()
}
