package io.material.catalog.topappbar

import io.material.catalog.R

/**
 * 应用程序的大型折叠顶部应用程序栏演示的Fragment
 */
class TopAppBarCollapsingLargeDemoFragment : BaseTopAppBarCollapsingDemoFragment() {
  /**
   * 折叠工具栏的布局ID
   */
  override val collapsingToolBarLayoutResId: Int
    get() = R.layout.cat_topappbar_collapsing_large_fragment

}
