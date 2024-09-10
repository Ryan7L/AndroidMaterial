package io.material.catalog.textfield

import io.material.catalog.R

open class TextFieldFilledDemoFragment : TextFieldControllableDemoFragment() {
  override val textFieldContent: Int
    get() = R.layout.cat_textfield_filled_content
}
