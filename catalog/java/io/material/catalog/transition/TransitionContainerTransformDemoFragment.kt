package io.material.catalog.transition

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionContainerTransformDemoFragment : DemoFragment() {
  private val END_FRAGMENT_TAG = "END_FRAGMENT_TAG"
  private var configurationHelper: ContainerTransformConfigurationHelper? = null
  private val holdTransition = Hold()
  override fun onAttach(context: Context) {
    super.onAttach(context)
    configurationHelper = ContainerTransformConfigurationHelper()
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_transition_container_transform_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    childFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks() {
      override fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
      ) {
        addTransitionableTarget(v, R.id.start_fab)
        addTransitionableTarget(v, R.id.single_line_list_item)
        addTransitionableTarget(v, R.id.vertical_card_item)
        addTransitionableTarget(v, R.id.horizontal_card_item)
        addTransitionableTarget(v, R.id.grid_card_item)
        addTransitionableTarget(v, R.id.grid_tall_card_item)
      }
    }, true)
    showStartFragment()
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.configure_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.configure) {
      configurationHelper?.showConfigurationChooser(requireContext()) {
        val endFragment = childFragmentManager.findFragmentByTag(END_FRAGMENT_TAG)
        endFragment?.let { configTransitions(it) }
      }
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  private fun addTransitionableTarget(view: View, id: Int) {
    view.findViewById<View>(id)?.let {
      ViewCompat.setTransitionName(it, id.toString())
      it.setOnClickListener { v -> showEndFragment(it) }
    }
  }

  private fun showStartFragment() {
    val fragment = TransitionSimpleLayoutFragment.newInstance(
      R.layout.cat_transition_container_transform_start_fragment,
      "shared_element_fab", R.id.start_fab
    )
    //将根视图添加为“保留”的目标，以便将整个视图层次结构作为一个整体保留，
    // 而不是单独保留每个子视图。有助于在过渡过程中保持阴影。
    holdTransition.addTarget(R.id.start_root)
    fragment.exitTransition = holdTransition
    childFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
      .addToBackStack("ContainerTransformFragment::start")
      .commit()
  }

  private fun showEndFragment(sharedElement: View) {
    val transitionName = "shared_element_end_root"
    val fragment = TransitionSimpleLayoutFragment.newInstance(
      R.layout.cat_transition_container_transform_end_fragment,
      transitionName
    )
    configTransitions(fragment)
    childFragmentManager.beginTransaction().addSharedElement(sharedElement, transitionName)
      .replace(R.id.fragment_container, fragment, END_FRAGMENT_TAG)
      .addToBackStack("ContainerTransformFragment::end")
      .commit()
    requireActivity().onBackPressedDispatcher
      .addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          childFragmentManager.popBackStack()
          remove()
        }
      })
  }

  private fun configTransitions(fragment: Fragment) {
    //对于所有 3 个容器层颜色，请使用 colorSurface，因为可以使用任何淡入淡出模式配置此变换，
    // 并且某些开始视图没有背景，并且结束视图没有背景。
    val colorSurface = MaterialColors.getColor(requireView(), R.attr.colorSurface)
    val enterContainerTransform = buildContainerTransform(true)
    enterContainerTransform.setAllContainerColors(colorSurface)
    fragment.sharedElementEnterTransition = enterContainerTransform
    holdTransition.duration = enterContainerTransform.duration
    val returnContainerTransform = buildContainerTransform(false)
    returnContainerTransform.setAllContainerColors(colorSurface)
    fragment.sharedElementReturnTransition = returnContainerTransform
  }

  private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
    return MaterialContainerTransform(requireContext(), entering).apply {
      drawingViewId = if (entering) R.id.end_root else R.id.start_root
      configurationHelper?.configure(this, entering)
    }
  }

}

