/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.transition;

import io.material.catalog.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.os.Build.VERSION_CODES;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.animation.PathInterpolatorCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.slider.Slider;
import com.google.android.material.slider.Slider.OnChangeListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;

/**
 * A helper class which manages all configuration UI presented in {@link
 * TransitionContainerTransformDemoFragment}.
 */
public class ContainerTransformConfigurationHelper {

  private static final String CUBIC_CONTROL_FORMAT = "%.3f";
  private static final String DURATION_FORMAT = "%.0f";
  private static final long NO_DURATION = -1;

  private boolean arcMotionEnabled;
  private long enterDuration;
  private long returnDuration;
  private Interpolator interpolator;
  private int fadeModeButtonId;
  private boolean drawDebugEnabled;

  private static final SparseIntArray FADE_MODE_MAP = new SparseIntArray();

  static {
    FADE_MODE_MAP.append(R.id.fade_in_button, MaterialContainerTransform.FADE_MODE_IN);
    FADE_MODE_MAP.append(R.id.fade_out_button, MaterialContainerTransform.FADE_MODE_OUT);
    FADE_MODE_MAP.append(R.id.fade_cross_button, MaterialContainerTransform.FADE_MODE_CROSS);
    FADE_MODE_MAP.append(R.id.fade_through_button, MaterialContainerTransform.FADE_MODE_THROUGH);
  }

  public ContainerTransformConfigurationHelper() {
    setUpDefaultValues();
  }

  /**
   * 显示与来自 {@link TransitionContainerTransformDemoFragment} 的容器转换关联的配置选择器.
   */
  void showConfigurationChooser(Context context, @Nullable OnDismissListener onDismissListener) {
    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
    bottomSheetDialog.setContentView(
        createConfigurationBottomSheetView(context, bottomSheetDialog));
    bottomSheetDialog.setOnDismissListener(onDismissListener);
    bottomSheetDialog.show();
  }

  /** 根据配置助手的参数设置 androidx 转换. */
  void configure(MaterialContainerTransform transform, boolean entering) {
    long duration = entering ? getEnterDuration() : getReturnDuration();
    if (duration != NO_DURATION) {
      transform.setDuration(duration);
    }
    if (getInterpolator() != null) {
      transform.setInterpolator(getInterpolator());
    }
    if (isArcMotionEnabled()) {
      transform.setPathMotion(new MaterialArcMotion());
    }
    transform.setFadeMode(getFadeMode());
    transform.setDrawDebugEnabled(isDrawDebugEnabled());
  }

  /** 根据配置助手的参数设置平台转换。 */
  @RequiresApi(VERSION_CODES.LOLLIPOP)
  void configure(
      com.google.android.material.transition.platform.MaterialContainerTransform transform,
      boolean entering) {
    long duration = entering ? getEnterDuration() : getReturnDuration();
    if (duration != NO_DURATION) {
      transform.setDuration(duration);
    }
    if (getInterpolator() != null) {
      transform.setInterpolator(getInterpolator());
    }
    if (isArcMotionEnabled()) {
      transform.setPathMotion(
          new com.google.android.material.transition.platform.MaterialArcMotion());
    }
    transform.setFadeMode(getFadeMode());
    transform.setDrawDebugEnabled(isDrawDebugEnabled());
  }

  /**
   * 自定义容器转换是否应使用 {@link com.google.android.material.transition.MaterialArcMotion}.
   */
  boolean isArcMotionEnabled() {
    return arcMotionEnabled;
  }

  /** 自定义容器转换要使用的输入持续时间。 */
  long getEnterDuration() {
    return enterDuration;
  }

  /** 自定义容器转换使用的返回持续时间. */
  long getReturnDuration() {
    return returnDuration;
  }

  /** 自定义容器转换使用的插值器. */
  Interpolator getInterpolator() {
    return interpolator;
  }

  /** 自定义容器变换使用的淡入淡出模式。 */
  int getFadeMode() {
    return FADE_MODE_MAP.get(fadeModeButtonId);
  }

  /** 自定义转换是否应绘制调试线. */
  boolean isDrawDebugEnabled() {
    return drawDebugEnabled;
  }

  private void setUpDefaultValues() {
    arcMotionEnabled = false;
    enterDuration = NO_DURATION;
    returnDuration = NO_DURATION;
    interpolator = null;
    fadeModeButtonId = R.id.fade_in_button;
    drawDebugEnabled = false;
  }

  /** 创建一个底部工作表对话框，显示用于配置容器转换的控件。 */
  private View createConfigurationBottomSheetView(Context context, BottomSheetDialog dialog) {
    View layout =
        LayoutInflater.from(context).inflate(R.layout.cat_transition_configuration_layout, null);
    setUpBottomSheetPathMotionButtonGroup(layout);
    setUpBottomSheetEnterDurationSlider(layout);
    setUpBottomSheetReturnDurationSlider(layout);
    setUpBottomSheetInterpolation(layout);
    setUpBottomSheetFadeModeButtonGroup(layout);
    setUpBottomSheetDebugging(layout);
    setUpBottomSheetConfirmationButtons(layout, dialog);
    return layout;
  }

