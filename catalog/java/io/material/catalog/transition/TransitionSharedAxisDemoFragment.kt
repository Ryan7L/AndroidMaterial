package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.transition.TransitionListenerAdapter
import com.google.android.material.transition.MaterialSharedAxis
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class TransitionSharedAxisDemoFragment : DemoFragment() {

  private val LAYOUT_RES_ID_START = R.layout.cat_transition_shared_axis_start
  private val LAYOUT_RES_ID_END = R.layout.cat_transition_shared_axis_end

  private lateinit var sharedAxisHelper: SharedAxisHelper
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.cat_transition_shared_axis_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   sharedAxisHelper = SharedAxisHelper(view.findViewById(R.id.controls_layout))
   val fragment = TransitionSimpleLayoutFragment.newInstance(LAYOUT_RES_ID_START)
   requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
     .commit()
    sharedAxisHelper.backButtonOnClickListener = View.OnClickListener { replaceFragment(LAYOUT_RES_ID_START) }
    sharedAxisHelper.nextButtonOnClickListener = View.OnClickListener { replaceFragment(LAYOUT_RES_ID_END) }
    sharedAxisHelper.updateButtonsEnabled(true)
  }

  private fun replaceFragment(layoutResId: Int) {
    val entering = layoutResId == LAYOUT_RES_ID_END
    val fragment = TransitionSimpleLayoutFragment.newInstance(layoutResId)
    //将过渡设置为片段的进入过渡。当将片段添加到容器中时将使用它，并在从容器中删除片段时重新使用该片段。
    fragment.enterTransition = createTransition(entering)
    if (entering) {
      fragment.returnTransition = createTransition(false)
    } else {
      //如果手动转换到开始片段以从后退堆栈中删除结束片段而没有后退事件，则弹出后退堆栈。
      requireActivity().supportFragmentManager.popBackStack()
    }
    getFragmentTransaction(fragment, entering).commit()
  }

  private fun getFragmentTransaction(fragment: Fragment, entering: Boolean): FragmentTransaction {
    return if (entering) getFragmentTransaction(fragment).addToBackStack(null) else
      getFragmentTransaction(fragment)
  }

  private fun getFragmentTransaction(fragment: Fragment): FragmentTransaction {
    return requireActivity().supportFragmentManager.beginTransaction()
      .replace(R.id.fragment_container, fragment)
  }

  private fun createTransition(entering: Boolean): MaterialSharedAxis {
    return MaterialSharedAxis(sharedAxisHelper.selectedAxis, entering).apply {
      //添加此转换的目标以仅在这些视图上显式运行转换。如果没有目标，则将为片段布局中的每个视图运行 MaterialSharedAxis 转换。
      addTarget(R.id.start_root)
      addTarget(R.id.end_root)
      addListener(object : TransitionListenerAdapter() {
        override fun onTransitionCancel(transition: androidx.transition.Transition) {
          sharedAxisHelper.updateButtonsEnabled(entering)
        }

        override fun onTransitionStart(transition: androidx.transition.Transition) {
          sharedAxisHelper.updateButtonsEnabled(!entering)
        }
      })

    }
  }
}
