package io.material.catalog.bottomsheet

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDragHandleView
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.windowpreferences.WindowPreferencesManager

class BottomSheetMainDemoFragment : DemoFragment() {

  private val onBackPressedCallback = object : OnBackPressedCallback(false) {
    /**
     * Callback for handling the [OnBackPressedDispatcher.onBackPressed] event.
     */
    override fun handleOnBackPressed() {
      persistentBottomSheetBehavior?.handleBackInvoked()
    }

    override fun handleOnBackCancelled() {
      persistentBottomSheetBehavior?.cancelBackProgress()
    }

    override fun handleOnBackProgressed(backEvent: BackEventCompat) {
      persistentBottomSheetBehavior?.updateBackProgress(backEvent)
    }

    override fun handleOnBackStarted(backEvent: BackEventCompat) {
      persistentBottomSheetBehavior?.startBackProgress(backEvent)
    }
  }
  private var windowPreferenceManager: WindowPreferencesManager? = null

  private lateinit var bottomSheetDialog: BottomSheetDialog

  private var persistentBottomSheetBehavior: BottomSheetBehavior<View>? = null

  private var windowInsets: WindowInsetsCompat? = null

  private var peekHeightPx = 0

  private lateinit var fullScreenSwitch: MaterialSwitch

  private lateinit var restrictExpansionSwitch: MaterialSwitch

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    windowPreferenceManager = WindowPreferencesManager(requireContext())
    peekHeightPx = resources.getDimensionPixelSize(R.dimen.cat_bottom_sheet_peek_height)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(demoContent, container, false)
    val content = view.findViewById<ViewGroup>(R.id.cat_bottomsheet_coordinator_layout)
    content.addView(inflater.inflate(standardBottomSheetLayout, content, false))

