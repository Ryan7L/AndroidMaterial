package io.material.catalog.navigationrail

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.google.android.material.navigation.NavigationBarView
import io.material.catalog.R

class NavigationRailDemoControlsFragment : NavigationRailDemoFragment() {
  private val MENU_GRAVITY_TOP = Gravity.TOP or Gravity.CENTER_HORIZONTAL
  private val MENU_GRAVITY_CENTER = Gravity.CENTER
  private val MENU_GRAVITY_BOTTOM = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL

  override fun initNavigationRailDemoControls(view: View) {
    super.initNavigationRailDemoControls(view)
    initAddRemoveHeaderViewButtons(view)
    initMenuGravityButtons(view)
    initLabelVisibilityModeButtons(view)
    initIconSlider(view)
  }

  override val navigationRailDemoControlsLayout: Int
    get() = R.layout.cat_navigation_demo_controls
  override val liftOnScrollTargetViewId: Int
    get() = R.id.cat_navigation_rail_nested_scroll_view

  private fun initMenuGravityButtons(view: View) {
    setMenuGravityClickListener(view, R.id.menu_gravity_top_button, MENU_GRAVITY_TOP)
    setMenuGravityClickListener(view, R.id.menu_gravity_center_button, MENU_GRAVITY_CENTER)
    setMenuGravityClickListener(view, R.id.menu_gravity_bottom_button, MENU_GRAVITY_BOTTOM)
  }


  private fun setMenuGravityClickListener(view: View, buttonId: Int, gravity: Int) {
    view.findViewById<Button>(buttonId).setOnClickListener {
      navigationRailView?.menuGravity = gravity
    }
  }

  private fun initAddRemoveHeaderViewButtons(view: View) {
    val addHeaderViewBtn = view.findViewById<Button>(R.id.add_header_view_button)
    val removeHeaderViewBtn = view.findViewById<Button>(R.id.remove_header_view_button)
    addHeaderViewBtn.setOnClickListener {
      navigationRailView?.addHeaderView(R.layout.cat_navigation_rail_header_view)
      addHeaderViewBtn.visibility = View.GONE
      removeHeaderViewBtn.visibility = View.VISIBLE
    }
    removeHeaderViewBtn.setOnClickListener {
      navigationRailView?.removeHeaderView()
      addHeaderViewBtn.visibility = View.VISIBLE
      removeHeaderViewBtn.visibility = View.GONE
    }
  }

  private fun initLabelVisibilityModeButtons(view: View) {
    setLabelVisibilityClickListener(
      view,
      R.id.label_mode_auto_button,
      NavigationBarView.LABEL_VISIBILITY_AUTO
    )
    setLabelVisibilityClickListener(
      view,
      R.id.label_mode_selected_button,
      NavigationBarView.LABEL_VISIBILITY_SELECTED
    )
    setLabelVisibilityClickListener(
      view,
      R.id.label_mode_labeled_button,
      NavigationBarView.LABEL_VISIBILITY_LABELED
    )
    setLabelVisibilityClickListener(
      view,
      R.id.label_mode_unlabeled_button,
      NavigationBarView.LABEL_VISIBILITY_UNLABELED
    )
  }

  private fun setLabelVisibilityClickListener(view: View, buttonId: Int, mode: Int) {
    view.findViewById<Button>(buttonId).setOnClickListener {
      navigationRailView?.labelVisibilityMode = mode
    }
  }

  private fun initIconSlider(view: View) {
    val iconSizeSlider = view.findViewById<SeekBar>(R.id.icon_size_slider)
    val displayMetrics = resources.displayMetrics
    val iconSizeTv = view.findViewById<TextView>(R.id.icon_size_text_view)
    iconSizeSlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      /**
       * Notification that the progress level has changed. Clients can use the fromUser parameter
       * to distinguish user-initiated changes from those that occurred programmatically.
       *
       * @param seekBar The SeekBar whose progress has changed
       * @param progress The current progress level. This will be in the range min..max where min
       * and max were set by [ProgressBar.setMin] and
       * [ProgressBar.setMax], respectively. (The default values for
       * min is 0 and max is 100.)
       * @param fromUser True if the progress change was initiated by the user.
       */
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        navigationRailView?.itemIconSize = (
          TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, progress.toFloat(), displayMetrics)
            .toInt()
          )
        iconSizeTv.text = progress.toString() + "dp"
      }

      /**
       * Notification that the user has started a touch gesture. Clients may want to use this
       * to disable advancing the seekbar.
       * @param seekBar The SeekBar in which the touch gesture began
       */
      override fun onStartTrackingTouch(seekBar: SeekBar?) {
      }

      /**
       * Notification that the user has finished a touch gesture. Clients may want to use this
       * to re-enable advancing the seekbar.
       * @param seekBar The SeekBar in which the touch gesture began
       */
      override fun onStopTrackingTouch(seekBar: SeekBar?) {
      }

    })
  }
}
