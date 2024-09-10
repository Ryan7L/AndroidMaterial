package io.material.catalog.progressindicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

abstract class ProgressIndicatorDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_progress_indicator_fragment, container, false)
    val content = view.findViewById<ViewGroup>(R.id.content)
    content.addView(inflater.inflate(progressIndicatorContentLayout, content, false))
    initDemoContents(view)
    if (progressIndicatorDemoControlLayout != 0) {
      val control = view.findViewById<ViewGroup>(R.id.control)
      control.addView(inflater.inflate(progressIndicatorDemoControlLayout, control, false))
    }
    initDemoControls(view)
    return view
  }

  open fun initDemoContents(view: View) {}
  open fun initDemoControls(view: View) {}

  @get:LayoutRes
  abstract val progressIndicatorContentLayout: Int

  @LayoutRes
  open val progressIndicatorDemoControlLayout: Int = 0
}
