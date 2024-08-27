package io.material.catalog.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class CarouselMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_carousel, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val rv = view.findViewById<RecyclerView>(R.id.carousel_recycler_view)
    val layoutManager = CarouselLayoutManager()
    rv.layoutManager = layoutManager
    rv.isNestedScrollingEnabled = false
    val listener = CarouselItemListener { _, position ->
      rv.scrollToPosition(position)
    }
    val adapter = CarouselAdapter(listener)
    rv.adapter = adapter
    adapter.submitList(createItems())
  }
}
