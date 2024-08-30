package io.material.catalog.elevation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.internal.ViewUtils
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class ElevationOverlayDemoActivity: DemoActivity() {
  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_elevation_overlay_activity, viewGroup, false)
    val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
    rv.adapter = ItemAdapter(elevationValues)
    rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    return view
  }

  override val demoTitleResId: Int
    get() = R.string.cat_elevation_overlay_title
  private val elevationValues: IntArray = intArrayOf(1, 2, 3, 4, 6, 8, 12, 16, 24)
  class ItemAdapter(private val elevationValues: IntArray): RecyclerView.Adapter<ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
      return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cat_elevation_overlay_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      holder.bind(elevationValues[position])
    }
    override fun getItemCount(): Int {
      return elevationValues.size
    }
  }
  class ItemViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView){
    private val overlayProvider = ElevationOverlayProvider(itemView.context)
    private val dpLabelView = itemView.findViewById<TextView>(R.id.elevation_overlay_dp_label)
    private val alphaLabelView = itemView.findViewById<TextView>(R.id.elevation_overlay_alpha_label)
    fun bind(elevationDp: Int){
      val elevation = ViewUtils.dpToPx(itemView.context, elevationDp)
      val color = overlayProvider.compositeOverlayWithThemeSurfaceColorIfNeeded(elevation)
      val alphaPercent = Math.round(overlayProvider.calculateOverlayAlphaFraction(elevation) * 100)
      ViewCompat.setElevation(itemView,elevation)
      itemView.setBackgroundColor(color)
      dpLabelView.text = String.format("%02d dp", elevationDp)
      alphaLabelView.text = String.format("%d%% On Surface", alphaPercent)
    }
  }
}
