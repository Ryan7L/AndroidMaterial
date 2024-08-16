package io.material.catalog.textfield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.material.catalog.R

/**
 * 显示填充文本字段演示的片段，其中包含应用程序的开始和结束图标以及控件。
 * @property textFieldContent Int
 */
class TextFieldExposedDropdownMenuDemoFragment : TextFieldControllableDemoFragment() {
  override val textFieldContent: Int
    get() = R.layout.cat_textfield_exposed_dropdown_menu_content

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = super.onCreateDemoView(inflater, container, savedInstanceState)
    val toggleLeadingIconButton = view?.findViewById<Button>(R.id.button_toggle_leading_icon)
    toggleLeadingIconButton?.setOnClickListener {
      if (textfields.isNotEmpty() && textfields[0].startIconDrawable == null) {
        textfields.forEach {
          it.setStartIconDrawable(R.drawable.ic_search_24px)
        }
        toggleLeadingIconButton.text = resources.getString(R.string.cat_textfield_hide_leading_icon)
      } else {
        textfields.forEach {
          it.startIconDrawable = null
        }
      }
    }
    return view
  }
}
