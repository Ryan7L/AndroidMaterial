package io.material.catalog.shapetheming

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StyleRes
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

abstract class ShapeThemingDemoFragment : DemoFragment() {
  private var statusBarColor: Int = 0
  private var wrappedContext: ContextThemeWrapper? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    this.wrappedContext = ContextThemeWrapper(context, shapeTheme)
    val layoutInflater = inflater.cloneInContext(wrappedContext)
    statusBarColor = activity?.window?.statusBarColor ?: 0
    val value = TypedValue()
    wrappedContext?.theme?.resolveAttribute(R.attr.colorPrimaryDark, value, true)
    activity?.window?.statusBarColor = value.data

    return super.onCreateView(layoutInflater, container, savedInstanceState)
  }

  override fun onDestroyView() {
    activity?.window?.statusBarColor = statusBarColor

    super.onDestroyView()
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_shape_theming_container, container, false)
    val content = view.findViewById<ViewGroup>(R.id.container)
    inflater.inflate(R.layout.cat_shape_theming_content, content, true)
    val button = content.findViewById<MaterialButton>(R.id.material_button)
    val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), shapeTheme)
      .setTitle(R.string.cat_shape_theming_title)
      .setTitle(R.string.cat_shape_theming_dialog_message)
      .setPositiveButton(R.string.cat_shape_theming_dialog_ok, null)
    button.setOnClickListener { dialogBuilder.show() }
    val bottomSheet = BottomSheetDialog(wrappedContext!!)
    bottomSheet.setContentView(R.layout.cat_shape_theming_bottomsheet_content)
    val bottomSheetInternal = bottomSheet.findViewById<View>(R.id.design_bottom_sheet)
    BottomSheetBehavior.from(bottomSheetInternal!!).peekHeight = 300
    val btn = content.findViewById<MaterialButton>(R.id.material_button_2)
    btn.setOnClickListener { bottomSheet.show() }
    return view
  }


  @get:StyleRes
  protected abstract val shapeTheme: Int
}
