package io.material.catalog.card

import android.animation.LayoutTransition
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.AccessibilityDelegate
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.card.MaterialCardView
import io.material.catalog.R
import io.material.catalog.draggable.DraggableCoordinatorLayout
import io.material.catalog.draggable.DraggableCoordinatorLayout.ViewDragListener
import io.material.catalog.feature.DemoFragment

class DraggableCardFragment : DemoFragment() {
  private lateinit var cardView: MaterialCardView

  override val demoTitleResId: Int
    get() = R.string.cat_card_draggable_card

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_card_draggable_fragment, container, false)
    val containerView = view as DraggableCoordinatorLayout
    val transition = (view as CoordinatorLayout).layoutTransition
    transition.enableTransitionType(LayoutTransition.CHANGING)
    cardView = view.findViewById(R.id.draggable_card)
    cardView.accessibilityDelegate = cardDelegate
    containerView.addDraggableChild(cardView)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return view
    containerView.setViewDragListener(object : ViewDragListener {
      override fun onViewCaptured(view: View, i: Int) {
        cardView.isDragged = true
      }

      override fun onViewReleased(view: View, v: Float, v1: Float) {
        cardView.isDragged = false
      }

    })
    return view
  }

  private val cardDelegate = object : AccessibilityDelegate() {

    override fun onInitializeAccessibilityNodeInfo(host: View, info: AccessibilityNodeInfo) {
      super.onInitializeAccessibilityNodeInfo(host, info)
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return

      val layoutParams = cardView.layoutParams as CoordinatorLayout.LayoutParams
      val gravity = layoutParams.gravity
      val isOnLeft = (gravity and Gravity.LEFT) == Gravity.LEFT
      val isOnRight = (gravity and Gravity.RIGHT) == Gravity.RIGHT
      val isOnTop = (gravity and Gravity.TOP) == Gravity.TOP
      val isOnBottom = (gravity and Gravity.BOTTOM) == Gravity.BOTTOM
      val isOnCenter = (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL
      if (!(isOnTop && isOnLeft)) {
        info.addAction(
          AccessibilityNodeInfo.AccessibilityAction(
            R.id.move_card_top_left_action,
            getString(R.string.cat_card_action_move_top_left)
          )
        )
      }
      if (!(isOnTop && isOnRight)) {
        info.addAction(
          AccessibilityNodeInfo.AccessibilityAction(
            R.id.move_card_top_right_action,
            getString(R.string.cat_card_action_move_top_right)
          )
        )
      }
      if (!(isOnBottom && isOnLeft)) {
        info.addAction(
          AccessibilityNodeInfo.AccessibilityAction(
            R.id.move_card_bottom_left_action,
            getString(R.string.cat_card_action_move_bottom_left)
          )
        )
      }
      if (!(isOnBottom && isOnRight)) {
        info.addAction(
          AccessibilityNodeInfo.AccessibilityAction(
            R.id.move_card_bottom_right_action,
            getString(R.string.cat_card_action_move_bottom_right)
          )
        )
      }
      if (!isOnCenter) {
        info.addAction(
          AccessibilityNodeInfo.AccessibilityAction(
            R.id.move_card_center_action,
            getString(R.string.cat_card_action_move_center)
          )
        )
      }
    }

    override fun performAccessibilityAction(host: View, action: Int, args: Bundle?): Boolean {
      val gravity = when (action) {
        R.id.move_card_top_left_action -> Gravity.TOP or Gravity.LEFT
        R.id.move_card_top_right_action -> Gravity.TOP or Gravity.RIGHT
        R.id.move_card_bottom_left_action -> Gravity.BOTTOM or Gravity.LEFT
        R.id.move_card_bottom_right_action -> Gravity.BOTTOM or Gravity.RIGHT
        R.id.move_card_center_action -> Gravity.CENTER
        else -> return super.performAccessibilityAction(host, action, args)
      }
      val layoutParams = cardView.layoutParams as CoordinatorLayout.LayoutParams
      if (layoutParams.gravity != gravity) {
        layoutParams.gravity = gravity
        cardView.requestLayout()
      }
      return true
    }
  }

}
