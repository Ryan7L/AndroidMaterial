package io.material.catalog.loadingindicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.loadingindicator.LoadingIndicatorDrawable
import com.google.android.material.loadingindicator.LoadingIndicatorSpec
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class LoadingIndicatorMainDemoFragment: DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
   val view = inflater.inflate(R.layout.cat_loading_indicator_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View){
    val spec = LoadingIndicatorSpec(requireContext(), null, 0, R.style.Widget_Material3_LoadingIndicator)
    spec.setScaleToFit(true)
    val loadingIndicatorDrawable = LoadingIndicatorDrawable.create(requireContext(), spec)
    val loadingButton = view.findViewById<MaterialButton>(R.id.loading_btn)
    loadingButton.icon = loadingIndicatorDrawable
  }
}
