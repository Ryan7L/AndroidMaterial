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

class AdaptiveFeedDemoFragment : Fragment() {
  private lateinit var fold: ReactiveGuide
  private lateinit var constraintLayout: ConstraintLayout
  private lateinit var closedLayout: ConstraintSet


  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_adaptive_feed_fragment, container, false)
    fold = view.findViewById(R.id.fold)
    val smallRv = view.findViewById<RecyclerView>(R.id.small_content_list)
    setUpContentRv(smallRv,true,15)
    val largeRv = view.findViewById<RecyclerView>(R.id.large_content_list)
    setUpContentRv(largeRv,false,5)
    constraintLayout = view.findViewById(R.id.feed_constraint_layout)
    closedLayout = ConstraintSet()
    closedLayout.clone(constraintLayout)
    return view
  }
  private fun setUpContentRv(
    rv: RecyclerView, isSmallContent: Boolean, itemCount: Int
  ) {
    rv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
    rv.adapter = FeedAdapter(if (isSmallContent) R.layout.cat_adaptive_feed_small_item else
    R.layout.cat_adaptive_feed_large_item,itemCount)
    ViewCompat.setNestedScrollingEnabled(rv,false)
  }

  private fun getOpenLayout(closedLayout: ConstraintSet, foldWidth: Int): ConstraintSet {
    val marginHorizontal = resources.getDimensionPixelOffset(R.dimen.cat_adaptive_margin_horizontal)
    return ConstraintSet().apply {
      clone(closedLayout)
      connect(
        R.id.top_button,
        ConstraintSet.START,
        R.id.fold,
        ConstraintSet.END,
        marginHorizontal + foldWidth
      )
      connect(
        R.id.small_content_list,
        ConstraintSet.START,
        R.id.fold,
        ConstraintSet.END,
        marginHorizontal + foldWidth
      )
      connect(
        R.id.small_content_list,
        ConstraintSet.TOP,
        R.id.top_button,
        ConstraintSet.BOTTOM
      )
      setVisibility(R.id.highlight_content_card, View.GONE)
      setVisibility(R.id.large_content_list, View.VISIBLE)
    }
  }

  fun setOpenLayout(foldPosition: Int, foldWidth: Int) {
    val openLayout = getOpenLayout(closedLayout, foldWidth)
    openLayout.applyTo(constraintLayout)
    fold.setGuidelineEnd(foldPosition)
  }

  fun setClosedLayout() {
    fold.setGuidelineEnd(0)
    closedLayout.applyTo(constraintLayout)
  }


  class FeedAdapter(private val itemLayout: Int, private val itemCount: Int) :
    RecyclerView.Adapter<FeedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
      return FeedViewHolder(LayoutInflater.from(parent.context).inflate(itemLayout, parent, false))
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
      return itemCount
    }
  }

  class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
