package io.material.catalog.progressindicator

import android.view.View
import androidx.annotation.StyleRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.progressindicator.CircularProgressIndicatorSpec
import com.google.android.material.progressindicator.IndeterminateDrawable
import io.material.catalog.R

class ProgressIndicatorStandaloneDemoFragment: ProgressIndicatorDemoFragment() {

  override fun initDemoContents(view: View) {
    val spec = CircularProgressIndicatorSpec(requireContext(), /* attrs= */ null, 0, specStyleResId)
    val chip = view.findViewById<Chip>(R.id.cat_progress_indicator_chip)
    chip.chipIcon = IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
    val progressIndicatorDrawable = IndeterminateDrawable.createCircularDrawable(requireContext(), spec)
    val btn = view.findViewById<MaterialButton>(R.id.cat_progress_indicator_button)
    btn.icon = progressIndicatorDrawable
    val chipIconSwitch = view.findViewById<MaterialSwitch>(R.id.cat_progress_indicator_standalone_chip_switch)
    chipIconSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      chip.isChipIconVisible = isChecked
      btn.icon = if (isChecked)  progressIndicatorDrawable else null
    }
  }


  override val progressIndicatorContentLayout: Int
    get() = R.layout.cat_progress_indicator_standalone_content

  @StyleRes
  val specStyleResId: Int = R.style.Widget_Material3_CircularProgressIndicator_ExtraSmall
}
