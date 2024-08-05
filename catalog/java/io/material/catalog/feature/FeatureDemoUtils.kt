package io.material.catalog.feature

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import io.material.catalog.R

abstract class FeatureDemoUtils {

  companion object {

    const val ARG_TRANSITION_NAME = "ARG_TRANSITION_NAME"

    const val MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID = R.id.container

    const val KEY_DEFAULT_CATALOG_DEMO_LANDING = "default_catalog_demo_landing_preference"

    const val KEY_DEFAULT_CATALOG_DEMO = "default_catalog_demo_preference"

    @JvmStatic
    fun saveDefaultDemo(context: Context, value: String) {
      PreferenceManager.getDefaultSharedPreferences(context).edit().putString(
        KEY_DEFAULT_CATALOG_DEMO, value
      ).apply()
    }

    @JvmStatic
    fun getDefaultDemo(context: Context): String =
      PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_DEFAULT_CATALOG_DEMO, "")
        ?: ""

    @JvmStatic
    fun saveDefaultDemoLanding(context: Context, value: String) {
      PreferenceManager.getDefaultSharedPreferences(context).edit().putString(
        KEY_DEFAULT_CATALOG_DEMO_LANDING, value
      ).apply()
    }

    @JvmStatic
    fun getDefaultDemoLanding(context: Context): String =
      PreferenceManager.getDefaultSharedPreferences(context).getString(
        KEY_DEFAULT_CATALOG_DEMO_LANDING, ""
      ) ?: ""

    @JvmStatic
    fun getCurrentFragment(activity: FragmentActivity): Fragment? =
      activity.supportFragmentManager.findFragmentById(
        MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID
      )

    @JvmStatic
    fun startFragment(activity: FragmentActivity, fragment: Fragment, tag: String) {
      startFragment(activity, fragment, tag, null, null)
    }

    @JvmStatic
    fun startFragment(
      activity: FragmentActivity,
      fragment: Fragment,
      tag: String,
      sharedElement: View?,
      sharedElementName: String?
    ) {
      startFragmentInternal(activity, fragment, tag, sharedElement, sharedElementName)
    }

    @JvmStatic
    private fun startFragmentInternal(
      activity: FragmentActivity,
      fragment: Fragment,
      tag: String,
      sharedElement: View?,
      sharedElementName: String?
    ) {
      //FragmentTransaction 是 Android 中用于管理 Fragment 事务的类。 它提供了一组 API，用于执行添加、移除、替换和显示 Fragment 等操作。
      // 您可以将 FragmentTransaction 视为一系列对 Fragment 的操作，这些操作会被一起执行。
      // 可以通过 FragmentManager 的 beginTransaction() 方法获取 FragmentTransaction 对象
      val transaction = activity.supportFragmentManager.beginTransaction()
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        && sharedElement != null && sharedElementName != null
      ) {
        val currentFragment = getCurrentFragment(activity)
        val context = currentFragment?.requireContext()
        context ?: return

        val transform = MaterialContainerTransform(context, true)
          .apply {
            containerColor = MaterialColors.getColor(sharedElement, R.attr.colorSurface)
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
          }
        fragment.sharedElementEnterTransition = transform
        transaction.addSharedElement(sharedElement, sharedElementName)
        Hold().also {
          //  将根视图添加为“保留”的目标，以便将整个视图层次结构作为一个整体保留，而不是单独保留每个子视图。有助于在过渡过程中保持阴影。
          it.addTarget(currentFragment.view!!)
          it.duration = transform.duration
          currentFragment.exitTransition = it
        }
        if (fragment.arguments == null) {
          fragment.arguments = Bundle().apply { putString(ARG_TRANSITION_NAME, sharedElementName) }
        } else {
          fragment.arguments?.putString(ARG_TRANSITION_NAME, sharedElementName)
        }
      } else {
        transaction.setCustomAnimations(
          R.anim.abc_grow_fade_in_from_bottom,
          R.anim.abc_fade_out,
          R.anim.abc_fade_in,
          R.anim.abc_shrink_fade_out_from_bottom
        )
      }
      transaction.replace(MAIN_ACTIVITY_FRAGMENT_CONTAINER_ID, fragment, tag)
        .addToBackStack(null)
        .commit()
    }
  }

}
