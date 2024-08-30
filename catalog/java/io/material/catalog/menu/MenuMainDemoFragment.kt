package io.material.catalog.menu

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.style.BackgroundColorSpan
import android.util.TypedValue
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class MenuMainDemoFragment : DemoFragment() {
  private val ICON_MARGIN = 8
  private val CLIP_DATA_LABEL = "Sample text to copy"
  private val KEY_POPUP_ITEM_LAYOUT = "popup_item_layout"

  @LayoutRes
  private var popupItemLayout: Int = R.layout.cat_popup_item
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    savedInstanceState?.let {
      popupItemLayout = it.getInt(KEY_POPUP_ITEM_LAYOUT)
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(KEY_POPUP_ITEM_LAYOUT, popupItemLayout)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.popup_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_menu_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val btn = view.findViewById<Button>(R.id.menu_button)
    val iconBtn = view.findViewById<Button>(R.id.menu_button_with_icons)
    btn.setOnClickListener { showMenu(it, R.menu.popup_menu) }
    iconBtn.setOnClickListener { showMenu(it, R.menu.menu_with_icons) }
    val tv = view.findViewById<TextView>(R.id.context_menu_tv)
    registerForContextMenu(tv)
    val listPopWindowBtn = view.findViewById<Button>(R.id.list_popup_window)
    val listPopupWindow = initializeListPopupMenu(listPopWindowBtn)
    listPopWindowBtn.setOnClickListener { listPopupWindow.show() }
  }

  private fun showMenu(v: View, menuRes: Int) {
    val popup = PopupMenu(requireContext(), v)
    popup.menuInflater.inflate(menuRes, popup.menu)
    if (popup.menu is MenuBuilder) {
      val builder = popup.menu as MenuBuilder
      builder.setOptionalIconsVisible(true)
      builder.visibleItems.forEach {
        val iconMarginPx = TypedValue.applyDimension(
          TypedValue.COMPLEX_UNIT_DIP,
          ICON_MARGIN.toFloat(),
          resources.displayMetrics
        ).toInt()
        if (it.icon != null) {
          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            it.icon = InsetDrawable(it.icon, iconMarginPx, 0, iconMarginPx, 0)
          } else {
            it.icon = object : InsetDrawable(it.icon, iconMarginPx, 0, iconMarginPx, 0) {
              override fun getIntrinsicWidth(): Int {
                return intrinsicHeight + iconMarginPx + iconMarginPx
              }
            }
          }
        }

      }
    }
    popup.setOnMenuItemClickListener {
      Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        it.title.toString(),
        Snackbar.LENGTH_LONG
      ).show()
      true
    }
    popup.show()
  }

  override fun onCreateContextMenu(
    menu: ContextMenu,
    v: View,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    val contextMenuTv = v as TextView
    menu.add(android.R.string.copy)
      .setOnMenuItemClickListener {
        val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(
          android.content.ClipData.newPlainText(
            CLIP_DATA_LABEL,
            contextMenuTv.text
          )
        )
        true
      }
    menu.add(R.string.context_menu_highlight)
      .setOnMenuItemClickListener {
        highlightText(contextMenuTv)
        true
      }
  }

  private fun initializeListPopupMenu(v: View): ListPopupWindow {
    val listPopWindow = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
    val adapter = ArrayAdapter<CharSequence>(
      requireContext(),
      popupItemLayout,
      resources.getStringArray(R.array.cat_list_popup_window_content)
    )
    listPopWindow.setAdapter(adapter)
    listPopWindow.anchorView = v
    listPopWindow.setOnItemClickListener { parent, view, position, id ->
      Snackbar.make(
        requireActivity().findViewById(android.R.id.content),
        adapter.getItem(position).toString(),
        Snackbar.LENGTH_LONG
      ).show()
      listPopWindow.dismiss()
    }
    return listPopWindow
  }

  private fun highlightText(tv: TextView) {
    val context = tv.context
    val text = tv.text
    val value = TypedValue()
    context.theme.resolveAttribute(R.attr.colorPrimary, value, true)
    val spanText = Spannable.Factory.getInstance().newSpannable(text)
    spanText.setSpan(
      BackgroundColorSpan(value.data),
      0,
      text.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    tv.text = spanText
  }
}
