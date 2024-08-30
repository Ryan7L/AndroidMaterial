package io.material.catalog.fab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class ExtendedFabBehaviorDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_extended_fab_behavior_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    val extendedFabs = DemoUtils.findViewsWithType(view, ExtendedFloatingActionButton::class.java)
    extendedFabs.forEach {
      it.setOnClickListener { v ->
        Snackbar.make(v, R.string.cat_extended_fab_clicked, Snackbar.LENGTH_SHORT).show()
      }
    }
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

}
