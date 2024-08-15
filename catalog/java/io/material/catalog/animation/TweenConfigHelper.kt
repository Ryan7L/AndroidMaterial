package io.material.catalog.animation

import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.view.View
import android.widget.RadioGroup
import androidx.core.view.children
import androidx.transition.TransitionManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import io.material.catalog.R

private const val ANIM_TYPE_ALPHA = 0
private const val ANIM_TYPE_SCALE = 1
private const val ANIM_TYPE_TRANSLATE = 2
private const val ANIM_TYPE_ROTATE = 3
private const val ANIM_TYPE_COMBO = 4

private const val REPEAT_MODEL_INFINITE = 0
private const val REPEAT_MODEL_RESTART = 1
private const val REPEAT_MODEL_REVERSE = 2

private const val CREATE_TYPE_XML = 0
private const val CREATE_TYPE_CODE = 1

private const val INTERPOLATOR_LINEAR = 0
private const val INTERPOLATOR_ACCELERATE = 1
private const val INTERPOLATOR_ACCELERATE_DECELERATE = 2
private const val INTERPOLATOR_DECELERATE = 3
private const val INTERPOLATOR_ANTICIPATE = 4
private const val INTERPOLATOR_ANTICIPATE_OVERSHOOT = 5
private const val INTERPOLATOR_OVERSHOOT = 6
private const val INTERPOLATOR_BOUNCE = 7
private const val INTERPOLATOR_CYCLE = 8
private const val INTERPOLATOR_CUSTOM = 9

private var defaultConfig =
  Config(ANIM_TYPE_ALPHA, REPEAT_MODEL_RESTART, 1, CREATE_TYPE_XML, INTERPOLATOR_LINEAR)

class TweenConfigHelper(private val context: Context) {
  private val dialog: BottomSheetDialog = BottomSheetDialog(context).apply {
    setContentView(R.layout.bottom_sheet_layout)
    initAnimTypeSelector(this)
    initAnimRepeatModelSelector(this)
    initAnimRepeatCountSelector(this)
    initCreateTypeSelector(this)
    initInterpolatorSelector(this)
  }
  private var animType: Int = defaultConfig.animType
  private var repeatModel: Int = defaultConfig.repeatModel
  private var repeatCount: Int = defaultConfig.repeatCount
  private var createType: Int = defaultConfig.createType
  private var interpolator: Int = defaultConfig.interpolator

  private lateinit var xmlBtn: MaterialButton

  fun openConfigDialog(onDismissListener: OnDismissListener) {
    dialog.setOnDismissListener(onDismissListener)
    dialog.show()
  }

  private fun initAnimTypeSelector(dialog: BottomSheetDialog) {
    val alphaBtn = dialog.findViewById<MaterialButton>(R.id.alphaBtn)
    val scaleBtn = dialog.findViewById<MaterialButton>(R.id.scaleBtn)
    val translateBtn = dialog.findViewById<MaterialButton>(R.id.translateBtn)
    val rotateBtn = dialog.findViewById<MaterialButton>(R.id.rotateBtn)
    val comboBtn = dialog.findViewById<MaterialButton>(R.id.comboBtn)
    alphaBtn?.isChecked = true
  }

  private fun initAnimRepeatModelSelector(dialog: BottomSheetDialog) {
    val infiniteBtn = dialog.findViewById<MaterialButton>(R.id.infiniteBtn)
    val restartBtn = dialog.findViewById<MaterialButton>(R.id.restartBtn)
    val reverseBtn = dialog.findViewById<MaterialButton>(R.id.reverseBtn)
    restartBtn?.isChecked = true
  }

  private fun initAnimRepeatCountSelector(dialog: BottomSheetDialog) {
    val slider = dialog.findViewById<Slider>(R.id.slider)
    slider?.addOnChangeListener { _, value, _ ->
      repeatCount = value.toInt()
    }
  }

  private fun initCreateTypeSelector(dialog: BottomSheetDialog) {
    xmlBtn = dialog.findViewById(R.id.xmlBtn)!!
    dialog.findViewById<MaterialButton>(R.id.codeBtn)?.setOnClickListener {
      enableInterpolatorSelector(this.dialog)
    }
    xmlBtn.setOnClickListener {
      enableInterpolatorSelector(this.dialog)
    }
    xmlBtn.isChecked = true
  }

  private fun initInterpolatorSelector(dialog: BottomSheetDialog) {
    val interpolatorGroup = dialog.findViewById<RadioGroup>(R.id.interpolationGroup)
    val placeholder = dialog.findViewById<MaterialButton>(R.id.placeholder)
    placeholder?.setOnClickListener {
      placeholder.visibility = View.GONE
      interpolatorGroup?.children?.forEach {
        it.visibility = View.VISIBLE
      }
    }
  }

  private fun enableInterpolatorSelector(dialog: BottomSheetDialog) {

  }

  val config: Config
    get() = Config(animType, repeatModel, repeatCount, createType, interpolator)
}

data class Config(
  val animType: Int,
  val repeatModel: Int,
  val repeatCount: Int,
  val createType: Int,
  val interpolator: Int
)
