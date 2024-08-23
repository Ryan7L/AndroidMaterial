package io.material.catalog.bottomappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.navigation.NavigationView
import com.google.android.material.shape.CutCornerTreatment
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils
import io.material.catalog.preferences.PreferencesDialogHelper

class BottomAppBarMainDemoFragment : DemoFragment() {

  private lateinit var bar: BottomAppBar
  private lateinit var coordinatorLayout: CoordinatorLayout
  private lateinit var fab: FloatingActionButton
  private lateinit var barNabView: View

  private var preferencesDialogHelper: PreferencesDialogHelper? = null

  private var bottomDrawerBehavior: BottomSheetBehavior<View>? = null


  private val bottomDrawerOnBackPressedCallback = object : OnBackPressedCallback(false) {
    /**
     * 用于处理 [OnBackPressedDispatcher.onBackPressed] 事件的回调。
     */
    override fun handleOnBackPressed() {
      bottomDrawerBehavior?.handleBackInvoked()
    }

    /**
     * 用于处理系统 UI 生成的回调相当于 [OnBackPressedDispatcher.dispatchOnBackStarted]。
     *
     * 这只会由 API 34 及更高版本的框架调用。
     */
    override fun handleOnBackStarted(backEvent: BackEventCompat) {
      bottomDrawerBehavior?.startBackProgress(backEvent)
    }

    /**
     * 用于处理生成的系统 UI 的回调相当于 [OnBackPressedDispatcher.dispatchOnBackProgressed]。
     *
     * 这只会由 API 34 及更高版本的框架调用。
     */
    override fun handleOnBackProgressed(backEvent: BackEventCompat) {
      bottomDrawerBehavior?.updateBackProgress(backEvent)
    }

    /**
     * 用于处理系统 UI 生成的回调相当于 [OnBackPressedDispatcher.dispatchOnBackCancelled]
     *
     * 这只会由 API 34 及更高版本的框架调用。
     */
    override fun handleOnBackCancelled() {
      bottomDrawerBehavior?.cancelBackProgress()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setHasOptionsMenu(true)
    //由于 BottomAppBar 被设置为操作栏，因此首选项帮助器以临时方式与工具栏一起使用。
    preferencesDialogHelper = PreferencesDialogHelper.createHelper(parentFragmentManager)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_bottomappbar_fragment, container, false)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    toolbar.title = defaultDemoTitle
    preferencesDialogHelper?.onCreateOptionsMenu(toolbar.menu, requireActivity().menuInflater)
    toolbar.setOnMenuItemClickListener {
      preferencesDialogHelper?.onOptionsItemSelected(it) ?: false
    }
    toolbar.setNavigationOnClickListener {
      activity?.onBackPressed()
    }
    coordinatorLayout = view.findViewById(R.id.coordinator_layout)
    bar = view.findViewById(R.id.bar)
    (activity as? AppCompatActivity)?.setSupportActionBar(bar)
    barNabView = bar.getChildAt(0)
    setUpBottomDrawer()
    fab = view.findViewById(R.id.fab)
    fab.setOnClickListener {
      showSnackBar(fab.contentDescription.toString())
    }
    val navigationView = view.findViewById<NavigationView>(R.id.navigation_view)
    navigationView.setNavigationItemSelectedListener {
      showSnackBar(it.title.toString())
      false
    }
    setUpDemoControls(view)
    setUpBottomAppBarShapeAppearance()
    return view
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.demo_primary_alternate, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    showSnackBar(item.title.toString())
    return true
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpFabConfig()
  }

  private fun setUpDemoControls(view: View) {
    // 设置切换按钮组的通用设置。
    val toggleButtonGroups =
      DemoUtils.findViewsWithType(view, MaterialButtonToggleGroup::class.java)
    toggleButtonGroups.forEach {
      it.isSingleSelection = true
      it.isSelectionRequired = true
    }

    val showFab = view.findViewById<MaterialButton>(R.id.show_fab_button)
    val hideFab = view.findViewById<MaterialButton>(R.id.hide_fab_button)
    if (fab.visibility == View.VISIBLE) {
      showFab.isChecked = true
    } else {
      hideFab.isChecked = true
    }
    showFab.setOnClickListener {
      fab.show()
    }
    hideFab.setOnClickListener {
      fab.hide()
    }

    val barScrollSwitch = view.findViewById<MaterialSwitch>(R.id.bar_scroll_switch)
    barScrollSwitch.isChecked = bar.hideOnScroll
    barScrollSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      bar.hideOnScroll = isChecked
    }
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

