package io.material.catalog.imageview

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.ShapeAppearanceModel
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import kotlin.random.Random

class ShapeableImageViewMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.catalog_imageview, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val toggleGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.togglegroup)
    val img = view.findViewById<ShapeableImageView>(R.id.image_view)
    val icon = view.findViewById<ShapeableImageView>(R.id.icon_view)
    val shapes = SparseArray<ShapeAppearanceModel>().apply {
      put(
        R.id.button_diamond,
        ShapeAppearanceModel.builder()
          .setAllCorners(CornerFamily.CUT,  /*cornerSize=*/0f)
          .setAllCornerSizes(ShapeAppearanceModel.PILL)
          .build()
      )
      put(
        R.id.button_circle,
        ShapeAppearanceModel.builder().setAllCornerSizes(ShapeAppearanceModel.PILL).build()
      )
      put(R.id.button_square, ShapeAppearanceModel.builder().build())
    }
    val random = Random.Default
    toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
      if (!isChecked) return@addOnButtonCheckedListener
      val shape = shapes.get(checkedId)
      img.setImageResource(
        random.nextBoolean().let { if (it) R.drawable.dog_image else R.drawable.dog_image_wink })
      img.shapeAppearanceModel = shape
      icon.shapeAppearanceModel = shape
    }
  }
}
