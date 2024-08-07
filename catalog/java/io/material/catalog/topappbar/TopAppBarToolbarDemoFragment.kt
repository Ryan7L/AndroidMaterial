package io.material.catalog.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

private const val NAVIGATION_ICON_RES_ID = R.drawable.ic_close_vd_theme_24px
private const val MENU_RES_ID = R.menu.cat_topappbar_menu

class TopAppBarToolbarDemoFragment : DemoFragment() {
  private val configurationViewData = ConfigurationViewData()
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_topappbar_toolbar_fragment, container, false)
    val toolbars = DemoUtils.findViewsWithType(view, MaterialToolbar::class.java)
    toolbars.forEach {
      initToolbar(view, it)
    }
    return view
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.cat_topappbar_configure_toolbars_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == R.id.configure_toolbars) {
      BottomSheetDialog(requireContext()).apply {
        setContentView(createConfigureToolbarsView(this))
        show()
      }
      true
    } else {
      @Suppress("DEPRECATION")
      super.onOptionsItemSelected(item)
    }
  }

  private fun initToolbar(rootView: View, toolbar: MaterialToolbar) {
    toolbar.setNavigationIcon(NAVIGATION_ICON_RES_ID)
    toolbar.setNavigationOnClickListener {
      showSnackBar(rootView, "${toolbar.subtitle} ,${toolbar.title}")
    }
    toolbar.inflateMenu(MENU_RES_ID)
    toolbar.setOnMenuItemClickListener {
      showSnackBar(rootView, it.title.toString())
      true
    }
  }

  private fun createConfigureToolbarsView(bottomSheetDialog: BottomSheetDialog): View {
    val view = LayoutInflater.from(requireContext())
      .inflate(R.layout.cat_topappbar_configure_toolbars, (requireView() as ViewGroup), false)
    val holder = ConfigureViewHolder(view).apply {
      titleEditText.setText(configurationViewData.titleText)
      titleCenteredCheckBox.isChecked = configurationViewData.titleCentered
      subtitleEditText.setText(configurationViewData.subtitleText)
      subtitleCenteredCheckBox.isChecked = configurationViewData.subtitleCentered
      navigationIconCheckBox.isChecked = configurationViewData.navigationIcon
      menuItemsCheckBox.isChecked = configurationViewData.menuItems

    }
    view.findViewById<View>(R.id.apply_button).setOnClickListener {
      applyToolbarConfigurations(holder)
      bottomSheetDialog.dismiss()
    }
    view.findViewById<View>(R.id.cancel_button).setOnClickListener {
      bottomSheetDialog.dismiss()
    }
    return view
  }

  private fun applyToolbarConfigurations(holder: ConfigureViewHolder) {
    configurationViewData.titleText = holder.titleEditText.text
    configurationViewData.titleCentered = holder.titleCenteredCheckBox.isChecked
    configurationViewData.subtitleText = holder.subtitleEditText.text
    configurationViewData.subtitleCentered = holder.subtitleCenteredCheckBox.isChecked
    configurationViewData.navigationIcon = holder.navigationIconCheckBox.isChecked
    configurationViewData.menuItems = holder.menuItemsCheckBox.isChecked
    val toolbars = DemoUtils.findViewsWithType(requireView(), MaterialToolbar::class.java)
    toolbars.forEach {
      if (configurationViewData.titleText.isNotEmpty()) {
        it.setTitle(configurationViewData.titleText)
      }
      it.isTitleCentered = configurationViewData.titleCentered
      if (configurationViewData.subtitleText.isNotEmpty()) {
        it.setSubtitle(configurationViewData.subtitleText)
      }
      it.isSubtitleCentered = configurationViewData.subtitleCentered
      if (configurationViewData.navigationIcon) {
        it.setNavigationIcon(NAVIGATION_ICON_RES_ID)
      } else {
        it.navigationIcon = null
      }
      it.menu.clear()
      if (configurationViewData.menuItems) {
        it.inflateMenu(MENU_RES_ID)
      }
    }

  }

  private fun showSnackBar(view: View, text: CharSequence) {
    Snackbar.make(
      view, text, Snackbar.LENGTH_SHORT

    )
  }
}

data class ConfigurationViewData(
  var titleText: CharSequence = "",
  var titleCentered: Boolean = false,
  var subtitleText: CharSequence = "",
  var subtitleCentered: Boolean = false,
  var navigationIcon: Boolean = true,
  var menuItems: Boolean = true
)

data class ConfigureViewHolder(val view: View) {
  val titleEditText = view.findViewById<EditText>(R.id.toolbar_title_edittext)
  val titleCenteredCheckBox =
    view.findViewById<CheckBox>(R.id.toolbar_title_centered_checkbox)
  val subtitleEditText = view.findViewById<EditText>(R.id.toolbar_subtitle_edittext)
  val subtitleCenteredCheckBox =
    view.findViewById<CheckBox>(R.id.toolbar_subtitle_centered_checkbox)
  val navigationIconCheckBox =
    view.findViewById<CheckBox>(R.id.toolbar_navigation_icon_checkbox)
  val menuItemsCheckBox = view.findViewById<CheckBox>(R.id.toolbar_menu_items_checkbox)

}
