package io.material.catalog.carousel

fun interface CarouselItemListener {
  fun onItemClicked(item: CarouselItem, position: Int)
}
