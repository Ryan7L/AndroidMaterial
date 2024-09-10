package io.material.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.google.android.material.card.MaterialCardView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class CardStatesFragment : DemoFragment() {
  override val demoTitleResId: Int
    get() = R.string.cat_card_states

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_card_states_fragment, container, false)
    val radioGroup = view.findViewById<RadioGroup>(R.id.cat_card_radio_group)
    val cardView = view.findViewById<MaterialCardView>(R.id.card)
    val checkableCardView = view.findViewById<MaterialCardView>(R.id.checkable_card)
    radioGroup.setOnCheckedChangeListener { group, checkedId ->
      checkableCardView.setOnLongClickListener {
        checkableCardView.isChecked = !checkableCardView.isChecked
        true
      }
      radioGroup.setOnCheckedChangeListener { group, checkedId ->
        cardView.isHovered = checkedId == R.id.hovered
        cardView.setPressed(checkedId == R.id.pressed)
        checkableCardView.isHovered = checkedId == R.id.hovered
        checkableCardView.setPressed(checkedId == R.id.pressed)
      }
    }
    return view
  }
}
