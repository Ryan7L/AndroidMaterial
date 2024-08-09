package io.material.catalog.z_ryan.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.feature.DemoActivity

class SampleActivity : DemoActivity() {
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
  ): View? {
    TODO("Not yet implemented")
  }

  override val isShouldShowDefaultDemoActionBar: Boolean = false

}
