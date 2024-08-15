package io.material.catalog.animation

import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.view.View
import android.view.animation.Animation
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout
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
    initCreateType(this)
    initAnimType(this)
    initRepeatModel(this)
    initRepeatCount(this)
    initDuration(this)
  }
  private var animType: Int = defaultConfig.animType
  private var repeatModel: Int = defaultConfig.repeatModel
  private var repeatCount: Int = defaultConfig.repeatCount
  private var createType: Int = defaultConfig.createType
  private var interpolator: Int = defaultConfig.interpolator
  private var isXml = true
  private var duration: Long = 0
  fun openConfigDialog(onDismissListener: OnDismissListener?) {
    dialog.setOnDismissListener(onDismissListener)
    dialog.show()
  }

  private fun initCreateType(dialog: BottomSheetDialog) {
    dialog.findViewById<MaterialButton>(R.id.xmlBtn)?.setOnClickListener {
      isXml = true
    }
    dialog.findViewById<MaterialButton>(R.id.codeBtn)?.setOnClickListener {
      isXml = false
    }
  }

  private fun initAnimType(dialog: BottomSheetDialog) {
    dialog.findViewById<MaterialButton>(R.id.type_alpha)?.setOnClickListener {
      animType = ANIM_TYPE_ALPHA
    }
    dialog.findViewById<MaterialButton>(R.id.type_scale)?.setOnClickListener {
      animType = ANIM_TYPE_SCALE
    }
    dialog.findViewById<MaterialButton>(R.id.type_translate)?.setOnClickListener {
      animType = ANIM_TYPE_TRANSLATE
    }
    dialog.findViewById<MaterialButton>(R.id.rotateBtn)?.setOnClickListener {
      animType = ANIM_TYPE_ROTATE
    }
  }

  private fun initRepeatModel(dialog: BottomSheetDialog) {
    dialog.findViewById<MaterialButton>(R.id.repeat_restart)?.setOnClickListener {
      repeatModel = REPEAT_MODEL_RESTART
    }
    dialog.findViewById<MaterialButton>(R.id.repeat_reverse)?.setOnClickListener {
      repeatModel = REPEAT_MODEL_REVERSE
    }
  }

  private fun initRepeatCount(dialog: BottomSheetDialog) {
    val slider = dialog.findViewById<Slider>(R.id.repeat_slider)
    val inputLayout = dialog.findViewById<TextInputLayout>(R.id.repeat_input_layout)
    val infinite = dialog.findViewById<MaterialButton>(R.id.repeat_infinite)
    val custom = dialog.findViewById<MaterialButton>(R.id.repeat_custom)
    slider?.setOnClickListener {
      inputLayout?.visibility = View.GONE
      infinite?.isChecked = false
      custom?.isChecked = false
    }
    slider?.addOnChangeListener { _, value, _ ->
      repeatCount = value.toInt()
    }
    infinite?.setOnClickListener {
      inputLayout?.visibility = View.GONE
      repeatCount = -1
    }
    custom?.setOnClickListener {
      slider?.value = 0f
      inputLayout?.visibility = View.VISIBLE
    }
  }

  private fun initDuration(dialog: BottomSheetDialog) {
    val slider = dialog.findViewById<Slider>(R.id.duration_slider)
    val little = dialog.findViewById<MaterialButton>(R.id.duration_level_little)
    val medium = dialog.findViewById<MaterialButton>(R.id.duration_level_medium)
    val custom = dialog.findViewById<MaterialButton>(R.id.duration_level_custom)
    val inputLayout = dialog.findViewById<TextInputLayout>(R.id.duration_input_layout)
    slider?.setOnClickListener {
      inputLayout?.visibility = View.GONE
    }
    slider?.addOnChangeListener { slider, value, fromUser ->
      duration = value.toLong()
    }
    little?.setOnClickListener {
      inputLayout?.visibility = View.GONE
    }
    medium?.setOnClickListener {
      inputLayout?.visibility = View.GONE
    }
    custom?.setOnClickListener {
      inputLayout?.visibility = View.GONE
    }
  }

  val config: Config
    get() = Config(animType, repeatModel, repeatCount, createType, interpolator)

  val animation: Animation
    get() {
      TODO()
    }
}

data class Config(
  val animType: Int,
  val repeatModel: Int,
  val repeatCount: Int,
  val createType: Int,
  val interpolator: Int
)

data class SharedConfig(
  val duration: Long,
  val fillEnabled: Boolean,
  val fillBefore: Boolean,
  val fillAfter: Boolean,
  val zAdjustment: Int,
  val repeatCount: Int,
  val repeatModel: Int,
  val startOffset: Long,
  val interpolator: Int
)

abstract class BaseAnimationConfig()
data class AlphaConfig(val fromAlpha: Float, val toAlpha: Float) : BaseAnimationConfig()
data class ScaleConfig(
  val fromX: Float,
  val fromY: Float,
  val toX: Float,
  val toY: Float,
  val pivotX: Float,
  val pivotY: Float
) : BaseAnimationConfig()

data class RotateConfig(
  val fromDegrees: Float, val toDegrees: Float, val pivotX: Float, val pivotY: Float
) : BaseAnimationConfig()

data class TranslateConfig(val fromX: Float, val fromY: Float, val toX: Float, val toY: Float) :
  BaseAnimationConfig()

data class BaseConfig(
  val animationType: Int = ANIM_TYPE_ALPHA, val createType: Int = CREATE_TYPE_XML
)

data class AnimationConfig(
  val baseConfig: BaseConfig = BaseConfig(),
  val animConfig: BaseAnimationConfig?,
  val sharedConfig: SharedConfig? = null
)
