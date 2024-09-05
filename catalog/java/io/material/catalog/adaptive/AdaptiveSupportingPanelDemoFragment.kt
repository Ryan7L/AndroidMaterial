package io.material.catalog.adaptive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ReactiveGuide
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R

class AdaptiveSupportingPanelDemoFragment: Fragment() {
  private lateinit var fragmentContainer: ConstraintLayout
  private lateinit var portraitLayout: ConstraintSet
  private lateinit var landscapeLayout: ConstraintSet
  private lateinit var guideline: ReactiveGuide

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_adaptive_supporting_panel_fragment, container, false)
    guideline = view.findViewById(R.id.horizontal_fold)
    val supportingPanelRv = view.findViewById<RecyclerView>(R.id.supporting_panel_side_container)
    supportingPanelRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    supportingPanelRv.adapter = PanelAdapter()
    ViewCompat.setNestedScrollingEnabled(supportingPanelRv, false)
    fragmentContainer = view.findViewById(R.id.supporting_panel_container)
    portraitLayout = ConstraintSet()
    portraitLayout.clone(fragmentContainer)
    landscapeLayout = getLandscapeLayout(fragmentContainer)
    return view
  }

  fun updatePortraitLayout(){
    portraitLayout.applyTo(fragmentContainer)
  }
  fun updateLandscapeLayout(){
    landscapeLayout.applyTo(fragmentContainer)
  }
  fun updateTableTopLayout(foldPosition: Int,foldWidth: Int){
    val tableTopLayout = getTableTopLayout(portraitLayout, foldWidth)
    tableTopLayout.applyTo(fragmentContainer)
    guideline.setGuidelineBegin(foldPosition)
  }
  private fun getLandscapeLayout(constraintLayout: ConstraintLayout): ConstraintSet{
    val marginVertical = resources.getDimensionPixelSize(R.dimen.cat_adaptive_margin_vertical)
    val marginHorizontal = resources.getDimensionPixelSize(R.dimen.cat_adaptive_margin_horizontal)
    return ConstraintSet().apply {
      clone(constraintLayout)
      connect(
        R.id.supporting_panel_main_content,
        ConstraintSet.END,
        R.id.supporting_panel_side_container,
        ConstraintSet.START
      )
      connect(
        R.id.supporting_panel_main_content,
        ConstraintSet.BOTTOM,
        ConstraintSet.PARENT_ID,
        ConstraintSet.BOTTOM)
      setMargin(R.id.supporting_panel_main_content, ConstraintSet.TOP, marginVertical)
      setMargin(R.id.supporting_panel_main_content, ConstraintSet.BOTTOM, marginVertical)
      setMargin(R.id.supporting_panel_main_content, ConstraintSet.END, marginHorizontal)

      constrainMinHeight(R.id.supporting_panel_main_content,0)
      connect(
        R.id.supporting_panel_side_container,
        ConstraintSet.TOP,
        ConstraintSet.PARENT_ID,
        ConstraintSet.TOP)
      connect(
        R.id.supporting_panel_side_container,
        ConstraintSet.START,
        R.id.supporting_panel_main_content,
        ConstraintSet.END
      )
      constrainPercentWidth(R.id.supporting_panel_side_container, 0.4f)
    }
  }
  private fun getTableTopLayout(portraitLayout: ConstraintSet, foldWidth: Int): ConstraintSet {
    val marginVertical = resources.getDimensionPixelSize(R.dimen.cat_adaptive_margin_vertical)
    return ConstraintSet().apply {
      clone(portraitLayout)
      setVisibility(R.id.horizontal_fold, View.VISIBLE)
      connect(R.id.supporting_panel_main_content, ConstraintSet.BOTTOM, R.id.horizontal_fold, ConstraintSet.TOP)
      setMargin(R.id.supporting_panel_main_content, ConstraintSet.BOTTOM, marginVertical)
      constrainMinHeight(R.id.supporting_panel_main_content,0)
      connect(
        R.id.supporting_panel_side_container,
        ConstraintSet.TOP,
        R.id.horizontal_fold,
        ConstraintSet.BOTTOM,
        marginVertical + foldWidth
      )
    }
  }
  private class PanelAdapter: RecyclerView.Adapter<PanelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanelViewHolder {
      return PanelViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cat_adaptive_supporting_panel_item, parent, false))
    }
    override fun onBindViewHolder(holder: PanelViewHolder, position: Int) {
    }
    override fun getItemCount(): Int {
      return 10
    }
  }
  private class PanelViewHolder(view: View): RecyclerView.ViewHolder(view)
}
