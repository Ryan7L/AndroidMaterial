package io.material.catalog.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.android.material.behavior.SwipeDismissBehavior.OnDismissListener
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class CardSwipeDismissFragment : DemoFragment() {
  override val demoTitleResId: Int
    get() = R.string.cat_card_swipe_dismiss

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_card_swipe_fragment, container, false)
    val containerLayout = view.findViewById<CoordinatorLayout>(R.id.card_container)
    val behavior = SwipeDismissBehavior<View>()
    behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END)

    val cardContentLayout = view.findViewById<MaterialCardView>(R.id.card_content_layout)
    val params = cardContentLayout.layoutParams as? CoordinatorLayout.LayoutParams
    params?.behavior = behavior
    behavior.listener = object : OnDismissListener {
      override fun onDismiss(view: View?) {
        Snackbar.make(containerLayout, R.string.cat_card_dismissed, Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.cat_card_undo) {
            resetCard(cardContentLayout)
          }.show()
      }

      override fun onDragStateChanged(state: Int) {
        onDragStateChanged(state, cardContentLayout)
      }
    }
    return view
  }

  companion object {
    private fun onDragStateChanged(state: Int, cardContentLayout: MaterialCardView) {
      when (state) {
        SwipeDismissBehavior.STATE_DRAGGING, SwipeDismissBehavior.STATE_SETTLING -> cardContentLayout.isDragged =
          true

        SwipeDismissBehavior.STATE_IDLE -> cardContentLayout.isDragged = false
        else -> {}
      }
    }

    private fun resetCard(cardContentLayout: MaterialCardView) {
      val params = cardContentLayout.layoutParams as? ViewGroup.MarginLayoutParams
      params?.setMargins(0, 0, 0, 0)
      cardContentLayout.alpha = 1.0f
      cardContentLayout.requestLayout()
    }
  }
}
