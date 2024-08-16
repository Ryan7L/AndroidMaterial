package io.material.catalog.animation

import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import androidx.core.view.children
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.material.catalog.R
import io.material.catalog.databinding.AnimSharedConfigBinding
import io.material.catalog.databinding.BottomSheetLayoutBinding
import io.material.catalog.databinding.ConfigAnimTypeLayoutBinding
import io.material.catalog.databinding.ConfigCreateTypeLayoutBinding
import io.material.catalog.databinding.ConfigDurationLayoutBinding
import io.material.catalog.databinding.ConfigRepeatCountLayoutBinding
import io.material.catalog.databinding.ConfigRepeatModelLayoutBinding

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
  private var binding: BottomSheetLayoutBinding
  private var createTypeBinding: ConfigCreateTypeLayoutBinding
  private var animTypeBinding: ConfigAnimTypeLayoutBinding
  private var sharedConfigBinding: AnimSharedConfigBinding
  private var repeatModelBinding: ConfigRepeatModelLayoutBinding
  private var repeatCountBinding: ConfigRepeatCountLayoutBinding
  private var durationBinding: ConfigDurationLayoutBinding
  private val dialog: BottomSheetDialog = BottomSheetDialog(context).apply {
    binding = BottomSheetLayoutBinding.inflate(layoutInflater)
    createTypeBinding = ConfigCreateTypeLayoutBinding.bind(binding.root)
    animTypeBinding = ConfigAnimTypeLayoutBinding.bind(binding.root)
    sharedConfigBinding = AnimSharedConfigBinding.bind(binding.root)
    repeatModelBinding = ConfigRepeatModelLayoutBinding.bind(sharedConfigBinding.root)
    repeatCountBinding = ConfigRepeatCountLayoutBinding.bind(sharedConfigBinding.root)
    durationBinding = ConfigDurationLayoutBinding.bind(sharedConfigBinding.root)
    setContentView(binding.root)
    initCreateType()
    initAnimType()
    initRepeatModel()
    initRepeatCount()
    initDuration()
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

  private fun initCreateType() {
    createTypeBinding.xmlBtn.setOnClickListener {
      isXml = true
    }
    createTypeBinding.codeBtn.setOnClickListener {
      isXml = false
    }
  }

  private fun initAnimType() {
    val group = animTypeBinding.typeGroup

    //重置item宽度
    val childrenCount = group.childCount
    val displayWidth = context.resources.displayMetrics.widthPixels
    val marginParent =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, context.resources.displayMetrics)
    val itemWidth = (displayWidth - 2 * marginParent) / childrenCount
    group.children.forEach {
      it.layoutParams = it.layoutParams.apply {
        width = itemWidth.toInt()
      }
    }
//    group.addOnButtonCheckedListener { group, checkedId, isChecked ->
//      when (checkedId) {
//        R.id.type_alpha -> animType = ANIM_TYPE_ALPHA
//        R.id.type_scale -> animType = ANIM_TYPE_SCALE
//        R.id.type_translate -> animType = ANIM_TYPE_TRANSLATE
//        R.id.type_rotate -> animType = ANIM_TYPE_ROTATE
//        R.id.type_set -> animType = ANIM_TYPE_COMBO
//      }
//    }
    animTypeBinding.typeAlpha.setOnClickListener {
      animType = ANIM_TYPE_ALPHA
    }
    animTypeBinding.typeScale.setOnClickListener {
      animType = ANIM_TYPE_SCALE
    }
    animTypeBinding.typeTranslate.setOnClickListener {
      animType = ANIM_TYPE_TRANSLATE
    }
    animTypeBinding.typeRotate.setOnClickListener {
      animType = ANIM_TYPE_ROTATE
    }
    animTypeBinding.typeSet.setOnClickListener {
      animType = ANIM_TYPE_COMBO
    }
  }

  private fun initRepeatModel() {
    repeatModelBinding.repeatRestart.setOnClickListener {
      repeatModel = REPEAT_MODEL_RESTART
    }
    repeatModelBinding.repeatReverse.setOnClickListener {
      repeatModel = REPEAT_MODEL_REVERSE
    }
  }

  private fun initRepeatCount() {
    val slider = repeatCountBinding.repeatSlider
    val inputLayout = repeatCountBinding.repeatInputLayout
    val infinite = repeatCountBinding.repeatInfinite
    val custom = repeatCountBinding.repeatCustom
    repeatCountBinding.repeatCountGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
      Log.d("TAG", "initRepeatCount: ")
    }
    slider.addOnChangeListener { _, value, _ ->
      repeatCount = value.toInt()
    }
    infinite.setOnClickListener {
      slider.isEnabled = false
      slider.value = 0f
      inputLayout.visibility = View.GONE
      repeatCount = -1
    }
    custom.setOnClickListener {
      slider.isEnabled = false
      slider.value = 0f
      inputLayout.visibility = View.VISIBLE
    }
  }

  private fun initDuration() {
    val slider = durationBinding.durationSlider
    val little = durationBinding.durationLevelLittle
    val medium = durationBinding.durationLevelMedium
    val custom = durationBinding.durationLevelCustom
    val inputLayout = durationBinding.durationInputLayout
    slider.addOnChangeListener { slider, value, fromUser ->
      duration = value.toLong()
    }
    little.setOnClickListener {
      inputLayout.visibility = View.GONE
      slider.stepSize = 50f
      slider.valueTo = 1000f
      slider.valueFrom = 100f
      slider.value = 100f
      slider.isEnabled = true
    }
    medium.setOnClickListener {
      slider.stepSize = 100f
      slider.valueTo = 5000f
      slider.valueFrom = 1000f
      slider.value = 1000f
      inputLayout.visibility = View.GONE
      slider.isEnabled = true
    }
    custom.bindClick {
      slider.stepSize = 100f
      slider.valueTo = 5000f
      slider.valueFrom = 1000f
      slider.value = 1000f
      slider.isEnabled = false
      inputLayout.visibility = View.VISIBLE
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

fun View.bindClick(onClickListener: (View) -> Unit) {
  setOnClickListener {
    onClickListener(it)
  }
}
