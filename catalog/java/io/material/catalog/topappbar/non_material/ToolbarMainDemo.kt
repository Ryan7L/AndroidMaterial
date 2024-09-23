package io.material.catalog.topappbar.non_material

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import io.material.catalog.feature.DemoFragment

class ToolbarMainDemo : DemoFragment() {
  private lateinit var toolbar: Toolbar
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    TODO("Not yet implemented")
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
