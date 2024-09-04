package io.material.catalog.transition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment

class TransitionSimpleLayoutFragment : Fragment() {
  companion object {
    private val KEY_LAYOUT_RES_ID = "KEY_LAYOUT_RES_ID"
    private val KEY_TRANSITION_NAME = "KEY_TRANSITION_NAME"
    private val KEY_TRANSITION_NAME_VIEW_ID = "KEY_TRANSITION_NAME_VIEW_ID"
    @JvmStatic
    fun newInstance(layoutResId: Int): TransitionSimpleLayoutFragment {
      return newInstance(layoutResId, null)
    }
    @JvmStatic
    fun newInstance(layoutResId: Int, transitionName: String?): TransitionSimpleLayoutFragment {
      return newInstance(layoutResId, transitionName, View.NO_ID)
    }
    @JvmStatic
    fun newInstance(
      layoutResId: Int,
      transitionName: String?,
      transitionNameViewId: Int
    ): TransitionSimpleLayoutFragment {
      val args = Bundle().apply {
        putInt(KEY_LAYOUT_RES_ID, layoutResId)
        putString(KEY_TRANSITION_NAME, transitionName)
        putInt(KEY_TRANSITION_NAME_VIEW_ID, transitionNameViewId)
      }
      val fragment = TransitionSimpleLayoutFragment()
      fragment.arguments = args
      return fragment
    }
  }

  private var layoutResId: Int = 0
  private var transitionName: String? = null
  private var transitionNameViewId: Int = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      layoutResId = it.getInt(KEY_LAYOUT_RES_ID)
      transitionName = it.getString(KEY_TRANSITION_NAME)
      transitionNameViewId = it.getInt(KEY_TRANSITION_NAME_VIEW_ID)
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(layoutResId, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    transitionName?.let {
      val transitionNameView =
        if (transitionNameViewId == View.NO_ID) view else view.findViewById(transitionNameViewId)
      ViewCompat.setTransitionName(transitionNameView, transitionName)
    }
  }
}
