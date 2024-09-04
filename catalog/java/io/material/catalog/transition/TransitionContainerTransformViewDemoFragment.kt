package io.material.catalog.transition

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialContainerTransform
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionContainerTransformViewDemoFragment : DemoFragment() {

  private lateinit var startCard: View
  private lateinit var startFab: View
  private lateinit var contactCard: View
  private lateinit var endView: View
  private lateinit var expandedCard: View
  private lateinit var root: FrameLayout
  private var configurationHelper: ContainerTransformConfigurationHelper? = null
  override fun onAttach(context: Context) {
    super.onAttach(context)
    configurationHelper = ContainerTransformConfigurationHelper()
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_transition_container_transform_view_fragment, container, false)
    root = view.findViewById(R.id.root)
    startFab = view.findViewById(R.id.start_fab)
    expandedCard = view.findViewById(R.id.expanded_card)
    contactCard = view.findViewById(R.id.contact_card)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    addTransitionableTarget(view, R.id.start_fab);
    addTransitionableTarget(view, R.id.single_line_list_item);
    addTransitionableTarget(view, R.id.vertical_card_item);
    addTransitionableTarget(view, R.id.horizontal_card_item);
    addTransitionableTarget(view, R.id.grid_card_item);
    addTransitionableTarget(view, R.id.grid_tall_card_item);
    addTransitionableTarget(view, R.id.expanded_card);
    addTransitionableTarget(view, R.id.contact_card);

  }
  private fun addTransitionableTarget(view: View, id: Int) {
    view.findViewById<View>(id)?.let {
      ViewCompat.setTransitionName(it, id.toString())
      if (id == R.id.expanded_card || id == R.id.contact_card) {
        it.setOnClickListener(this::showStartView)
      } else {
        it.setOnClickListener(this::showEndView)
      }
    }
  }

  private fun showEndView(startView: View) {
    if (startView.id == R.id.start_fab) {
      this.endView = contactCard
    } else {
      //将 startView 引用保存为触发转换的 startCard，以便知道在返回转换期间要转换到哪张卡。
      this.startCard = startView
      this.endView = expandedCard
    }
    //构造两个视图之间的容器变换过渡。
    val transition = buildContainerTransform(true).apply {
      setStartView(startView)
      endView = this@TransitionContainerTransformViewDemoFragment.endView
      //添加单个目标以阻止容器变换在开始视图和结束视图上运行。
      addTarget(this@TransitionContainerTransformViewDemoFragment.endView)
    }
    //触发容器转换转换.
    TransitionManager.beginDelayedTransition(root, transition)
    startView.visibility = View.INVISIBLE
    endView.visibility = View.VISIBLE
    requireActivity().onBackPressedDispatcher.addCallback(this,
      object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
          showStartView(this@TransitionContainerTransformViewDemoFragment.endView)
          remove()
        }
      })
  }

  private fun showStartView(endView: View) {
    val startView = if (endView.id == R.id.contact_card) startFab else startCard
    val transition = buildContainerTransform(false).apply {
      setStartView(endView)
      setEndView(startView)
      addTarget(startView)
    }
    TransitionManager.beginDelayedTransition(root, transition)
    startView.visibility = View.VISIBLE
    endView.visibility = View.INVISIBLE
  }

  private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
    return MaterialContainerTransform(requireContext(), entering).apply {
      scrimColor = Color.TRANSPARENT
      drawingViewId = root.id
      configurationHelper?.configure(this, entering)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.configure_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.configure) {
      configurationHelper?.showConfigurationChooser(
        requireContext()
      ) {
        buildContainerTransform(true)
      }
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}
