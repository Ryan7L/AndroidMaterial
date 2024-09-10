package io.material.catalog.transition

import android.util.SparseIntArray
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEach
import com.google.android.material.transition.MaterialSharedAxis
import io.material.catalog.R

private val BUTTON_AXIS_MAP = SparseIntArray().apply {
  append(R.id.radio_button_axis_x, MaterialSharedAxis.X)
  append(R.id.radio_button_axis_y, MaterialSharedAxis.Y)
  append(R.id.radio_button_axis_z, MaterialSharedAxis.Z)
}

/**
 * 用于设置和管理共享轴演示控件的帮助程序类。
 */
class SharedAxisHelper(controlLayout: ViewGroup) {
  private val backButton: Button = controlLayout.findViewById(R.id.back_button)
  private val nextButton: Button = controlLayout.findViewById(R.id.next_button)
  private val directionRadioGroup: RadioGroup =
    controlLayout.findViewById(R.id.radio_button_group_direction)

  init {
    ViewCompat.setOnApplyWindowInsetsListener(controlLayout) { view, insets ->
      view.setPadding(
        view.paddingLeft,
        view.paddingTop,
        view.paddingRight,
        insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
      )
      return@setOnApplyWindowInsetsListener insets
    }
  }

  var nextButtonOnClickListener: View.OnClickListener? = null
    set(value) {
      nextButton.setOnClickListener(value)
    }
  var backButtonOnClickListener: View.OnClickListener? = null
    set(value) {
      backButton.setOnClickListener(value)
    }

  fun updateButtonsEnabled(startScreenShowing: Boolean) {
    backButton.isEnabled = !startScreenShowing
    nextButton.isEnabled = startScreenShowing
  }

  var selectedAxis: Int
    get() = BUTTON_AXIS_MAP[directionRadioGroup.checkedRadioButtonId]
    set(value) {
      val index = BUTTON_AXIS_MAP.indexOfValue(value)
      val id = BUTTON_AXIS_MAP.keyAt(index)
      directionRadioGroup.check(id)
    }

  fun setAxisButtonGroupEnabled(enabled: Boolean) {
    directionRadioGroup.forEach {
      it.isEnabled = enabled
    }
  }
}
