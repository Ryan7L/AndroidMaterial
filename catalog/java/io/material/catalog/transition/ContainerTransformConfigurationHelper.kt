//package io.material.catalog.transition
//
//import android.content.Context
//import android.content.DialogInterface
//import android.util.SparseIntArray
//import android.view.animation.Interpolator
//import com.google.android.material.bottomsheet.BottomSheetDialog
//import com.google.android.material.transition.MaterialContainerTransform
//import io.material.catalog.R
//
//private val FADE_MODE_MAP = SparseIntArray().apply {
//  append(R.id.fade_in_button, MaterialContainerTransform.FADE_MODE_IN)
//  append(R.id.fade_out_button, MaterialContainerTransform.FADE_MODE_OUT)
//  append(R.id.fade_cross_button, MaterialContainerTransform.FADE_MODE_CROSS)
//  append(R.id.fade_through_button, MaterialContainerTransform.FADE_MODE_THROUGH)
//}
//
///**
// * 管理 [TransitionContainerTransformDemoFragment] 中提供的所有配置 UI 的帮助程序类。
// */
//class ContainerTransformConfigurationHelper {
//  companion object {
//    private const val CUBIC_CONTROL_FORMAT = "%.3f"
//    private const val DURATION_FORMAT = "%.0f"
//    private const val NO_DURATION = -1L
//  }
//  private var arcMotionEnabled = false
//  private var enterDuration = NO_DURATION
//  private var returnDuration = NO_DURATION
//  private var interpolator: Interpolator? = null
//  private var fadeModeButtonId = R.id.fade_in_button
//  private var drawDebugEnabled = false
//
//  fun showConfigurationChooser(context: Context, onDismissListener: DialogInterface.OnDismissListener?) {
//    BottomSheetDialog(context).run {
//      setContentView(createConfigurationBottomSheetView(context,this))
//    }
//  }
//}
