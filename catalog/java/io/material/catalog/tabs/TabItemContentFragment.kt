package io.material.catalog.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.material.catalog.R

class TabItemContentFragment : Fragment() {
  companion object {
    private val TAB_NUMBER = "tab_number"

    @JvmStatic
    fun newInstance(tabNumber: Int): TabItemContentFragment {
      val args = Bundle().apply {
        putInt(TAB_NUMBER, tabNumber)
      }
      val fragment = TabItemContentFragment()
      fragment.arguments = args
      return fragment
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.tab_item_content_fragment, container, false)
    val tabNumber = arguments?.getInt(TAB_NUMBER)
    view.findViewById<TextView>(R.id.tab_number_textview).text =
      String.format(getString(R.string.cat_tab_item_content), tabNumber)
    return view
  }
}
