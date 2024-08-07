package io.material.catalog.z_ryan.bar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class ToolBarActivity : DemoActivity() {
  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View {
    return layoutInflater.inflate(R.layout.cat_bar_toolbar_activity, viewGroup, false)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
  override val isShouldShowDefaultDemoActionBarCloseButton: Boolean
    get() = false
}
