package io.material.catalog.color

import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.IdRes

class ColorHarmonizationGridRowData private constructor(
  @IdRes val leftLayoutId: Int,
  @IdRes val rightLayoutId: Int,
  @ArrayRes val colorNameIds: Int,
) {
  @ColorRes var colorResId: Int = 0
  var colorAttributeResIds: IntArray = intArrayOf()
  constructor(leftLayoutId: Int, rightLayoutId: Int, @ColorRes colorResId: Int, @ArrayRes colorNameIds: Int) : this(leftLayoutId, rightLayoutId, colorNameIds){
    this.colorResId = colorResId
  }
  constructor(leftLayoutId: Int, rightLayoutId: Int, colorAttributeResIds: IntArray, @ArrayRes colorNameIds: Int) : this(leftLayoutId, rightLayoutId,colorNameIds){
    this.colorAttributeResIds = colorAttributeResIds
  }
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as ColorHarmonizationGridRowData

    if (leftLayoutId != other.leftLayoutId) return false
    if (rightLayoutId != other.rightLayoutId) return false
    if (colorResId != other.colorResId) return false
    if (colorNameIds != other.colorNameIds) return false
    if (!colorAttributeResIds.contentEquals(other.colorAttributeResIds)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = leftLayoutId
    result = 31 * result + rightLayoutId
    result = 31 * result + colorResId
    result = 31 * result + colorNameIds
    result = 31 * result + colorAttributeResIds.contentHashCode()
    return result
  }
}
