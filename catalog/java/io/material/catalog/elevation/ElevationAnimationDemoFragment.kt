package io.material.catalog.elevation

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.shape.MaterialShapeDrawable
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class ElevationAnimationDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_elevation_animation_fragment, container, false)
    val translationZLabelView = view.findViewById<TextView>(R.id.translation_z_label)
    val maxTranslationZ = resources.getDimension(R.dimen.cat_elevation_max_translation_z)
    val maxTranslationZDp = (maxTranslationZ / resources.displayMetrics.density).toInt()
    translationZLabelView.text =
      getString(R.string.cat_elevation_animation_label, maxTranslationZDp)
    val materialShapeDrawable = MaterialShapeDrawable.createWithElevationOverlay(view.context)
    view.background = materialShapeDrawable
    setTranslationZ(view, materialShapeDrawable, maxTranslationZ)
    startTranslationZAnimation(view, materialShapeDrawable, maxTranslationZ)
    return view
  }

  private fun startTranslationZAnimation(
    view: View,
    materialShapeDrawable: MaterialShapeDrawable,
    maxTranslationZ: Float
  ) {
    val animator = ValueAnimator.ofFloat(0f, maxTranslationZ).apply {
      duration = 2000
      startDelay = 1000
      repeatMode = ValueAnimator.RESTART
      repeatCount = ValueAnimator.INFINITE
      addUpdateListener {
        setTranslationZ(view, materialShapeDrawable, it.animatedValue as Float)
      }
    }
    animator.start()
  }

  private fun setTranslationZ(
    view: View,
    materialShapeDrawable: MaterialShapeDrawable,
    translationZ: Float
  ) {
    materialShapeDrawable.translationZ = translationZ
    view.translationZ = translationZ
  }
}
