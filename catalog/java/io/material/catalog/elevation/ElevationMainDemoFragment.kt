package io.material.catalog.elevation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.internal.ViewUtils
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ElevationMainDemoFragment : DemoFragment() {

  private var currentElevationLevel = 0
    set(value) {
      field = value
      elevationInDp = elevationLevelValues[field]
    }
  private var elevationInDp = 0
  private lateinit var elevationLevelValues: IntArray

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_elevation_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    elevationLevelValues = resources.getIntArray(R.array.cat_elevation_level_values)

    val increaseButton = view.findViewById<Button>(R.id.increase_elevation)
    val decreaseButton = view.findViewById<Button>(R.id.decrease_elevation)

    increaseButton.setOnClickListener {
      updateCardsElevationLevel(view, currentElevationLevel + 1)
    }
    decreaseButton.setOnClickListener {
      updateCardsElevationLevel(view, currentElevationLevel - 1)
    }
    updateCardsElevationLevel(view, currentElevationLevel)
  }

  private fun updateCardsElevationLevel(view: View, newElevationLevel: Int) {
    val cards = DemoUtils.findViewsWithType(view, MaterialCardView::class.java)
    if (newElevationLevel in 0..maxElevationValue) {
      currentElevationLevel = newElevationLevel
      cards.forEach {
        it.cardElevation = ViewUtils.dpToPx(view.context, elevationInDp)
      }
      setElevationLevelTextView(view, elevationLevelText)
    }
  }

  private fun setElevationLevelTextView(view: View, levelText: String) {
    val tv = view.findViewById<TextView>(R.id.current_elevation_level_label)
    tv.text = levelText
  }

  private val maxElevationValue: Int
    get() = elevationLevelValues.size - 1
  private val elevationLevelText: String
    get() = resources.getString(R.string.cat_elevation_fragment_level, elevationInDp)
}
