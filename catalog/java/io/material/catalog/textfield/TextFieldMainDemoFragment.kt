package io.material.catalog.textfield

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R

class TextFieldMainDemoFragment: TextFieldDemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(textFieldContent,container,false)
  }

  override val textFieldContent: Int
    get() = R.layout.cat_textfield_content
}
