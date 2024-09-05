package io.material.catalog.color

import androidx.annotation.ColorRes
import androidx.annotation.IdRes

data class HarmonizableButtonData(
  @IdRes val buttonId: Int,
  @ColorRes val colorResId: Int,
  val isLightButton: Boolean
)
