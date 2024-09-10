package io.material.catalog.tableofcontents

import androidx.annotation.LayoutRes
import io.material.catalog.R

/**
 * 一个辅助类，可促进应用程序中的资源覆盖。
 * @property logoLayout Int
 */
open class TocResourceProvider {

  @LayoutRes
  open val logoLayout: Int = R.layout.cat_toc_logo
}
