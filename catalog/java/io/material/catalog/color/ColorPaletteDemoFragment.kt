package io.material.catalog.color

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

abstract class ColorPaletteDemoFragment : DemoFragment() {
  private var adapter: ColorsAdapter? = null

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(colorsLayoutResId, container, false)
    val rv = view as RecyclerView
    adapter = ColorsAdapter(requireContext(), colorsArrayResId)
    rv.layoutManager = LinearLayoutManager(requireContext())
    rv.addItemDecoration(ColorSectionsItemDecoration(requireContext(),adapter!!))
    rv.adapter = adapter
    return view
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.cat_colors_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.copy_colors){
      val clipboard = requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
      val clip = ClipData.newPlainText("Colors", generateColorsText(adapter!!))
      clipboard.setPrimaryClip(clip)
      Toast.makeText(requireContext(), "Copied colors to clipboard.", Toast.LENGTH_LONG).show()
      return true
    }
    return super.onOptionsItemSelected(item)
  }
  @get:LayoutRes
  protected abstract val colorsLayoutResId: Int
  @get:ArrayRes
  protected abstract val colorsArrayResId: Int
  private fun generateColorsText(adapter: ColorsAdapter): String {
    val colorsText = StringBuilder()
    for (item in adapter.items) {
      if (item is ColorHeaderItem) {
        if (colorsText.isNotEmpty()) {
          colorsText.append("\n")
        }
        colorsText.append(item.displayName).append("\n")
      } else if (item is ColorItem) {
        val value = ContextCompat.getColor(requireContext(), item.colorRes)
        val colorResName = item.colorResName
        val resQualifier =
          if (colorResName.startsWith(ColorHeaderItem.SYSTEM_PREFIX)) "@android:color/" else "@color/"
        colorsText.append(String.format("#%06x", value and 0xFFFFFF)).append("\n")
        colorsText.append(resQualifier).append(colorResName).append("\n")
      }
    }
    return colorsText.toString()
  }
}
