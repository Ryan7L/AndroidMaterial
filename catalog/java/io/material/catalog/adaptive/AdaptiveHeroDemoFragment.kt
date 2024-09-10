package io.material.catalog.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R

class AdaptiveHeroDemoFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_adaptive_hero_fragment, container, false)
    val sideRv = view.findViewById<RecyclerView>(R.id.hero_side_content)
    sideRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    sideRv.adapter = HeroAdapter()
    ViewCompat.setNestedScrollingEnabled(sideRv, false)
    val constraintLayout = view.findViewById<ConstraintLayout>(R.id.hero_constraint_layout)
    val smallLayout = getSmallLayout(constraintLayout)
    val mediumLayout = getMediumLayout(smallLayout)
    val largeLayout = getLargeLayout(mediumLayout)
    val screenWidth = resources.configuration.screenWidthDp
    if (screenWidth < AdaptiveUtils.MEDIUM_SCREEN_WIDTH_SIZE) {
      smallLayout.applyTo(constraintLayout)
    } else if (screenWidth < AdaptiveUtils.LARGE_SCREEN_WIDTH_SIZE) {
      mediumLayout.applyTo(constraintLayout)
    } else {
      largeLayout.applyTo(constraintLayout)
    }
    return view
  }

  private fun getSmallLayout(constraintLayout: ConstraintLayout): ConstraintSet {
    return ConstraintSet().apply {
      clone(constraintLayout)
    }
  }

  private fun getMediumLayout(smallLayout: ConstraintSet): ConstraintSet {
    val marginHorizontal = resources.getDimensionPixelOffset(R.dimen.cat_adaptive_hero_margin)
    val noMargin = resources.getDimensionPixelOffset(R.dimen.cat_adaptive_margin_none)
    val marginHorizontalAdditional =
      resources.getDimensionPixelOffset(R.dimen.cat_adaptive_hero_margin_horizontal_additional)
    return ConstraintSet().apply {
      clone(smallLayout)
      connect(
        R.id.hero_main_content,
        ConstraintSet.TOP,
        R.id.hero_top_content,
        ConstraintSet.BOTTOM
      )
      connect(
        R.id.hero_main_content,
        ConstraintSet.END,
        R.id.hero_side_content_container,
        ConstraintSet.START
      )
      connect(
        R.id.hero_main_content,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM
      )
      setMargin(R.id.hero_top_content, ConstraintSet.START, noMargin)
      setMargin(R.id.hero_top_content, ConstraintSet.LEFT, noMargin)
      setMargin(R.id.hero_top_content, ConstraintSet.END, marginHorizontalAdditional)
      setMargin(R.id.hero_top_content, ConstraintSet.RIGHT, marginHorizontalAdditional)
      connect(
        R.id.hero_side_content_container,
        ConstraintSet.TOP,
        R.id.hero_top_content,
        ConstraintSet.BOTTOM
      )
      connect(
        R.id.hero_side_content_container,
        ConstraintSet.START,
        R.id.hero_main_content,
        ConstraintSet.END
      )
      constrainPercentWidth(R.id.hero_side_content_container, 0.4f)
      setMargin(R.id.hero_side_content_container, ConstraintSet.START, marginHorizontal)
      setMargin(R.id.hero_side_content_container, ConstraintSet.LEFT, marginHorizontal)
      setMargin(R.id.hero_side_content_container, ConstraintSet.RIGHT, marginHorizontalAdditional)
      setMargin(R.id.hero_side_content_container, ConstraintSet.END, marginHorizontalAdditional)
    }
  }

  private fun getLargeLayout(mediumLayout: ConstraintSet): ConstraintSet {
    val noMargin = resources.getDimensionPixelOffset(R.dimen.cat_adaptive_margin_none)
    val marginHorizontal = resources.getDimensionPixelOffset(R.dimen.cat_adaptive_hero_margin)
    return ConstraintSet().apply {
      clone(mediumLayout)
      connect(
        R.id.hero_top_content,
        ConstraintSet.END,
        R.id.hero_side_content_container,
        ConstraintSet.START
      )
      connect(
        R.id.hero_side_content_container,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP
      )
      setMargin(
        R.id.hero_main_content,
        ConstraintSet.RIGHT,
        noMargin
      )
      setMargin(
        R.id.hero_main_content,
        ConstraintSet.END,
        noMargin
      )
      setMargin(R.id.hero_top_content, ConstraintSet.RIGHT, marginHorizontal)
      setMargin(R.id.hero_top_content, ConstraintSet.END, marginHorizontal)
    }
  }

  class HeroAdapter :
    RecyclerView.Adapter<HeroViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
      return HeroViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.cat_adaptive_hero_item, parent, false)
      )
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
      return 10
    }
  }

  class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
