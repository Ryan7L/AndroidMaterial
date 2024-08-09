package io.material.catalog.tableofcontents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import io.material.catalog.R
import io.material.catalog.feature.FeatureDemo
import io.material.catalog.feature.FeatureDemoUtils
import io.material.catalog.feature.STATUS_WIP

private const val FRAGMENT_CONTENT = "fragment_content"

class TocViewHolder(private val activity: FragmentActivity, private val viewGroup: ViewGroup) :
  RecyclerView.ViewHolder(
    LayoutInflater.from(activity).inflate(R.layout.cat_toc_item, viewGroup, false)
  ) {

  val titleTv = itemView.findViewById<TextView>(R.id.cat_toc_title)
  val imageView = itemView.findViewById<ImageView>(R.id.cat_toc_image)
  val statusWipLabelView = itemView.findViewById<TextView>(R.id.cat_toc_status_wip_label)
  fun bind(activity: FragmentActivity, featureDemo: FeatureDemo) {
    val transitionName = activity.getString(featureDemo.titleResId)
    ViewCompat.setTransitionName(itemView, transitionName)
    titleTv.setText(featureDemo.titleResId)
    imageView.setImageResource(featureDemo.drawableResId)
    itemView.setOnClickListener {
      FeatureDemoUtils.startFragment(
        activity,
        featureDemo.landingFragment,
        FRAGMENT_CONTENT,
        it,
        transitionName
      )
      statusWipLabelView.visibility =
        if (featureDemo.status == STATUS_WIP) View.VISIBLE else View.GONE
    }
  }
}
