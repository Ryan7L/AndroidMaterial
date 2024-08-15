package io.material.catalog.animation

import android.content.DialogInterface
import android.content.DialogInterface.OnDismissListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment


class TweenDemoFragment : DemoFragment(), OnClickListener {

  private lateinit var startBtn: MaterialButton
  private lateinit var cancelBtn: MaterialButton
  private lateinit var resetBtn: MaterialButton
  private var isCancel = false
  private var animation: Animation? = null
  private var configHelper: TweenConfigHelper? = null
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    configHelper = TweenConfigHelper(requireContext())
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.layout_tween_fragmnet, container, false)
    return view
  }
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.configure_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.configure) {
      configHelper?.openConfigDialog {

      }
      return true
    } else {
      return super.onOptionsItemSelected(item)
    }
  }

  private fun getAnim(): Animation {
    animation?.interpolator
    animation?.setAnimationListener(object : AnimationListener {
      override fun onAnimationStart(animation: Animation?) {
        TODO("Not yet implemented")
      }

      override fun onAnimationEnd(animation: Animation?) {
        TODO("Not yet implemented")
      }

      override fun onAnimationRepeat(animation: Animation?) {
        TODO("Not yet implemented")
      }

    })
    return animation!!
  }

  private fun startAnim() {
    getAnim().start()
    startBtn.isEnabled = false
    resetBtn.isEnabled = true
    cancelBtn.isEnabled = true
  }

  private fun cancelAnim() {
    animation?.cancel()
    startBtn.isEnabled = false
    resetBtn.isEnabled = true
    cancelBtn.isEnabled = false
  }

  private fun resetAnim() {
    animation?.reset()
    startBtn.isEnabled = true
    cancelBtn.isEnabled = false
    resetBtn.isEnabled = false
  }


  override fun onClick(v: View?) {
    when (v?.id) {
      R.id.start -> {
        if (!isCancel) {
          startAnim()
        } else {
          Snackbar.make(
            requireView(),
            "Animation is canceled,Please call reset before start",
            Snackbar.LENGTH_SHORT
          ).show()
        }
      }

      R.id.cancel -> {
        isCancel = true
        cancelAnim()
      }

      R.id.reset -> {
        resetAnim()
      }
    }
  }
}
