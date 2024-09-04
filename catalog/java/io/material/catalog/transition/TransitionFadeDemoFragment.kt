package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.transition.TransitionManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.transition.MaterialFade
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionFadeDemoFragment : DemoFragment() {
  private lateinit var fadeButton: Button
  private lateinit var fadeFab: FloatingActionButton

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_transition_fade_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    fadeButton = view.findViewById(R.id.fade_button)
    fadeFab = view.findViewById(R.id.fade_fab)
    fadeButton.setOnClickListener {
      toggleFabVisibilityWithTransition()
    }
  }

  private fun toggleFabVisibilityWithTransition() {
    val sceneRoot = requireView() as ViewGroup
    val entering = fadeFab.visibility == View.GONE
    val materialFade = MaterialFade()
    TransitionManager.beginDelayedTransition(sceneRoot, materialFade)
    fadeFab.visibility = if (entering) View.VISIBLE else View.GONE
    fadeButton.setText(if (entering) R.string.cat_transition_fade_button_hide_fab else R.string.cat_transition_fade_button_show_fab)
  }
}
