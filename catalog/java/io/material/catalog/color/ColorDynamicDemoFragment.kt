package io.material.catalog.color

import io.material.catalog.R

class ColorDynamicDemoFragment : ColorPaletteDemoFragment() {
  override val colorsLayoutResId: Int
    get() = R.layout.cat_colors_palette_fragment
  override val colorsArrayResId: Int
    get() = R.array.cat_dynamic_colors
}
