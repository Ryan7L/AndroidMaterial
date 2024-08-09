package io.material.catalog.tableofcontents

import android.util.Log
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.feature.FeatureDemo
import java.util.Locale

private const val TAG = "TocAdapter"

class TocAdapter(
  private val activity: FragmentActivity,
  private val featureDemos: MutableList<FeatureDemo>
) :
  RecyclerView.Adapter<TocViewHolder>(), Filterable {

  private val featureDemoList = featureDemos.toList()

  private val featureDemoFilter = object : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
      val filteredList = mutableListOf<FeatureDemo>()
      constraint?.let {
        if (it.isEmpty()) {
          filteredList.addAll(featureDemoList)
        } else {
          featureDemoList.forEach { demo ->
            if (activity.getString(demo.titleResId)
                .lowercase(Locale.ROOT)
                .contains(it.toString().lowercase(Locale.ROOT))
            ) {
              filteredList.add(demo)
            }
          }
        }
      }
      return FilterResults().apply {
        values = filteredList
      }
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
      results?.let {
        featureDemos.clear()
        (it.values as? List<*>)?.forEach { demo ->
          Log.d(TAG, "publishResults: foeEach")
          if (demo is FeatureDemo) {
            Log.d(TAG, "publishResults: add")
            featureDemos.add(demo)
          }
        }
        notifyDataSetChanged()
      }
    }

  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TocViewHolder {
    return TocViewHolder(activity, parent)
  }

  override fun getItemCount(): Int = featureDemos.size

  override fun onBindViewHolder(holder: TocViewHolder, position: Int) {
    holder.bind(activity, featureDemos[position])
  }

  override fun getFilter(): Filter = featureDemoFilter

}
