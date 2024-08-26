package io.material.catalog.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.ViewUtils
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.windowpreferences.WindowPreferencesManager

class BottomSheetScrollableContentDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view =
      inflater.inflate(R.layout.cat_bottomsheet_scrollable_content_fragment, container, false)
    val button = view.findViewById<View>(R.id.bottomsheet_button)
    button.setOnClickListener {
      BottomSheet().show(parentFragmentManager, "")
    }
    return view
  }

  class BottomSheet : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val bottomSheetDialog = BottomSheetDialog(
        requireContext(),
        R.style.ThemeOverlay_Catalog_BottomSheetDialog_Scrollable
      )
      WindowPreferencesManager(requireContext()).applyEdgeToEdgePreference(bottomSheetDialog.window)
      bottomSheetDialog.setContentView(R.layout.cat_bottomsheet_scrollable_content)
      val bottomSheetInternal = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet)
      BottomSheetBehavior.from(bottomSheetInternal!!).peekHeight = 400
      val bottomSheetContent = bottomSheetInternal.findViewById<View>(R.id.bottom_drawer_2)
      ViewUtils.doOnApplyWindowInsets(
        bottomSheetContent
      ) { view, insets, initialPadding ->
        /**
         * When [set][View.setOnApplyWindowInsetsListener] on a
         * View, this listener method will be called instead of the view's own [ ][View.onApplyWindowInsets] method. The `initialPadding` is the view's
         * original padding which can be updated and will be applied to the view automatically. This
         * method should return a new [WindowInsetsCompat] with any insets consumed.
         */
        //在内部 NestedScrollView 中添加插图，以使边缘到边缘的行为一致 - 即，额外的填充将仅显示在所有内容的底部，即仅当您无法再向下滚动以显示更多内容时。
        bottomSheetContent.setPaddingRelative(
          initialPadding.start,
          initialPadding.top,
          initialPadding.end,
          initialPadding.bottom + insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
        )
        return@doOnApplyWindowInsets insets
      }
      return bottomSheetDialog
    }
  }
}
