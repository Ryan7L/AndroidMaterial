package io.material.catalog.carousel

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.material.catalog.R

class CarouselItemViewHolder(val itemView: View, val listener: CarouselItemListener) :
  RecyclerView.ViewHolder(itemView) {
  val imageView: ImageView = itemView.findViewById(R.id.carousel_image_view)
  fun bind(item: CarouselItem) {
    Glide.with(imageView)
      .load(item.drawableRes)
      .centerCrop()
      .into(imageView)
    imageView.setContentDescription(imageView.resources.getString(item.contentDescRes))
    itemView.setOnClickListener {
      listener.onItemClicked(item, bindingAdapterPosition)
    }
  }
}