  /** 根据所选单选按钮更新是否使用弧线运动 */
  private void setUpBottomSheetPathMotionButtonGroup(View view) {
    MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.path_motion_button_group);
    if (toggleGroup != null) {
      // Set initial value.
      toggleGroup.check(arcMotionEnabled ? R.id.arc_motion_button : R.id.linear_motion_button);
      toggleGroup.addOnButtonCheckedListener(
          (group, checkedId, isChecked) -> {
            if (checkedId == R.id.arc_motion_button) {
              arcMotionEnabled = isChecked;
            }
          });
    }
  }

  /** 根据所选单选按钮更新淡入淡出模式 */
  private void setUpBottomSheetFadeModeButtonGroup(View view) {
    MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.fade_mode_button_group);
    if (toggleGroup != null) {
      // Set initial value.
      toggleGroup.check(fadeModeButtonId);
      toggleGroup.addOnButtonCheckedListener(
          (group, checkedId, isChecked) -> {
            if (isChecked) {
              fadeModeButtonId = checkedId;
            }
          });
    }
  }

  /** 当滑块值更改时更新输入持续时间和持续时间文本. */
  private void setUpBottomSheetEnterDurationSlider(View view) {
    setUpBottomSheetDurationSlider(
        view,
        R.id.enter_duration_slider,
        R.id.enter_duration_value,
        enterDuration,
        (slider, value, fromUser) -> enterDuration = (long) value);
  }

  /** 当滑块值更改时更新返回持续时间和持续时间文本. */
  private void setUpBottomSheetReturnDurationSlider(View view) {
    setUpBottomSheetDurationSlider(
        view,
        R.id.return_duration_slider,
        R.id.return_duration_value,
        returnDuration,
        (slider, value, fromUser) -> returnDuration = (long) value);
  }

  @SuppressLint("DefaultLocale")
  private void setUpBottomSheetDurationSlider(
      View view,
      @IdRes int sliderResId,
      @IdRes int labelResId,
      float duration,
      OnChangeListener listener) {
    Slider durationSlider = view.findViewById(sliderResId);
    TextView durationValue = view.findViewById(labelResId);
    if (durationSlider != null && durationValue != null) {
      // Set initial value.
      durationSlider.setValue(duration != NO_DURATION ? duration : 0);
      durationValue.setText(String.format(DURATION_FORMAT, durationSlider.getValue()));
      // Update the duration and durationValue's text whenever the slider is slid.
      durationSlider.addOnChangeListener(
          (slider, value, fromUser) -> {
            listener.onValueChange(slider, value, fromUser);
            durationValue.setText(String.format(DURATION_FORMAT, value));
          });
    }
  }

  /** 设置插值 */
  private void setUpBottomSheetInterpolation(View view) {
    RadioGroup interpolationGroup = view.findViewById(R.id.interpolation_radio_group);
    ViewGroup customContainer = view.findViewById(R.id.custom_curve_container);
    TextInputLayout overshootTensionTextInputLayout =
        view.findViewById(R.id.overshoot_tension_text_input_layout);
    EditText overshootTensionEditText = view.findViewById(R.id.overshoot_tension_edit_text);
    TextInputLayout anticipateOvershootTensionTextInputLayout =
        view.findViewById(R.id.anticipate_overshoot_tension_text_input_layout);
    EditText anticipateOvershootTensionEditText =
        view.findViewById(R.id.anticipate_overshoot_tension_edit_text);

    if (interpolationGroup != null && customContainer != null) {
      setTextInputClearOnTextChanged(view.findViewById(R.id.x1_text_input_layout));
      setTextInputClearOnTextChanged(view.findViewById(R.id.x2_text_input_layout));
      setTextInputClearOnTextChanged(view.findViewById(R.id.y1_text_input_layout));
      setTextInputClearOnTextChanged(view.findViewById(R.id.y2_text_input_layout));

      overshootTensionEditText.setText(String.valueOf(CustomOvershootInterpolator.DEFAULT_TENSION));
      anticipateOvershootTensionEditText.setText(
          String.valueOf(CustomAnticipateOvershootInterpolator.DEFAULT_TENSION));

      // Check the correct current radio button and fill in custom bezier fields if applicable.
      if (interpolator instanceof FastOutSlowInInterpolator) {
        interpolationGroup.check(R.id.radio_fast_out_slow_in);
      } else if (interpolator instanceof OvershootInterpolator) {
        interpolationGroup.check(R.id.radio_overshoot);
        if (interpolator instanceof CustomOvershootInterpolator) {
          CustomOvershootInterpolator customOvershootInterpolator =
              (CustomOvershootInterpolator) interpolator;
          overshootTensionEditText.setText(String.valueOf(customOvershootInterpolator.tension));
        }
      } else if (interpolator instanceof AnticipateOvershootInterpolator) {
        interpolationGroup.check(R.id.radio_anticipate_overshoot);
        if (interpolator instanceof CustomAnticipateOvershootInterpolator) {
          CustomAnticipateOvershootInterpolator customAnticipateOvershootInterpolator =
              (CustomAnticipateOvershootInterpolator) interpolator;
          anticipateOvershootTensionEditText.setText(
              String.valueOf(customAnticipateOvershootInterpolator.tension));
        }
      } else if (interpolator instanceof BounceInterpolator) {
        interpolationGroup.check(R.id.radio_bounce);
      } else if (interpolator instanceof CustomCubicBezier) {
        interpolationGroup.check(R.id.radio_custom);
        CustomCubicBezier currentInterp = (CustomCubicBezier) interpolator;
        setTextFloat(view.findViewById(R.id.x1_edit_text), currentInterp.controlX1);
        setTextFloat(view.findViewById(R.id.y1_edit_text), currentInterp.controlY1);
        setTextFloat(view.findViewById(R.id.x2_edit_text), currentInterp.controlX2);
        setTextFloat(view.findViewById(R.id.y2_edit_text), currentInterp.controlY2);
      } else {
        interpolationGroup.check(R.id.radio_default);
      }

      // Show/hide custom text input fields depending on initial checked radio button.
      updateCustomTextFieldsVisibility(
          interpolationGroup.getCheckedRadioButtonId(),
          overshootTensionTextInputLayout,
          anticipateOvershootTensionTextInputLayout,
          customContainer);

      // Watch for any changes to selected radio button and update custom text fields visibility.
      // The custom text field values will be captured when the configuration is applied.
      interpolationGroup.setOnCheckedChangeListener(
          (group, checkedId) ->
              updateCustomTextFieldsVisibility(
                  checkedId,
                  overshootTensionTextInputLayout,
                  anticipateOvershootTensionTextInputLayout,
                  customContainer));
    }
  }

  private static void updateCustomTextFieldsVisibility(
      int checkedId,
      TextInputLayout overshootTensionTextInputLayout,
      TextInputLayout anticipateOvershootTensionTextInputLayout,
      ViewGroup customContainer) {
    overshootTensionTextInputLayout.setVisibility(
        checkedId == R.id.radio_overshoot ? View.VISIBLE : View.GONE);
    anticipateOvershootTensionTextInputLayout.setVisibility(
        checkedId == R.id.radio_anticipate_overshoot ? View.VISIBLE : View.GONE);
    customContainer.setVisibility(checkedId == R.id.radio_custom ? View.VISIBLE : View.GONE);
  }

  @SuppressLint("DefaultLocale")
  private static void setTextFloat(EditText editText, float value) {
    editText.setText(String.format(CUBIC_CONTROL_FORMAT, value));
  }

  @Nullable
  private static Float getTextFloat(@Nullable EditText editText) {
    if (editText == null) {
      return null;
    }

    String text = editText.getText().toString();
    try {
      return Float.valueOf(text);
    } catch (Exception e) {
      return null;
    }
  }

  private static void setTextInputLayoutError(TextInputLayout layout) {
    layout.setError(" ");
  }

  private static void setTextInputClearOnTextChanged(TextInputLayout layout) {
    layout
        .getEditText()
        .addTextChangedListener(
            new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {
                layout.setError(null);
              }

              @Override
              public void afterTextChanged(Editable s) {}
            });
  }

  private static boolean isValidCubicBezierControlValue(@Nullable Float value) {
    return value != null && value >= 0 && value <= 1;
  }

  private static boolean areValidCubicBezierControls(
      View view, Float x1, Float y1, Float x2, Float y2) {
    boolean isValid = true;
    if (!isValidCubicBezierControlValue(x1)) {
      isValid = false;
      setTextInputLayoutError(view.findViewById(R.id.x1_text_input_layout));
    }
    if (!isValidCubicBezierControlValue(y1)) {
      isValid = false;
      setTextInputLayoutError(view.findViewById(R.id.y1_text_input_layout));
    }
    if (!isValidCubicBezierControlValue(x2)) {
      isValid = false;
      setTextInputLayoutError(view.findViewById(R.id.x2_text_input_layout));
    }
    if (!isValidCubicBezierControlValue(y2)) {
      isValid = false;
      setTextInputLayoutError(view.findViewById(R.id.y2_text_input_layout));
    }

    return isValid;
  }

  /** Set up whether or not to draw debugging paint */
  private void setUpBottomSheetDebugging(View view) {
    CheckBox debugCheckbox = view.findViewById(R.id.draw_debug_checkbox);
    if (debugCheckbox != null) {
      debugCheckbox.setChecked(drawDebugEnabled);
      debugCheckbox.setOnCheckedChangeListener(
          (buttonView, isChecked) -> drawDebugEnabled = isChecked);
    }
  }

  /** Set up buttons to apply and validate configuration values and dismiss the bottom sheet */
  private void setUpBottomSheetConfirmationButtons(View view, BottomSheetDialog dialog) {
    view.findViewById(R.id.apply_button)
        .setOnClickListener(
            v -> {
              // Capture and update interpolation
              RadioGroup interpolationGroup = view.findViewById(R.id.interpolation_radio_group);
              int checkedRadioButtonId = interpolationGroup.getCheckedRadioButtonId();
              if (checkedRadioButtonId == R.id.radio_custom) {
                Float x1 = getTextFloat(view.findViewById(R.id.x1_edit_text));
                Float y1 = getTextFloat(view.findViewById(R.id.y1_edit_text));
                Float x2 = getTextFloat(view.findViewById(R.id.x2_edit_text));
                Float y2 = getTextFloat(view.findViewById(R.id.y2_edit_text));

                if (areValidCubicBezierControls(view, x1, y1, x2, y2)) {
                  interpolator = new CustomCubicBezier(x1, y1, x2, y2);
                  dialog.dismiss();
                }
              } else if (checkedRadioButtonId == R.id.radio_overshoot) {
                EditText overshootTensionEditText =
                    view.findViewById(R.id.overshoot_tension_edit_text);
                Float tension = getTextFloat(overshootTensionEditText);
                interpolator =
                    tension != null
                        ? new CustomOvershootInterpolator(tension)
                        : new CustomOvershootInterpolator();
                dialog.dismiss();
              } else if (checkedRadioButtonId == R.id.radio_anticipate_overshoot) {
                EditText overshootTensionEditText =
                    view.findViewById(R.id.anticipate_overshoot_tension_edit_text);
                Float tension = getTextFloat(overshootTensionEditText);
                interpolator =
                    tension != null
                        ? new CustomAnticipateOvershootInterpolator(tension)
                        : new CustomAnticipateOvershootInterpolator();
                dialog.dismiss();
              } else if (checkedRadioButtonId == R.id.radio_bounce) {
                interpolator = new BounceInterpolator();
                dialog.dismiss();
              } else if (checkedRadioButtonId == R.id.radio_fast_out_slow_in) {
                interpolator = new FastOutSlowInInterpolator();
                dialog.dismiss();
              } else {
                interpolator = null;
                dialog.dismiss();
              }
            });

    view.findViewById(R.id.clear_button)
        .setOnClickListener(
            v -> {
              setUpDefaultValues();
              dialog.dismiss();
            });
  }

  /** A custom overshoot interpolator which exposes its tension. */
  private static class CustomOvershootInterpolator extends OvershootInterpolator {

    // This is the default tension value in OvershootInterpolator
    static final float DEFAULT_TENSION = 2.0f;

    final float tension;

    CustomOvershootInterpolator() {
      this(DEFAULT_TENSION);
    }

    CustomOvershootInterpolator(float tension) {
      super(tension);
      this.tension = tension;
    }
  }

  /** A custom anticipate overshoot interpolator which exposes its tension. */
  private static class CustomAnticipateOvershootInterpolator
      extends AnticipateOvershootInterpolator {

    // This is the default tension value in AnticipateOvershootInterpolator
    static final float DEFAULT_TENSION = 2.0f;

    final float tension;

    CustomAnticipateOvershootInterpolator() {
      this(DEFAULT_TENSION);
    }

    CustomAnticipateOvershootInterpolator(float tension) {
      super(tension);
      this.tension = tension;
    }
  }

  /** A custom cubic bezier interpolator which exposes its control points. */
  private static class CustomCubicBezier implements Interpolator {

    final float controlX1;
    final float controlY1;
    final float controlX2;
    final float controlY2;

    private final Interpolator interpolator;

    CustomCubicBezier(float controlX1, float controlY1, float controlX2, float controlY2) {
      this.controlX1 = controlX1;
      this.controlY1 = controlY1;
      this.controlX2 = controlX2;
      this.controlY2 = controlY2;

      this.interpolator = PathInterpolatorCompat.create(controlX1, controlY1, controlX2, controlY2);
    }

    @Override
    public float getInterpolation(float input) {
      return interpolator.getInterpolation(input);
    }

    String getDescription(Context context) {
      return context.getString(
          R.string.cat_transition_config_custom_interpolator_desc,
          controlX1,
          controlY1,
          controlX2,
          controlY2);
    }
  }
}