    bottomSheetDialog = BottomSheetDialog(requireContext())
    bottomSheetDialog.setContentView(R.layout.cat_bottomsheet_content)
    //选择在关闭底部工作表对话框时执行滑动以关闭动画。
    bottomSheetDialog.dismissWithAnimation = true
    windowPreferenceManager?.applyEdgeToEdgePreference(bottomSheetDialog.window)
    val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from(bottomSheetInternal!!).peekHeight = peekHeightPx
    val button = view.findViewById<View>(R.id.bottomsheet_button)
    button.setOnClickListener {
      bottomSheetDialog.show()
      bottomSheetDialog.setTitle(getText(R.string.cat_bottomsheet_title))
      val button0 = bottomSheetInternal.findViewById<Button>(R.id.cat_bottomsheet_modal_button)
      button0.setOnClickListener { v ->
        Toast.makeText(v.context, R.string.cat_bottomsheet_button_clicked, Toast.LENGTH_SHORT)
          .show()
      }
      val enabledSwitch =
        bottomSheetInternal.findViewById<MaterialSwitch>(R.id.cat_bottomsheet_modal_enabled_switch)
      enabledSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
        val updatedText =
          if (isChecked) R.string.cat_bottomsheet_button_label_enabled else R.string.cat_bottomsheet_button_label_disabled
        button0.setText(updatedText)
        button0.isEnabled = isChecked
      }
    }
    fullScreenSwitch = view.findViewById(R.id.cat_fullscreen_switch)
    fullScreenSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      updateBottomSheetHeights()
    }
    restrictExpansionSwitch = view.findViewById(R.id.cat_bottomsheet_expansion_switch)
    restrictExpansionSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      fullScreenSwitch.isEnabled = !isChecked
      view.findViewById<BottomSheetDragHandleView>(R.id.drag_handle).isEnabled = !isChecked
      bottomSheetInternal.findViewById<BottomSheetDragHandleView>(R.id.drag_handle).isEnabled =
        !isChecked
      updateBottomSheetHeights()
    }
    val dialogText = bottomSheetInternal.findViewById<TextView>(R.id.bottomsheet_state)
    BottomSheetBehavior.from(bottomSheetInternal)
      .addBottomSheetCallback(createBottomSheetCallback(dialogText))
    val bottomSheetText = view.findViewById<TextView>(R.id.cat_persistent_bottomsheet_state)
    val bottomSheetPersistent = view.findViewById<View>(R.id.bottom_drawer)
    persistentBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetPersistent)
    persistentBottomSheetBehavior?.addBottomSheetCallback(createBottomSheetCallback(bottomSheetText))
    bottomSheetPersistent.post {
      val state = persistentBottomSheetBehavior?.state
      updateStateTextView(bottomSheetPersistent, bottomSheetText, state!!)
      updateBackHandlingEnabled(state)
    }
    val button1 = view.findViewById<Button>(R.id.cat_bottomsheet_button)
    button1.setOnClickListener { v ->
      Toast.makeText(v.context, R.string.cat_bottomsheet_button_clicked, Toast.LENGTH_SHORT).show()
    }
    val enabledSwitch = view.findViewById<MaterialSwitch>(R.id.cat_bottomsheet_enabled_switch)
    enabledSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      val updatedText =
        if (isChecked) R.string.cat_bottomsheet_button_label_enabled else R.string.cat_bottomsheet_button_label_disabled
      button1.setText(updatedText)
      button1.isEnabled = isChecked
    }
    ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
      windowInsets = insets
      updateBottomSheetHeights()
      return@setOnApplyWindowInsetsListener insets
    }
    return view
  }

  private val bottomSheetDialogDefaultHeight: Int
    get() = windowHeight * 2 / 3

  private val bottomSheetPersistentDefaultHeight: Int
    get() = windowHeight * 3 / 5

  private fun updateBottomSheetHeights() {
    val bottomSheetChildView = view!!.findViewById<View>(R.id.bottom_drawer)
    val params = bottomSheetChildView.layoutParams
    val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetChildView)
    bottomSheetBehavior.setUpdateImportantForAccessibilityOnSiblings(fullScreenSwitch.isChecked)
    val modalBottomSheetChildView = bottomSheetDialog.findViewById<View>(R.id.bottom_drawer_2)
    val layoutParams = modalBottomSheetChildView?.layoutParams
    val modalBottomSheetBehavior = bottomSheetDialog.behavior
    var fitToContents = true
    var halfExpandedRatio = 0.5f
    val windowHeight = windowHeight
    if (params != null && layoutParams != null) {
      if (fullScreenSwitch.isEnabled && fullScreenSwitch.isChecked) {
        params.height = windowHeight
        layoutParams.height = windowHeight
        fitToContents = false
        halfExpandedRatio = 0.7f
      } else if (restrictExpansionSwitch.isEnabled && restrictExpansionSwitch.isChecked) {
        params.height = peekHeightPx
        layoutParams.height = peekHeightPx
      } else {
        params.height = bottomSheetPersistentDefaultHeight
        layoutParams.height = bottomSheetDialogDefaultHeight
      }
      bottomSheetChildView.layoutParams = params
      modalBottomSheetChildView.layoutParams = layoutParams
      bottomSheetBehavior.isFitToContents = fitToContents
      modalBottomSheetBehavior.isFitToContents = fitToContents
      bottomSheetBehavior.halfExpandedRatio = halfExpandedRatio
      modalBottomSheetBehavior.halfExpandedRatio = halfExpandedRatio
    }


  }

  private val windowHeight: Int
    get() {
      // 计算全屏使用的窗口高度
      val displayMetrics = DisplayMetrics()
      (context as? Activity)?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
      // 允许全屏 BottomSheet 扩展到系统窗口之外并在状态栏下绘制。
      var height = displayMetrics.heightPixels
      if (windowInsets != null) {
        height += windowInsets!!.getInsets(WindowInsetsCompat.Type.systemBars()).top
        height += windowInsets!!.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
      }
      return height
    }

  @LayoutRes
  private val demoContent = R.layout.cat_bottomsheet_fragment

  @LayoutRes
  private val standardBottomSheetLayout = R.layout.cat_bottomsheet_standard

  private fun createBottomSheetCallback(textView: TextView): BottomSheetCallback {
    return object : BottomSheetCallback() {
      /**
       * 当底部工作表更改其状态时调用。
       *
       * @param bottomSheet 底部工作表视图.
       * @param newState 新的状态。这将是 [.STATE_DRAGGING]、[ ][.STATE_SETTLING]、[.STATE_EXPANDED]、[.STATE_COLLAPSED]、[ ][.STATE_HIDDEN] 或 [.STATE_HALF_EXPANDED] 之一。
       */
      override fun onStateChanged(bottomSheet: View, newState: Int) {
        updateStateTextView(bottomSheet, textView, newState)
      }

      /**
       * 当拖动底部工作表时调用。
       *
       * @param bottomSheet 底部工作表视图。
       * @param slideOffset 该底部工作表在 [-1,1] 范围内的新偏移量。当该底板向上移动时，偏移量增加。从 0 到 1，工作表处于折叠和展开状态之间，从 -1 到 0，工作表处于隐藏和折叠状态之间。
       */
      override fun onSlide(bottomSheet: View, slideOffset: Float) {
      }

    }
  }

  private fun updateStateTextView(bottomSheet: View, textView: TextView, state: Int) {
    when (state) {
      BottomSheetBehavior.STATE_DRAGGING -> textView.setText(R.string.cat_bottomsheet_state_dragging)
      BottomSheetBehavior.STATE_EXPANDED -> textView.setText(R.string.cat_bottomsheet_state_expanded)
      BottomSheetBehavior.STATE_COLLAPSED -> textView.setText(R.string.cat_bottomsheet_state_collapsed)
      BottomSheetBehavior.STATE_HALF_EXPANDED -> {
        val behavior = BottomSheetBehavior.from(bottomSheet)
        textView.text = getString(
          R.string.cat_bottomsheet_state_half_expanded,
          behavior.halfExpandedRatio
        )
      }

      else -> {}
    }

  }

  private fun setUpBackHandling(behavior: BottomSheetBehavior<View>) {
    requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    behavior.addBottomSheetCallback(object : BottomSheetCallback() {

      override fun onStateChanged(bottomSheet: View, newState: Int) {
        updateBackHandlingEnabled(newState)
      }


      override fun onSlide(bottomSheet: View, slideOffset: Float) {
      }

    })
  }

  private fun updateBackHandlingEnabled(state: Int) {
    when (state) {
      BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED -> onBackPressedCallback.isEnabled =
        true

      BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> onBackPressedCallback.isEnabled =
        false

      else -> {}
    }
  }

}
