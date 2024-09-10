package io.material.catalog.transition

import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.text.Editable
import android.text.TextWatcher
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.slider.Slider
import com.google.android.material.slider.Slider.OnChangeListener
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialArcMotion
import io.material.catalog.R

class ContainerTransformConfigurationHelper {
  companion object {
    private const val CUBIC_CONTROL_FORMAT = "%.3f"
    private const val DURATION_FORMAT = "%.0f"
    private const val NO_DURATION = -1L
    private val FADE_MODE_MAP = SparseIntArray().apply {
      append(R.id.fade_in_button, MaterialContainerTransform.FADE_MODE_IN)
      append(R.id.fade_out_button, MaterialContainerTransform.FADE_MODE_OUT)
      append(R.id.fade_cross_button, MaterialContainerTransform.FADE_MODE_CROSS)
      append(R.id.fade_through_button, MaterialContainerTransform.FADE_MODE_THROUGH)
    }

    /**
     * 一个定制的过冲插值器，暴露了它的张力
     */
    private class CustomOvershootInterpolator @JvmOverloads constructor(val tension: Float = DEFAULT_TENSION) :
      OvershootInterpolator(tension) {
      companion object {
        val DEFAULT_TENSION = 2f
      }
    }

    /**
     * 自定义预期超调插值器会暴露其张力。
     */
    private class CustomAnticipateOvershootInterpolator @JvmOverloads constructor(val tension: Float = DEFAULT_TENSION) :
      AnticipateOvershootInterpolator(tension) {
      companion object {
        val DEFAULT_TENSION = 2f
      }
    }

    /**
     * 自定义三次贝塞尔曲线插值器，暴露其控制点
     */
    private class CustomCubicBezier(
      val controlX1: Float,
      val controlY1: Float,
      val controlX2: Float,
      val controlY2: Float
    ) : Interpolator {
      private val interpolator =
        PathInterpolatorCompat.create(controlX1, controlY1, controlX2, controlY2)

      override fun getInterpolation(input: Float): Float {
        return interpolator.getInterpolation(input)
      }

      fun getDescription(context: Context) = context.getString(
        R.string.cat_transition_config_custom_interpolator_desc,
        controlX1,
        controlY1,
        controlX2,
        controlY2
      )
    }

    private fun getTextFloat(editText: EditText?): Float? {
      return try {
        editText?.text?.toString()?.toFloat()
      } catch (e: Exception) {
        null
      }
    }

    private fun setTextFloat(editText: EditText, value: Float) {
      editText.setText(String.format(CUBIC_CONTROL_FORMAT, value))
    }

    private fun setTextInputLayoutError(layout: TextInputLayout) {
      layout.error = " "
    }

    private fun isValidCubicBezierControlValue(value: Float?): Boolean {
      return value != null && value in 0f..1f
    }

    private fun areValidCubicBezierControls(
      view: View,
      x1: Float?,
      y1: Float?,
      x2: Float?,
      y2: Float?
    ): Boolean {
      var isValid = true
      if (!isValidCubicBezierControlValue(x1)) {
        isValid = false
        setTextInputLayoutError(view.findViewById(R.id.x1_text_input_layout))
      }
      if (!isValidCubicBezierControlValue(y1)) {
        isValid = false
        setTextInputLayoutError(view.findViewById(R.id.y1_text_input_layout))
      }
      if (!isValidCubicBezierControlValue(x2)) {
        isValid = false
        setTextInputLayoutError(view.findViewById(R.id.x2_text_input_layout))
      }
      if (!isValidCubicBezierControlValue(y2)) {
        isValid = false
        setTextInputLayoutError(view.findViewById(R.id.y2_text_input_layout))
      }
      return isValid
    }

    fun TextInputLayout.textInputClearOnTextChanged() {
      editText?.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
          error = null
        }
      })
    }

    private fun updateCustomTextFieldsVisibility(
      checkedId: Int,
      overshootTensionTextInputLayout: TextInputLayout,
      anticipateOvershootTensionTextInputLayout: TextInputLayout,
      customContainer: ViewGroup
    ) {
      overshootTensionTextInputLayout.visibility =
        if (checkedId == R.id.radio_overshoot) View.VISIBLE else View.GONE
      anticipateOvershootTensionTextInputLayout.visibility =
        if (checkedId == R.id.radio_anticipate_overshoot) View.VISIBLE else View.GONE
      customContainer.visibility = if (checkedId == R.id.radio_custom) View.VISIBLE else View.GONE
    }

  }

  var arcMotionEnabled = false
  var enterDuration = NO_DURATION
  var returnDuration = NO_DURATION
  var interpolator: Interpolator? = null
  var fadeModeButtonId = R.id.fade_in_button
  var drawDebugEnabled = false
  val fadeMode: Int
    get() = FADE_MODE_MAP[fadeModeButtonId]

  fun showConfigurationChooser(context: Context, onDismissListener: OnDismissListener?) {
    BottomSheetDialog(context).run {
      setContentView(createConfigurationBottomSheetView(context, this))
      setOnDismissListener(onDismissListener)
      show()
    }
  }

  fun configure(transform: MaterialContainerTransform, entering: Boolean) {
    val duration = if (entering) enterDuration else returnDuration
    if (duration != NO_DURATION) {
      transform.duration = duration
    }
    interpolator?.let {
      transform.interpolator = it
    }
    if (arcMotionEnabled) {
      transform.setPathMotion(com.google.android.material.transition.MaterialArcMotion())
    }
    transform.fadeMode = fadeMode
    transform.isDrawDebugEnabled = drawDebugEnabled
  }

  fun configure(
    transform: com.google.android.material.transition.platform.MaterialContainerTransform,
    entering: Boolean
  ) {
    val duration = if (entering) enterDuration else returnDuration
    if (duration != NO_DURATION) {
      transform.duration = duration
    }
    interpolator?.let {
      transform.interpolator = it
    }
    if (arcMotionEnabled) {
      transform.pathMotion = MaterialArcMotion()
    }
    transform.fadeMode = fadeMode
    transform.isDrawDebugEnabled = drawDebugEnabled
  }

  private fun resetDefaultValues() {
    arcMotionEnabled = false
    enterDuration = NO_DURATION
    returnDuration = NO_DURATION
    interpolator = null
    fadeModeButtonId = R.id.fade_in_button
    drawDebugEnabled = false
  }

  private fun createConfigurationBottomSheetView(
    context: Context,
    dialog: BottomSheetDialog
  ): View {
    val layout =
      LayoutInflater.from(context).inflate(R.layout.cat_transition_configuration_layout, null)
    setUpBottomSheetPathMotionButtonGroup(layout)
    setUpBottomSheetEnterDurationSlider(layout)
    setUpBottomSheetReturnDurationSlider(layout)
    setUpBottomSheetInterpolation(layout)
    setUpBottomSheetFadeModeButtonGroup(layout)
    setUpBottomSheetDebugging(layout)
    setUpBottomSheetConfirmationButtons(layout, dialog)
    return layout
  }

  private fun setUpBottomSheetPathMotionButtonGroup(view: View) {
    view.findViewById<MaterialButtonToggleGroup>(R.id.path_motion_button_group)?.let {
      it.check(if (arcMotionEnabled) R.id.arc_motion_button else R.id.linear_motion_button)
      it.addOnButtonCheckedListener { group, checkedId, isChecked ->
        if (checkedId == R.id.arc_motion_button) {
          arcMotionEnabled = isChecked
        }
      }
    }
  }

  private fun setUpBottomSheetFadeModeButtonGroup(view: View) {
    view.findViewById<MaterialButtonToggleGroup>(R.id.fade_mode_button_group)?.let {
      it.check(fadeModeButtonId)
      it.addOnButtonCheckedListener { group, checkedId, isChecked ->
        if (isChecked) {
          fadeModeButtonId = checkedId
        }
      }
    }
  }

  private fun setUpBottomSheetReturnDurationSlider(view: View) {
    setUpBottomSheetDurationSlider(
      view, R.id.return_duration_slider,
      R.id.return_duration_value,
      returnDuration
    ) { _, value, _ ->
      returnDuration = value.toLong()
    }
  }

  private fun setUpBottomSheetEnterDurationSlider(view: View) {
    setUpBottomSheetDurationSlider(
      view, R.id.enter_duration_slider,
      R.id.enter_duration_value,
      enterDuration
    ) { _, value, _ ->
      enterDuration = value.toLong()
    }
  }

  private fun setUpBottomSheetDurationSlider(
    view: View,
    sliderResId: Int,
    labelResId: Int,
    duration: Long,
    listener: OnChangeListener
  ) {
    val durationSlider = view.findViewById<Slider>(sliderResId)
    val durationTv = view.findViewById<TextView>(labelResId)
    if (durationSlider != null && durationTv != null) {
      durationSlider.value = if (duration != NO_DURATION) duration.toFloat() else 0f
      durationTv.text = String.format(DURATION_FORMAT, durationSlider.value)
      durationSlider.addOnChangeListener { slider, value, fromUser ->
        listener.onValueChange(slider, value, fromUser)
        durationTv.text = String.format(DURATION_FORMAT, value)
      }
    }
  }

  /**
   * 设置插值器
   */
  private fun setUpBottomSheetInterpolation(view: View) {
    val interpolationGroup = view.findViewById<RadioGroup>(R.id.interpolation_radio_group)
    val customContainer = view.findViewById<ViewGroup>(R.id.custom_curve_container)
    val overshootTensionTextInputLayout =
      view.findViewById<TextInputLayout>(R.id.overshoot_tension_text_input_layout)
    val anticipateOvershootTensionTextInputLayout =
      view.findViewById<TextInputLayout>(R.id.anticipate_overshoot_tension_text_input_layout)
    val overshootTensionEditText = view.findViewById<EditText>(R.id.overshoot_tension_edit_text)
    val anticipateOvershootTensionEditText =
      view.findViewById<EditText>(R.id.anticipate_overshoot_tension_edit_text)

    if (interpolationGroup != null && customContainer != null) {
      view.findViewById<TextInputLayout>(R.id.x1_text_input_layout).textInputClearOnTextChanged()
      view.findViewById<TextInputLayout>(R.id.y1_text_input_layout).textInputClearOnTextChanged()
      view.findViewById<TextInputLayout>(R.id.x2_text_input_layout).textInputClearOnTextChanged()
      view.findViewById<TextInputLayout>(R.id.y2_text_input_layout).textInputClearOnTextChanged()
      overshootTensionEditText.setText(CustomOvershootInterpolator.DEFAULT_TENSION.toString())
      anticipateOvershootTensionEditText.setText(CustomAnticipateOvershootInterpolator.DEFAULT_TENSION.toString())
      //检查正确的当前单选按钮并填写自定义贝塞尔曲线字段（如果适用）
      when (interpolator) {
        is FastOutSlowInInterpolator -> {
          interpolationGroup.check(R.id.radio_fast_out_slow_in)
        }

        is OvershootInterpolator -> {
          interpolationGroup.check(R.id.radio_overshoot)
          if (interpolator is CustomOvershootInterpolator) {
            overshootTensionEditText.setText((interpolator as CustomOvershootInterpolator).tension.toString())
          }
        }

        is AnticipateOvershootInterpolator -> {
          interpolationGroup.check(R.id.radio_anticipate_overshoot)
          if (interpolator is CustomAnticipateOvershootInterpolator) {
            anticipateOvershootTensionEditText.setText((interpolator as CustomAnticipateOvershootInterpolator).tension.toString())
          }
        }

        is BounceInterpolator -> interpolationGroup.check(R.id.radio_bounce)
        is CustomCubicBezier -> {
          interpolationGroup.check(R.id.radio_custom)

          setTextFloat(
            view.findViewById(R.id.x1_edit_text),
            (interpolator as CustomCubicBezier).controlX1
          )
          setTextFloat(
            view.findViewById(R.id.y1_edit_text),
            (interpolator as CustomCubicBezier).controlY1
          )
          setTextFloat(
            view.findViewById(R.id.x2_edit_text),
            (interpolator as CustomCubicBezier).controlX2
          )
          setTextFloat(
            view.findViewById(R.id.y2_edit_text),
            (interpolator as CustomCubicBezier).controlY2
          )
        }

        else -> interpolationGroup.check(R.id.radio_default)
      }

      //根据初始选中的单选按钮显示隐藏自定义文本输入字段
      updateCustomTextFieldsVisibility(
        interpolationGroup.checkedRadioButtonId,
        overshootTensionTextInputLayout,
        anticipateOvershootTensionTextInputLayout,
        customContainer
      )
      //监视所选单选按钮的任何更改并更新自定义文本字段的可见性。应用配置时将捕获自定义文本字段值。
      interpolationGroup.setOnCheckedChangeListener { group, checkedId ->
        updateCustomTextFieldsVisibility(
          checkedId,
          overshootTensionTextInputLayout,
          anticipateOvershootTensionTextInputLayout,
          customContainer
        )
      }
    }
  }

  /**
   * 设置是否绘制debug 辅助线
   * @param view View
   */
  private fun setUpBottomSheetDebugging(view: View) {
    val debugCheckBox = view.findViewById<CheckBox>(R.id.draw_debug_checkbox)
    debugCheckBox?.let {
      it.isChecked = drawDebugEnabled
      it.setOnCheckedChangeListener { _, isChecked ->
        drawDebugEnabled = isChecked
      }
    }
  }

  /**
   * 设置按钮以应用和验证配置值并关闭底部工作表
   */
  private fun setUpBottomSheetConfirmationButtons(view: View, dialog: BottomSheetDialog) {
    view.findViewById<View>(R.id.apply_button).setOnClickListener {
      val interpolationGroup = view.findViewById<RadioGroup>(R.id.interpolation_radio_group)
      when (interpolationGroup.checkedRadioButtonId) {
        R.id.radio_custom -> {
          val x1 = getTextFloat(view.findViewById(R.id.x1_edit_text))
          val x2 = getTextFloat(view.findViewById(R.id.x2_edit_text))
          val y1 = getTextFloat(view.findViewById(R.id.y1_edit_text))
          val y2 = getTextFloat(view.findViewById(R.id.y2_edit_text))
          if (areValidCubicBezierControls(view, x1, y1, x2, y2)) {
            interpolator = CustomCubicBezier(x1!!, y1!!, x2!!, y2!!)
            dialog.dismiss()
          }
        }

        R.id.radio_overshoot -> {
          val overshootTensionEditText =
            view.findViewById<EditText>(R.id.overshoot_tension_edit_text)
          val tension = getTextFloat(overshootTensionEditText)
          interpolator =
            tension?.let { CustomOvershootInterpolator(it) } ?: CustomOvershootInterpolator()
          dialog.dismiss()
        }

        R.id.radio_anticipate_overshoot -> {
          val anticipateOvershootTensionEditText =
            view.findViewById<EditText>(R.id.anticipate_overshoot_tension_edit_text)
          val tension = getTextFloat(anticipateOvershootTensionEditText)
          interpolator = tension?.let { CustomAnticipateOvershootInterpolator(it) }
            ?: CustomAnticipateOvershootInterpolator()
          dialog.dismiss()
        }

        R.id.radio_bounce -> {
          interpolator = BounceInterpolator()
          dialog.dismiss()
        }

        R.id.radio_fast_out_slow_in -> {
          interpolator = FastOutSlowInInterpolator()
          dialog.dismiss()
        }

        else -> {
          interpolator = null
          dialog.dismiss()
        }

      }
    }
    view.findViewById<View>(R.id.clear_button).setOnClickListener {
      resetDefaultValues()
      dialog.dismiss()
    }
  }
}

