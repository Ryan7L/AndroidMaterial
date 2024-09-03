package io.material.catalog.sidesheet

import android.os.Bundle
import android.util.SparseIntArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.BackEventCompat
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.sidesheet.SideSheetBehavior
import com.google.android.material.sidesheet.SideSheetCallback
import com.google.android.material.sidesheet.SideSheetDialog
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.preferences.PreferencesDialogHelper
import io.material.catalog.windowpreferences.WindowPreferencesManager

class SideSheetMainDemoFragment : DemoFragment() {
  private var preferencesDialogHelper: PreferencesDialogHelper? = null
  private val GRAVITY_ID_RES_MAP = SparseIntArray().apply {
    append(R.id.left_gravity_button, Gravity.LEFT)
    append(R.id.right_gravity_button, Gravity.RIGHT)
    append(R.id.start_gravity_button, Gravity.START)
    append(R.id.end_gravity_button, Gravity.END)
  }
  private val sideSheetViews = mutableListOf<View>()
  private lateinit var showModalSheetButton: Button
  private lateinit var showDetachedModalSheetButton: Button
  private lateinit var sheetGravityButtonToggleGroup: MaterialButtonToggleGroup
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //由于演示绘制了自己的操作栏，因此首选项帮助程序以临时方式与工具栏一起使用，以允许侧页达到屏幕高度的 100%。
    preferencesDialogHelper = PreferencesDialogHelper.createHelper(getParentFragmentManager())
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(demoContent, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val sideSheetsContainer = view.findViewById<ViewGroup>(R.id.cat_sidesheet_coordinator_layout)
    View.inflate(context, sideSheetsContent, sideSheetsContainer)
    setUpToolbar(view)
    setUpSheetGravityButtonToggleGroup(view)

    val standardRightSideSheet = setUpSideSheet(
      view,
      R.id.standard_side_sheet_container,
      R.id.show_standard_side_sheet_button,
      R.id.close_icon_button
    )
    setSideSheetCallback(
      standardRightSideSheet,
      R.id.side_sheet_state_text,
      R.id.side_sheet_slide_offset_text
    )

    val detachedStandardSideSheet = setUpSideSheet(
      view,
      R.id.standard_detached_side_sheet_container,
      R.id.show_standard_detached_side_sheet_button,
      R.id.detached_close_icon_button
    )
    setSideSheetCallback(
      detachedStandardSideSheet,
      R.id.detached_side_sheet_state_text,
      R.id.detached_side_sheet_slide_offset_text
    )

    val verticallyScrollingSideSheet = setUpSideSheet(
      view,
      R.id.vertically_scrolling_side_sheet_container,
      R.id.show_vertically_scrolling_side_sheet_button,
      R.id.vertically_scrolling_side_sheet_close_icon_button
    )
    setSideSheetCallback(
      verticallyScrollingSideSheet,
      R.id.vertically_scrolling_side_sheet_state_text,
      R.id.vertically_scrolling_side_sheet_slide_offset_text
    )

    showModalSheetButton = view.findViewById(R.id.show_modal_side_sheet_button)
    setUpModalSheet()

    showDetachedModalSheetButton = view.findViewById(R.id.show_modal_detached_side_sheet_button)
    setUpDetachedModalSheet()

    val coplanarSideSheet = setUpSideSheet(
      view,
      R.id.coplanar_side_sheet_container,
      R.id.show_coplanar_side_sheet_button,
      R.id.coplanar_side_sheet_close_icon_button
    )
    setSideSheetCallback(
      coplanarSideSheet,
      R.id.coplanar_side_sheet_state_text,
      R.id.coplanar_side_sheet_slide_offset_text
    )

    val detachedCoplanarSideSheet = setUpSideSheet(
      view,
      R.id.coplanar_detached_side_sheet_container,
      R.id.show_coplanar_detached_side_sheet_button,
      R.id.coplanar_detached_side_sheet_close_icon_button
    )
    setSideSheetCallback(
      detachedCoplanarSideSheet,
      R.id.coplanar_detached_side_sheet_state_text,
      R.id.coplanar_detached_side_sheet_slide_offset_text
    )

  }

