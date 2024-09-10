package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionSharedAxisViewDemoFragment : DemoFragment() {
  private val onBackPressedCallback = object : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
      switchView()
    }
  }
  private lateinit var container: ViewGroup
  private lateinit var startView: View
  private lateinit var endView: View
  private lateinit var sharedAxisHelper: SharedAxisHelper

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_transition_shared_axis_view_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    sharedAxisHelper = SharedAxisHelper(view.findViewById(R.id.controls_layout))
    container = view.findViewById(R.id.container)
    startView = view.findViewById(R.id.start_view)
    endView = view.findViewById(R.id.end_view)
    requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    sharedAxisHelper.backButtonOnClickListener = View.OnClickListener { switchView() }
    sharedAxisHelper.nextButtonOnClickListener = View.OnClickListener { switchView() }
  }

  private fun switchView() {
    val isStartViewShowing = isStartViewShowing
    //更改开始视图和结束视图的可见性，使用共享轴过渡进行动画处理。
    val sharedAxis = createTransition(isStartViewShowing)
    TransitionManager.beginDelayedTransition(container, sharedAxis)
    startView.visibility = if (isStartViewShowing) View.GONE else View.VISIBLE
    endView.visibility = if (isStartViewShowing) View.VISIBLE else View.GONE
    sharedAxisHelper.updateButtonsEnabled(!isStartViewShowing)
    onBackPressedCallback.isEnabled = isStartViewShowing
  }

  private fun createTransition(entering: Boolean): MaterialSharedAxis {
    return MaterialSharedAxis(sharedAxisHelper.selectedAxis, entering).apply {
      //添加此转换的目标以仅在这些视图上显式运行转换。如果没有目标，MaterialSharedAxis 转换将为 ViewGroup 布局中的每个视图运行。
      addTarget(startView)
      addTarget(endView)
    }
  }

  private val isStartViewShowing: Boolean
    get() = startView.visibility == View.VISIBLE
}
