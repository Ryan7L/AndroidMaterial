package io.material.catalog.feature

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment


/**
 * demo 或组件已就绪
 */
const val STATUS_READY = 0

/**
 * demo 或 组件正在运行中
 */
const val STATUS_WIP = 1

/**
 * 此FeatureDemo的状态标志。
 */
@IntDef(STATUS_READY, STATUS_WIP)
@Retention(AnnotationRetention.SOURCE)
annotation class Status()

abstract class FeatureDemo @JvmOverloads constructor(
  @StringRes val titleResId: Int,
  @DrawableRes val drawableResId: Int,
  @Status val status: Int = STATUS_READY
) {
  companion object {
    @JvmField
    val KEY_FAVORITE_LAUNCH = "KEY_FAVORITE_LAUNCH"
  }

  abstract val landingFragment: Fragment


}
