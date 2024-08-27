package io.material.catalog.chip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class ChipGroupDemoFragment : DemoFragment() {
  private lateinit var singleSelectionSwitch: MaterialSwitch
  private lateinit var selectionRequiredSwitch: MaterialSwitch

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_chip_group_fragment, container, false)
    singleSelectionSwitch = view.findViewById(R.id.single_selection)
    selectionRequiredSwitch = view.findViewById(R.id.selection_required)

    val reflowGroup = view.findViewById<ChipGroup>(R.id.reflow_group)
    val scrollGroup = view.findViewById<ChipGroup>(R.id.scroll_group)

    singleSelectionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      reflowGroup.isSingleSelection = isChecked
      scrollGroup.isSingleSelection = isChecked
      initChipGroup(reflowGroup)
      initChipGroup(scrollGroup)
    }
    selectionRequiredSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      reflowGroup.isSelectionRequired = isChecked
      scrollGroup.isSelectionRequired = isChecked
      initChipGroup(reflowGroup)
      initChipGroup(scrollGroup)
    }
    initChipGroup(reflowGroup)
    initChipGroup(scrollGroup)
    val fab = view.findViewById<FloatingActionButton>(R.id.cat_chip_group_refresh)
    fab.setOnClickListener {
      initChipGroup(reflowGroup)
      initChipGroup(scrollGroup)
    }
    return view
  }

  private fun initChipGroup(chipGroup: ChipGroup) {
    chipGroup.removeAllViews()
    val singleSelection = singleSelectionSwitch.isChecked
    val textArray = resources.getStringArray(R.array.cat_chip_group_text_array)
    textArray.forEach {
      val chip = layoutInflater.inflate(getChipGroupItem(singleSelection), chipGroup, false) as Chip
      chip.text = it
      chip.setCloseIconVisible(true)
      chip.setOnCloseIconClickListener {
        chipGroup.removeView(chip)
      }
      chipGroup.addView(chip)
    }
  }

  private fun getChipGroupItem(singleSelection: Boolean): Int {
    return if (singleSelection) {
      R.layout.cat_chip_group_item_choice
    } else {
      R.layout.cat_chip_group_item_filter
    }
  }
}
