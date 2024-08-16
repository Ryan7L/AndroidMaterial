package io.material.catalog.textfield

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.material.catalog.R

abstract class TextFieldControllableDemoFragment : TextFieldDemoFragment() {

  var errorText: String? = null

  override fun initTextFieldDemoControls(inflater: LayoutInflater, view: View) {
    super.initTextFieldDemoControls(inflater, view)
    errorText = resources.getString(R.string.cat_textfield_error)
    //用于切换错误文本可见性的初始化按钮。
    val toggleErrorButton = view.findViewById<Button>(R.id.button_toggle_error)
    toggleErrorButton.setOnClickListener {
      if (textfields.isNotEmpty() && textfields[0].error == null) {
        setAllTextFieldsError(errorText)
        toggleErrorButton.text = resources.getString(R.string.cat_textfield_hide_error_text)
        Snackbar.make(it, R.string.cat_textfield_show_error_text, Snackbar.LENGTH_SHORT).show()
      } else {
        setAllTextFieldsError(null)
        toggleErrorButton.text = resources.getString(R.string.cat_textfield_show_error_text)
        Snackbar.make(it, R.string.cat_textfield_hide_error_text, Snackbar.LENGTH_SHORT).show()
      }
    }
    //用于更新标签文本的初始化文本字段。
    val labelTextField = view.findViewById<TextInputLayout>(R.id.text_input_label)
    val labelEditText = labelTextField.editText
    labelEditText?.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (!labelTextField.checkTextInputIsNull(true)) {
          setAllTextFieldsLabel(labelEditText.text.toString())
          showToast(R.string.cat_textfield_toast_label_text)
        }
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }

