package io.material.catalog.fab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.LayoutRes
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ExtendedFabDemoFragment : DemoFragment() {
  private var showFabs = true
  private var expandedFabs = true
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_extended_fab_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val content = view.findViewById<ViewGroup>(R.id.content)
    View.inflate(context, extendedFabContent, content)
    val extendedFabs = DemoUtils.findViewsWithType(view, ExtendedFloatingActionButton::class.java)
    extendedFabs.forEach {
      it.setOnClickListener { v ->
        Snackbar.make(v, R.string.cat_extended_fab_clicked, Snackbar.LENGTH_SHORT).show()
      }
    }
    val showHideBtn = view.findViewById<Button>(R.id.show_hide_fabs)
    showHideBtn.setOnClickListener {
      extendedFabs.forEach {
        if (showFabs) {
          it.hide()
          showHideBtn.setText(R.string.show_fabs_label)
        } else {
          it.show()
          showHideBtn.setText(R.string.hide_fabs_label)
        }
      }
      showFabs = !showFabs
    }
    val collapseExpandBtn = view.findViewById<Button>(R.id.collapse_expand_fabs)
    collapseExpandBtn.setOnClickListener {
      extendedFabs.forEach {
        if (expandedFabs) {
          it.shrink()
          collapseExpandBtn.setText(R.string.extend_fabs_label)
        } else {
          it.extend()
          collapseExpandBtn.setText(R.string.shrink_fabs_label)
        }
      }
      expandedFabs = !expandedFabs
    }

  }

  @get:LayoutRes
  private val extendedFabContent: Int
    get() = R.layout.m3_extended_fabs
}
