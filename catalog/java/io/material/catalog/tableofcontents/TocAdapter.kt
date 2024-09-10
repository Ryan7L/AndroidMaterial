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
  //Filterable:定义可过滤行为。可过滤类的数据可受过滤器约束。可过滤类通常是Adapter实现
  private val featureDemoList = featureDemos.toList()

  //Filter：过滤器，异步的
  private val featureDemoFilter = object : Filter() {
    /**
     * 在工作线程中调用以根据约束过滤数据。子类必须实现该方法才能执行过滤操作。
     * 过滤操作计算出的结果必须以 [android.widget.Filter.FilterResults] 的形式
     * 返回，然后通过 [publishResults]
     * 在 UI 线程中发布。
     *
     * 当约束为空时，必须恢复原始数据
     *
     * @param constraint 用于过滤数据的约束
     * @return 过滤操作的结果
     */
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

    /**
     * 在UI线程中调用，将过滤结果发布到用户界面中。子类必须实现此方法才能显示 [performFiltering] 中计算的结果
     *
     * @param constraint 用于过滤数据的约束
     * @param results 过滤操作的结果
     */
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
