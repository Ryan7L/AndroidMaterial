package io.material.catalog.button

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils
import kotlin.math.max

class ButtonsMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(buttonsContentResId, container, false)

    val labelButtonContent = view.findViewById<ViewGroup>(R.id.labelButtonContent)
    View.inflate(context, labelButtonContentResId, labelButtonContent)
    val labelIconButtonContent = view.findViewById<ViewGroup>(R.id.labelIconButtonContent)
    View.inflate(context, labelIconButtonContentResId, labelIconButtonContent)
    val iconButtonContent = view.findViewById<ViewGroup>(R.id.iconButtonContent)
    View.inflate(context, iconButtonContentResId, iconButtonContent)

    val buttons = DemoUtils.findViewsWithType(view, MaterialButton::class.java)
    var maxMeasuredWidth = 0

    val displayMetrics = resources.displayMetrics
    buttons.forEach {
      it.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
      maxMeasuredWidth = max(maxMeasuredWidth, it.measuredWidth)
      it.setOnClickListener { v ->
        Snackbar.make(v, R.string.cat_button_clicked, Snackbar.LENGTH_LONG)
          .setAction(R.string.cat_snackbar_action_button_text, {}).show()
      }
    }

    val enabledSwitch = view.findViewById<MaterialSwitch>(R.id.cat_button_enabled_switch)
    enabledSwitch.setOnCheckedChangeListener { _, isChecked ->
      val text =
        getText(if (isChecked) R.string.cat_button_label_enabled else R.string.cat_button_label_disabled)
      buttons.forEach {
        if (it.text.isNotEmpty()) {
          it.text = text
        }
        it.isEnabled = isChecked
        it.isFocusable = isChecked
      }
    }

    return view
  }

  @LayoutRes
  private val labelButtonContentResId = R.layout.cat_label_buttons_content

  @LayoutRes
  private val labelIconButtonContentResId = R.layout.cat_label_icon_buttons_content

  @LayoutRes
  private val iconButtonContentResId = R.layout.cat_icon_buttons_content

  @LayoutRes
  private val buttonsContentResId = R.layout.cat_buttons_fragment

}
