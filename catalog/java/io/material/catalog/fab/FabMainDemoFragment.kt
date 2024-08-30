package io.material.catalog.fab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import androidx.annotation.LayoutRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class FabMainDemoFragment : DemoFragment() {
  private var showFabs = true
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_fab_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val content = view.findViewById<ViewGroup>(R.id.content)
    View.inflate(context, fabContent, content)
    View.inflate(context, themeFabLayoutResId, content)
    val fabs = DemoUtils.findViewsWithType(view, FloatingActionButton::class.java)
    fabs.forEach {
      it.setOnClickListener { v ->
        Snackbar.make(it, R.string.cat_fab_clicked, Snackbar.LENGTH_SHORT).show()
      }
    }
    val showHideBtn = view.findViewById<Button>(R.id.show_hide_fabs)
    showHideBtn.setOnClickListener {
      fabs.forEach {
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
    val spinFabBtn = view.findViewById<Button>(R.id.rotate_fabs)
    spinFabBtn.setOnClickListener {
      if (!showFabs) return@setOnClickListener
      fabs.forEach {
        it.rotation = 0f
        it.animate().rotation(360f).withLayer().setDuration(1000)
          .setInterpolator(AccelerateDecelerateInterpolator()).start()
      }
    }
  }

  @get:LayoutRes
  private val fabContent: Int
    get() = R.layout.m3_fabs

  @get:LayoutRes
  private val themeFabLayoutResId: Int
    get() = R.layout.m3_theme_fab
}
