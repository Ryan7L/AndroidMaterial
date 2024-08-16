package io.material.catalog.textfield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.textfield.TextInputLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

abstract class TextFieldDemoFragment : DemoFragment() {
  open lateinit var textfields: List<TextInputLayout>

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_textfield_fragment, container, false)
    initTextFields(inflater, view)
    initTextFieldDemoControls(inflater, view)
    return view
  }

  private fun initTextFields(inflater: LayoutInflater, view: View) {
    inflateTextFields(inflater, view.findViewById(R.id.content))
    //在演示控件的文本字段之前添加内容布局中的文本字段，以允许修改演示文本字段，而无需修改用于演示控件的文本字段。
    addTextFieldsToList(view)
  }


  private fun inflateTextFields(inflater: LayoutInflater, content: ViewGroup) {
    content.addView(inflater.inflate(textFieldContent, content, false))
  }

  protected open fun initTextFieldDemoControls(inflater: LayoutInflater, view: View) {
    inflateTextFieldDemoControls(inflater, view.findViewById(R.id.content))

  }

  private fun inflateTextFieldDemoControls(inflater: LayoutInflater, content: ViewGroup) {
    if (textFieldDemoControlsLayout != 0) {
      content.addView(inflater.inflate(textFieldDemoControlsLayout, content, false))
    }
  }

  private fun addTextFieldsToList(view: View) {
    textfields = DemoUtils.findViewsWithType(view, TextInputLayout::class.java)
  }

  @get:LayoutRes
  open val textFieldContent: Int = R.layout.cat_textfield_content

  @get:LayoutRes
  open val textFieldDemoControlsLayout: Int = 0
}
