package io.material.catalog.bottomnav

import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.material.catalog.R

class BottomNavigationLabelVisibilityDemoFragment : BottomNavigationDemoFragment() {

  override fun initBottomNavDemoControls(view: View) {
    super.initBottomNavDemoControls(view)
    initLabelVisibilityModeButtons(view)
    initIconSlider(view)
  }

  override val bottomNavDemoControlsLayout: Int
    get() = R.layout.cat_bottom_navs_label_visibility_controls

  private fun setAllBottomNavsLabelVisibilityMode(labelVisibilityMode: Int) {
    bottomNavigationViews?.forEach {
      setBottomNavsLabelVisibilityMode(it, labelVisibilityMode)
    }
  }

  private fun setBottomNavsLabelVisibilityMode(
    bottomNavigationView: BottomNavigationView,
    labelVisibilityMode: Int
  ) {
    bottomNavigationView.labelVisibilityMode = labelVisibilityMode
  }

  private fun setAllBottomNavsIconSize(size: Int) {
    bottomNavigationViews?.forEach {
      it.itemIconSize = size
    }
  }

  private fun initLabelVisibilityModeButtons(view: View) {
    initLabelVisibilityModeButton(
      view.findViewById(R.id.label_mode_auto_button),
      BottomNavigationView.LABEL_VISIBILITY_AUTO
    )
    initLabelVisibilityModeButton(
      view.findViewById(R.id.label_mode_selected_button),
      BottomNavigationView.LABEL_VISIBILITY_SELECTED
    )
    initLabelVisibilityModeButton(
      view.findViewById(R.id.label_mode_labeled_button),
      BottomNavigationView.LABEL_VISIBILITY_LABELED
    )
    initLabelVisibilityModeButton(
      view.findViewById(R.id.label_mode_unlabeled_button),
      BottomNavigationView.LABEL_VISIBILITY_UNLABELED
    )
  }

  private fun initLabelVisibilityModeButton(
    labelVisibilityModeButton: Button, labelVisibilityMode: Int
  ) {
    labelVisibilityModeButton.setOnClickListener {
      setAllBottomNavsLabelVisibilityMode(labelVisibilityMode)
    }
  }


  private fun initIconSlider(view: View) {
    val iconSizeSlider = view.findViewById<SeekBar>(R.id.icon_size_slider)
    val displayMetrics = resources.displayMetrics
    val iconSizeTextView = view.findViewById<TextView>(R.id.icon_size_text_view)
    val iconSizeUnit = "dp"
    iconSizeSlider.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
      /**
       * 通知进度级别已发生变化。客户端可以使用 fromUser 参数来区分用户发起的更改和以编程方式发生的更改。
       *
       * @param seekBar 进度发生变化的SeekBar
       * @param progress 当前的进度水平。这将在 min..max 范围内，其中 min 和 max 分别由 [ProgressBar.setMin]
       * 和 [ProgressBar.setMax] 设置。 （最小值的默认值为 0，最大值为 100。）
       * @param fromUser 如果进度更改是由用户发起的，则为 True。
       */
      override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        setAllBottomNavsIconSize(
          TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            progress.toFloat(),
            displayMetrics
          ).toInt()
        )
        iconSizeTextView.text = progress.toString() + iconSizeUnit
      }

      /**
       * 通知用户已开始触摸手势。客户端可能希望使用此通知来禁用滚动条的前进。
       * @param seekBar 触摸手势开始的 SeekBar
       */
      override fun onStartTrackingTouch(seekBar: SeekBar?) {
      }

      /**
       * 通知用户已完成触摸手势。客户端可能希望使用此通知重新启用滚动条的推进。
       * @param seekBar 触摸手势开始的 SeekBar
       */
      override fun onStopTrackingTouch(seekBar: SeekBar?) {
      }

    })
  }
}