  private fun setUpSheetGravityButtonToggleGroup(view: View) {
    sheetGravityButtonToggleGroup = view.findViewById(R.id.sheet_gravity_button_toggle_group)
    sheetGravityButtonToggleGroup.check(R.id.end_gravity_button)
    sheetGravityButtonToggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (isChecked) {
        val sheetGravity = getGravityForIdRes(checkedId)
        sideSheetViews.forEach {
          val layoutParams = it.layoutParams
          if (layoutParams is CoordinatorLayout.LayoutParams) {
            layoutParams.gravity = sheetGravity
            it.requestLayout()
          }
        }
      }
    }
  }

  private fun setUpBackHandling(sideSheet: View, sideSheetBehavior: SideSheetBehavior<View>) {
    val nonModalOnBackPressedCallback = createNonModalOnBackPressedCallback(sideSheetBehavior)
    requireActivity().onBackPressedDispatcher.addCallback(this, nonModalOnBackPressedCallback)
    sideSheetBehavior.addCallback(object : SideSheetCallback() {
      override fun onStateChanged(sheet: View, newState: Int) {
        updateBackHandlingEnabled(nonModalOnBackPressedCallback, newState)
      }

      override fun onSlide(sheet: View, slideOffset: Float) {

      }
    })
    sideSheet.post {
      updateBackHandlingEnabled(nonModalOnBackPressedCallback, sideSheetBehavior.state)
    }
  }

  private fun updateBackHandlingEnabled(onBackPressedCallback: OnBackPressedCallback, state: Int) {
    when (state) {
      SideSheetBehavior.STATE_EXPANDED, SideSheetBehavior.STATE_SETTLING -> onBackPressedCallback.isEnabled =
        true

      SideSheetBehavior.STATE_HIDDEN -> onBackPressedCallback.isEnabled = false
      else -> {}
    }
  }

  private fun setUpSideSheet(
    view: View,
    sideSheetContainerId: Int,
    showSideSheetButtonId: Int,
    closeIconButtonId: Int
  ): View {
    val sideSheet = view.findViewById<View>(sideSheetContainerId)
    val behavior = SideSheetBehavior.from(sideSheet)
    val showBtn = view.findViewById<Button>(showSideSheetButtonId)
    showBtn.setOnClickListener {
      showSideSheet(behavior)
    }
    val closeIconBtn = sideSheet.findViewById<View>(closeIconButtonId)
    closeIconBtn.setOnClickListener {
      hideSideSheet(behavior)
    }
    setUpBackHandling(sideSheet, behavior)
    sideSheetViews.add(sideSheet)
    return sideSheet
  }

  private fun setSideSheetCallback(sideSheet: View, stateTvId: Int, slideOffsetTvId: Int) {
    val sideSheetBehavior = SideSheetBehavior.from(sideSheet)
    val stateTv = sideSheet.findViewById<TextView>(stateTvId)
    val slideOffsetTv = sideSheet.findViewById<TextView>(slideOffsetTvId)
    sideSheetBehavior.addCallback(createSideSheetCallback(stateTv, slideOffsetTv))
    sideSheet.post {
      updateStateTextView(stateTv, sideSheetBehavior.state)
    }
  }

  private fun setUpDetachedModalSheet() {
    setUpModalSheet(
      getDetachedModalThemeOverlayResId(),
      R.layout.cat_sidesheet_content,
      R.id.m3_side_sheet,
      R.id.side_sheet_state_text,
      R.string.cat_sidesheet_modal_detached_title,
      showDetachedModalSheetButton,
      R.id.close_icon_button
    )
  }

  private fun setUpModalSheet() {
    setUpModalSheet(
      R.layout.cat_sidesheet_content, R.id.m3_side_sheet, R.id.side_sheet_title_text,
      R.string.cat_sidesheet_modal_title,
      showModalSheetButton,
      R.id.close_icon_button
    )
  }

  private fun setUpModalSheet(
    sheetContentLayoutRes: Int,
    sheetContentRootIdRes: Int,
    sheetTitleIdRes: Int,
    sheetTitleStringRes: Int,
    showSheetButton: Button,
    closeIconButtonIdRes: Int
  ) {
    setUpModalSheet(
      View.NO_ID,
      sheetContentLayoutRes,
      sheetContentRootIdRes,
      sheetTitleIdRes,
      sheetTitleStringRes,
      showSheetButton,
      closeIconButtonIdRes
    )
  }

  private fun setUpModalSheet(
    sheetThemeOverlayRes: Int,
    sheetContentLayoutRes: Int,
    sheetContentRootIdRes: Int,
    sheetTitleIdRes: Int,
    sheetTitleStringRes: Int,
    showSheetButton: Button,
    closeIconButtonIdRes: Int
  ) {
    showSheetButton.setOnClickListener {
      val sheetDialog = if (sheetThemeOverlayRes == View.NO_ID) {
        SideSheetDialog(requireContext())
      } else {
        SideSheetDialog(requireContext(), sheetThemeOverlayRes)
      }
      sheetDialog.setContentView(sheetContentLayoutRes)
      val modalSheetContent = sheetDialog.findViewById<View>(sheetContentRootIdRes)
      modalSheetContent?.let { v ->
        val modalSideSheetTitle = v.findViewById<TextView>(sheetTitleIdRes)
        modalSideSheetTitle.setText(sheetTitleStringRes)
      }
      WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(sheetDialog.window)
      sheetDialog.behavior.addCallback(
        createSideSheetCallback(
          sheetDialog.findViewById<TextView>(R.id.side_sheet_state_text),
          sheetDialog.findViewById<TextView>(R.id.side_sheet_slide_offset_text)
        )
      )
      sheetDialog.setSheetEdge(getGravityForIdRes(sheetGravityButtonToggleGroup.checkedButtonId))
      val modalSideSheetCloseIconBtn = sheetDialog.findViewById<View>(closeIconButtonIdRes)
      modalSideSheetCloseIconBtn?.setOnClickListener {
        sheetDialog.hide()
      }
      sheetDialog.show()
    }
  }


  private fun setUpToolbar(view: View) {
    val toolbar = ViewCompat.requireViewById<Toolbar>(view, R.id.toolbar)
    (activity as? AppCompatActivity)?.let {
      if (preferencesDialogHelper != null) {
        preferencesDialogHelper!!.onCreateOptionsMenu(toolbar.menu, it.menuInflater)
        toolbar.setOnMenuItemClickListener { id ->
          preferencesDialogHelper!!.onOptionsItemSelected(id)
        }
      }
    }
  }

  private fun showSideSheet(behavior: SideSheetBehavior<View>) {
    behavior.expand()
  }

  private fun hideSideSheet(behavior: SideSheetBehavior<View>) {
    behavior.hide()
  }

  private val demoContent = R.layout.cat_sidesheet_fragment

  private val sideSheetsContent = R.layout.cat_sidesheets

  private fun getDetachedModalThemeOverlayResId(): Int {
    return R.style.ThemeOverlay_Catalog_SideSheet_Modal_Detached
  }

  private fun getGravityForIdRes(gravityButtonIdRes: Int): Int {
    return GRAVITY_ID_RES_MAP.get(gravityButtonIdRes)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

  private fun createSideSheetCallback(
    stateTextView: TextView?,
    slideOffsetTextView: TextView?
  ): SideSheetCallback {
    return object : SideSheetCallback() {
      override fun onStateChanged(sheet: View, newState: Int) {
        updateStateTextView(stateTextView, newState)
      }

      override fun onSlide(sheet: View, slideOffset: Float) {
        slideOffsetTextView?.visibility = View.VISIBLE
        slideOffsetTextView?.text =
          resources.getString(R.string.cat_sidesheet_slide_offset_text, slideOffset)
      }
    }
  }

  private fun updateStateTextView(stateTextView: TextView?, state: Int) {
    stateTextView?.visibility = View.VISIBLE
    when (state) {
      SideSheetBehavior.STATE_DRAGGING -> stateTextView?.setText(R.string.cat_sidesheet_state_dragging)
      SideSheetBehavior.STATE_EXPANDED -> stateTextView?.setText(R.string.cat_sidesheet_state_expanded)
      SideSheetBehavior.STATE_SETTLING -> stateTextView?.setText(R.string.cat_sidesheet_state_settling)
      else -> {}
    }
  }

  private fun createNonModalOnBackPressedCallback(behavior: SideSheetBehavior<View>) =
    object : OnBackPressedCallback(false) {
      override fun handleOnBackStarted(backEvent: BackEventCompat) {
        behavior.startBackProgress(backEvent)
      }

      override fun handleOnBackPressed() {
        behavior.handleBackInvoked()
      }

      override fun handleOnBackCancelled() {
        behavior.cancelBackProgress()
      }

      override fun handleOnBackProgressed(backEvent: BackEventCompat) {
        behavior.updateBackProgress(backEvent)
      }
    }
}
