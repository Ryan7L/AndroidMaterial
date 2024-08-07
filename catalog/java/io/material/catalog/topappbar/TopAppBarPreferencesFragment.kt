package io.material.catalog.topappbar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

private const val TAG = "TopAppBarPreferencesFra"

//TODO:详细描述Preference的使用
class TopAppBarPreferencesFragment : DemoFragment() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION") setHasOptionsMenu(true)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_topappbar_preferences_fragment, container, false)
    val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toolbar)

    childFragmentManager.beginTransaction()
      .replace(R.id.cat_topappbar_preferences_container, PreferencesFragment()).commit()
    registerPreferenceChangedListener()
    return view
  }

  private fun registerPreferenceChangedListener() {
    PreferenceManager.OnPreferenceTreeClickListener {
      Log.i(TAG, " ${it.key} 项 被点击")
      return@OnPreferenceTreeClickListener true
    }
    PreferenceManager.getDefaultSharedPreferences(requireContext()).all.forEach {
      Log.i(TAG, "registerPreferenceChangedListener: key: ${it.key}  value: ${it.value}")
    }
    val preference = PreferenceManager.getDefaultSharedPreferences(requireContext())
    preference.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
      Log.i(TAG, " $key 值发生变化")
    }

  }

  @Deprecated("Deprecated in Java")
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.cat_topappbar_menu, menu)
    @Suppress("DEPRECATION") super.onCreateOptionsMenu(menu, inflater)
  }

  @Deprecated("Deprecated in Java")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    @Suppress("DEPRECATION") return super.onOptionsItemSelected(item) || DemoUtils.showSnackBar(
      requireActivity(),
      item
    )
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
   companion object{
     class PreferencesFragment : PreferenceFragmentCompat() {
       /**
        * 在.onCreate期间调用以提供此片段的偏好设置。子类应直接调用.setPreferenceScreen或通过辅助方法（例如[.addPreferencesFromResource] ）调用。
        *
        * @param savedInstanceState 如果片段是从先前保存的状态重新创建的，则这就是状态。这个参数包含了 Fragment 之前的状态信息。 如果 Fragment 曾经被销毁并重建 (例如由于配置更改)，
        *                           这个 Bundle 就会包含之前保存的状态数据。 您可以使用它来恢复 Fragment 的状态，例如之前选择的偏好设置。
        * @param rootKey            如果非空，则此首选项片段应以具有此键的 [androidx.preference.PreferenceScreen] 为根，
        *                           这个参数是偏好设置的根 key。 它用于标识您想要在 Fragment 中显示的偏好设置层次结构的根节点。
        */
       override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
         setPreferencesFromResource(R.xml.cat_topappbar_preferences, rootKey)
         //监听preference
         findPreference<Preference>("setting1")?.setOnPreferenceClickListener {
           Log.i(TAG, "onCreatePreferences: $it")
           return@setOnPreferenceClickListener true
         }
       }

     }
   }
}
