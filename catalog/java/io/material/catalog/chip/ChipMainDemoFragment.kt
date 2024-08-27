package io.material.catalog.chip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ChipMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_chip_fragment, container, false)
    val content = view.findViewById<ViewGroup>(R.id.content)
    View.inflate(context, R.layout.cat_chip_content, content)
    val chips = DemoUtils.findViewsWithType(view, Chip::class.java)
    chips.forEach {
      it.setOnCloseIconClickListener {
        Snackbar.make(view, "Clicked close icon.", Snackbar.LENGTH_SHORT).show()
      }
      if (it.isEnabled && !it.isCheckable) {
        it.setOnClickListener {
          Snackbar.make(view, "Activated chip.", Snackbar.LENGTH_SHORT).show()
        }
      }
    }
    val longTextSwitch = view.findViewById<MaterialSwitch>(R.id.cat_chip_text_length_switch)

    longTextSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      val updatedText =
        getText(if (isChecked) R.string.cat_chip_text_to_truncate else R.string.cat_chip_text)
      chips.forEach {
        it.text = updatedText
      }
    }

    val enabledSwitch = view.findViewById<MaterialSwitch>(R.id.cat_chip_enabled_switch)
    enabledSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      chips.forEach {
        it.isEnabled = isChecked
      }
    }
    return view
  }

}
