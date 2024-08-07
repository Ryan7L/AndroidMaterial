package io.material.catalog.z_ryan.bar

import android.content.Intent
import io.material.catalog.R
import io.material.catalog.feature.Demo
import io.material.catalog.feature.DemoLandingFragment

class BarFragment : DemoLandingFragment() {

  override val titleResId: Int = R.string.cat_bar_title
  override val descriptionResId: Int = R.string.cat_bar_description
  override val mainDemo: Demo= object : Demo() {
    override val activityIntent: Intent
      get() = Intent(requireContext(), ActionBarActivity::class.java)
  }
  override val additionalDemos: List<Demo> = listOf(
  object : Demo(R.string.cat_bar_demo_toolbar_title) {
    override val activityIntent: Intent
      get() = Intent(requireContext(), ToolBarActivity::class.java)
  }
  )
}