    //用于更新错误文本的初始化文本字段
    val textInputError = view.findViewById<TextInputLayout>(R.id.text_input_error)
    val inputErrorEditText = textInputError.editText
    inputErrorEditText?.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (!textInputError.checkTextInputIsNull(true)) {
          updateErrorText(inputErrorEditText.text.toString(), toggleErrorButton)
          showToast(R.string.cat_textfield_toast_error_text)
        }
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }

    val helperTextTextField = view.findViewById<TextInputLayout>(R.id.text_input_helper_text)
    val helperTextEditText = helperTextTextField.editText
    helperTextEditText?.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (!helperTextTextField.checkTextInputIsNull(true)) {
          setAllTextFieldsHelperText(helperTextEditText.text.toString())
          showToast(R.string.cat_textfield_toast_helper_text)
        }
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }
    val placeholderTextField = view.findViewById<TextInputLayout>(R.id.text_input_placeholder)
    val placeholderEditText = placeholderTextField.editText
    placeholderEditText?.setOnEditorActionListener { v, actionId, event ->
      if (!placeholderTextField.checkTextInputIsNull(true)) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          setAllTextFieldsPlaceholder(placeholderEditText.text.toString())
          showToast(R.string.cat_textfield_toast_placeholder_text)
        }
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }
    val counterMaxTextField = view.findViewById<TextInputLayout>(R.id.text_input_counter_max)
    val counterEditText = counterMaxTextField.editText
    counterEditText?.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        if (!counterMaxTextField.checkTextInputIsNull(true)) {
          val length = counterEditText.text.toString().toInt()
          setAllTextFieldsCounterMax(length)
          showToast(R.string.cat_textfield_toast_counter_text)
        }
        return@setOnEditorActionListener true
      }
      return@setOnEditorActionListener false
    }
    val updateButton = view.findViewById<Button>(R.id.button_update)
    updateButton.setOnClickListener {
      var updated = false
      if (!labelTextField.checkTextInputIsNull(false)) {
        setAllTextFieldsLabel(labelEditText?.text.toString())
        updated = true
      }
      if (!textInputError.checkTextInputIsNull(false)) {
        updateErrorText(inputErrorEditText?.text.toString(), toggleErrorButton)
        updated = true
      }
      if (!helperTextTextField.checkTextInputIsNull(false)) {
        setAllTextFieldsHelperText(helperTextEditText?.text.toString())
        updated = true
      }
      if (!counterMaxTextField.checkTextInputIsNull(false)) {
        val length = counterEditText?.text.toString().toInt()
        setAllTextFieldsCounterMax(length)
      }
      if (!placeholderTextField.checkTextInputIsNull(false)) {
        setAllTextFieldsPlaceholder(placeholderEditText?.text.toString())
        updated = true
      }
      if (updated) {
        showToast(R.string.cat_textfield_toast_update_button)
      }
    }
    val enabledSwitch = view.findViewById<MaterialSwitch>(R.id.cat_textfield_enabled_switch)
    enabledSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      textfields.forEach {
        it.isEnabled = isChecked
      }
      val messageId =
        if (isChecked) resources.getString(R.string.cat_textfield_label_enabled) else resources.getString(
          R.string.cat_textfield_label_disabled
        )
      Snackbar.make(buttonView, messageId, Snackbar.LENGTH_SHORT).show()
    }
  }

  private fun setAllTextFieldsLabel(label: String) {
    textfields.forEach {
      it.hint = label
    }
  }

  private fun setAllTextFieldsError(error: String?) {
    val parent = textfields[0].parent as ViewGroup
    var textFieldWithErrorHasFocus = false
    textfields.forEach {
      setErrorIconClickListeners(it)
      it.error = error
      textFieldWithErrorHasFocus = textFieldWithErrorHasFocus or it.hasFocus()
    }
    if (!textFieldWithErrorHasFocus) {
      //请求可访问性重点关注第一个文本字段以显示错误。
      parent.getChildAt(0).requestFocus()
    }
  }

  private fun updateErrorText(errorText: String, toggleErrorButton: Button) {
    this.errorText = errorText
    //如果已经显示错误，请再次调用 setError 来更新其文本
    if (toggleErrorButton.text.toString() == resources.getString(R.string.cat_textfield_hide_error_text)) {
      textfields.forEach {
        setErrorIconClickListeners(it)
        it.error = errorText
      }
    }
  }

  private fun setErrorIconClickListeners(inputLayout: TextInputLayout) {
    inputLayout.setErrorIconOnClickListener {
      showToast(R.string.cat_textfield_toast_error_icon_click)
    }
    inputLayout.setErrorIconOnLongClickListener {
      showToast(R.string.cat_textfield_toast_error_icon_long_click)
      true
    }
  }

  private fun setAllTextFieldsHelperText(helperText: String) {
    textfields.forEach {
      it.helperText = helperText
    }
  }

  private fun setAllTextFieldsPlaceholder(placeholder: String) {
    textfields.forEach {
      it.placeholderText = placeholder
    }
  }

  private fun setAllTextFieldsCounterMax(length: Int) {
    textfields.forEach {
      it.counterMaxLength = length
    }
  }

  //  private fun checkTextInputIsNull(textInputLayout: TextInputLayout, showError: Boolean): Boolean {
//    return if (textInputLayout.editText?.text.isNullOrEmpty()) {
//      if (showError) {
//        textInputLayout.error = resources.getString(R.string.cat_textfield_null_input_error_text)
//      }
//      true
//    } else {
//      textInputLayout.error = null
//      false
//    }
//  }
  fun TextInputLayout.checkTextInputIsNull(showError: Boolean): Boolean {
    return if (this.editText?.text.isNullOrEmpty()) {
      if (showError) {
        this.error = resources.getString(R.string.cat_textfield_null_input_error_text)
      }
      true
    } else {
      this.error = null
      false
    }
  }

  private fun showToast(@StringRes msgResId: Int) {
    Toast.makeText(context, msgResId, Toast.LENGTH_LONG).show()
  }

  override val textFieldDemoControlsLayout: Int
    get() = R.layout.cat_textfield_controls
}