  private fun setUpFabConfig() {
    val fabAlignmentMode = view?.findViewById<MaterialButtonToggleGroup>(R.id.fabLocationModeGroup)
    val fabAnchorMode = view?.findViewById<MaterialButtonToggleGroup>(R.id.fabAnchorModeGroup)
    fabAlignmentMode?.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (isChecked) {
        when (checkedId) {
          R.id.locationEnd -> bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
          R.id.locationCenter -> bar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
        }
      }
    }
    fabAnchorMode?.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (isChecked) {
        when (checkedId) {
          R.id.anchorModeEmbed -> bar.fabAnchorMode = BottomAppBar.FAB_ANCHOR_MODE_EMBED
          R.id.anchorModeCradle -> bar.fabAnchorMode = BottomAppBar.FAB_ANCHOR_MODE_CRADLE
        }
        setUpBottomAppBarShapeAppearance()
      }
    }
  }

  private fun setUpBottomAppBarShapeAppearance() {
//    fab.shapeAppearanceModel = 圆形fab
//      fab.shapeAppearanceModel.toBuilder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
    val fabShapeAppearanceModel = fab.shapeAppearanceModel
    val isCutCornersFab = fabShapeAppearanceModel.bottomLeftCorner is CutCornerTreatment
      && fabShapeAppearanceModel.bottomRightCorner is CutCornerTreatment

    val topEdge = if (isCutCornersFab) {
      BottomAppBarCutCornersTopEdge(
        bar.fabCradleMargin,
        bar.fabCradleRoundedCornerRadius,
        bar.cradleVerticalOffset
      )
    } else {
      BottomAppBarTopEdgeTreatment(
        bar.fabCradleMargin,
        bar.fabCradleRoundedCornerRadius,
        bar.cradleVerticalOffset
      )
    }
    val background = bar.background as MaterialShapeDrawable
    background.shapeAppearanceModel =
      background.shapeAppearanceModel.toBuilder().setTopEdge(topEdge).build()
  }

  private fun setUpBottomDrawer() {
    val bottomDrawer = coordinatorLayout.findViewById<View>(R.id.bottom_drawer)
    bottomDrawerBehavior = BottomSheetBehavior.from(bottomDrawer)
    bottomDrawerBehavior?.let {
      it.setUpdateImportantForAccessibilityOnSiblings(true)
      it.state = BottomSheetBehavior.STATE_HIDDEN
      bottomDrawer.post {
        updateBackHandlingEnabled(it.state)
      }
      it.addBottomSheetCallback(object : BottomSheetCallback() {
        /**
         * 当BottomSheet更改其状态时调用。
         *
         * @param bottomSheet BottomSheet
         * @param newState 新的状态。这将是 [.STATE_DRAGGING]、[ ][.STATE_SETTLING]、[.STATE_EXPANDED]、[.STATE_COLLAPSED]、[ ][.STATE_HIDDEN] 或 [.STATE_HALF_EXPANDED] 之一。
         */
        override fun onStateChanged(bottomSheet: View, newState: Int) {
          updateBackHandlingEnabled(newState)
          if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            barNabView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
          }
        }

        /**
         * 当拖动BottomSheet时调用。
         *
         * @param bottomSheet BottomSheet
         * @param slideOffset 该BottomSheet的新偏移量在 [-1,1] 范围内。当该底板向上移动时，偏移量增加。从 0 到 1，Sheet处于折叠和展开状态之间，从 -1 到 0，Sheet处于隐藏和折叠状态之间。
         */
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

      })
    }


    requireActivity().onBackPressedDispatcher.addCallback(this, bottomDrawerOnBackPressedCallback)
    bar.setNavigationOnClickListener {
      bottomDrawerBehavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }
  }

  private fun updateBackHandlingEnabled(state: Int) {
    when (state) {
      BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_HALF_EXPANDED, BottomSheetBehavior.STATE_COLLAPSED -> {
        bottomDrawerOnBackPressedCallback.isEnabled = true
      }

      BottomSheetBehavior.STATE_HIDDEN -> bottomDrawerOnBackPressedCallback.isEnabled = false
      else -> {
        // 不执行任何操作，仅更改为“稳定”状态启用的回调。
      }
    }
  }

  private fun showSnackBar(text: String) {
    Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT)
      .setAnchorView(if (fab.visibility == View.VISIBLE) fab else bar)
      .show()
  }
}
