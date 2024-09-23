package io.material.catalog.textfield.non_material

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.Slider
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TextViewMainDemoFragment : DemoFragment() {
  private lateinit var shadowTextView: TextView
  private val shadowColor = Color.RED
  private val textColor = Color.GREEN
  private lateinit var dialog: BottomSheetDialog
//  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//    super.onViewCreated(view, savedInstanceState)
////    requireActivity().addMenuProvider(object : MenuProvider {
////      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
////        menuInflater.inflate(R.menu.configure_menu, menu)
////      }
////
////      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
////        if (menuItem.itemId == R.id.configure) {
////          dialog.show()
////          return true
////        }
////        return false
////      }
////    }, this)
//  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater.inflate(R.menu.configure_menu, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.configure) {
      dialog.show()
      return true
    } else {
      return super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.non_material_textview_main_demo, container, false)
    view.findViewById<TextView>(R.id.marquee_text_view).requestFocus()
    shadowTextView = view.findViewById(R.id.shadow_text_view)
    shadowTextView.setTextColor(textColor)
    initConfigDialog(view)
    return view
  }

  private fun initConfigDialog(view: View) {
    dialog = BottomSheetDialog(view.context).apply {
      setContentView(R.layout.shadow_controller_layout)
    }
    val shadowDxSlider = dialog.findViewById<Slider>(R.id.shadow_dx_slider) ?: return
    val shadowDySlider = dialog.findViewById<Slider>(R.id.shadow_dy_slider) ?: return
    val shadowRadiusSlider = dialog.findViewById<Slider>(R.id.shadow_radius_slider) ?: return
    shadowDxSlider.addOnChangeListener { slider, value, fromUser ->
      shadowTextView.setShadowLayer(
        shadowRadiusSlider.value,
        value,
        shadowDySlider.value,
        shadowColor
      )
    }
    shadowDySlider.addOnChangeListener { slider, value, fromUser ->
      shadowTextView.setShadowLayer(
        shadowRadiusSlider.value,
        shadowDxSlider.value,
        value,
        shadowColor
      )
    }
    shadowRadiusSlider.addOnChangeListener { slider, value, fromUser ->
      shadowTextView.setShadowLayer(
        value,
        shadowDxSlider.value,
        shadowDySlider.value,
        shadowColor
      )
    }
  }
}
