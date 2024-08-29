package io.material.catalog.divider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class DividerItemDecorationDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_divider_recyclerview_fragment, container, false)
    initRv(view)
    return view
  }

  private fun initRv(view: View) {
    val horizontalRv = view.findViewById<RecyclerView>(R.id.divider_recyclerview_horizontal)
    val verticalRv = view.findViewById<RecyclerView>(R.id.divider_recyclerview_vertical)
    setUpDividers(horizontalRv, LinearLayoutManager.HORIZONTAL)
    setUpDividers(verticalRv, LinearLayoutManager.VERTICAL)
  }

  private fun setUpDividers(rv: RecyclerView, orientation: Int) {
    val layoutManager = LinearLayoutManager(context, orientation, false)
    rv.layoutManager = layoutManager
    val divider = MaterialDividerItemDecoration(requireContext(), orientation)
    rv.addItemDecoration(divider)
    rv.adapter = DividerAdapter()
  }

  companion object {
    class DividerAdapter : RecyclerView.Adapter<DividerViewHolder>() {
      private val ITEM_COUNT = 20
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DividerViewHolder {
        return DividerViewHolder(
          (LayoutInflater.from(parent.context)
            .inflate(R.layout.cat_divider_recyclerview_item, parent, false) as TextView)
        )
      }

      override fun onBindViewHolder(holder: DividerViewHolder, position: Int) {
        val text = holder.view.resources.getString(R.string.cat_divider_item_text, position + 1)
        holder.view.text = text
      }

      override fun getItemCount(): Int {
        return ITEM_COUNT
      }
    }

    class DividerViewHolder(val view: TextView) : RecyclerView.ViewHolder(view) {

    }
  }
}
