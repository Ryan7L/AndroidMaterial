package io.material.catalog.chip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class ChipRecyclerviewDemoFragment: DemoFragment() {
  private lateinit var rv: RecyclerView
  private lateinit var adapter: ChipAdapter
  private lateinit var layoutManager: RecyclerView.LayoutManager
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_chip_recyclerview_fragment,container,false)
    rv = view.findViewById(R.id.chip_recyclerview_parent)
    adapter = ChipAdapter()
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
    rv.layoutManager = layoutManager
    rv.adapter = adapter
    return view
  }

  class ChipAdapter():RecyclerView.Adapter<ChipAdapter.ChipViewHolder>(){

    private val checkedChipId = HashSet<Int>(itemCount)

    class ChipViewHolder(val itemView: View,val checkedChipId: MutableSet<Int>): RecyclerView.ViewHolder(itemView){
      val chip = itemView as Chip
      init {
          chip.setOnClickListener {
            val chipId = it.tag as Int
            if (chip.isEnabled){
              checkedChipId.add(chipId)
            }else{
              checkedChipId.remove(chipId)
            }
          }
      }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.cat_chip_group_item_filter,parent,false)
     return ChipViewHolder(view,checkedChipId)
    }


    override fun getItemCount(): Int {
      return 30
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
      holder.chip.tag = position
      holder.chip.isChecked = checkedChipId.contains(position)
      holder.chip.text = holder.chip.resources.getString(R.string.cat_chip_text)+" "+position
    }
  }
}
