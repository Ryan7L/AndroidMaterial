package io.material.catalog.textfield

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import io.material.catalog.R

open class TextFieldFilledDemoFragment : TextFieldControllableDemoFragment() {
  override val textFieldContent: Int
    get() = R.layout.cat_textfield_filled_content

}
