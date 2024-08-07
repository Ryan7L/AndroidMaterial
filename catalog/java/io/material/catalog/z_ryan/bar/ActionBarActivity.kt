package io.material.catalog.z_ryan.bar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

private const val TAG = "ActionBarActivity"

class ActionBarActivity : DemoActivity() {
  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View {

    supportActionBar?.setTitle(R.string.cat_bar_title)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)//显示后退按钮
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24)
//    supportActionBar?.setCustomView() 设置自定义actionBar视图
    return layoutInflater.inflate(R.layout.cat_bar_actionbar_activity, viewGroup, false)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
  override val isShouldShowDefaultDemoActionBarCloseButton: Boolean
    get() = false
  override val isShouldApplyEdgeToEdgePreference: Boolean
    get() = false

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.action_bar_menu, menu)
    return true
  }

  override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
    Log.d(TAG, "onPrepareOptionsMenu: ")
    return super.onPrepareOptionsMenu(menu)
  }

  override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
    Log.d(TAG, "onMenuOpened: ")
    return super.onMenuOpened(featureId, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.setting -> {
        Log.d(TAG, "onOptionsItemSelected: setting")
      }

      R.id.search -> {
        Log.d(TAG, "onOptionsItemSelected: search")
      }

      android.R.id.home -> {
        Log.d(TAG, "onOptionsItemSelected: home")
        finish()
      }
    }
    return true
  }

  override fun onOptionsMenuClosed(menu: Menu?) {
    Log.d(TAG, "onOptionsMenuClosed: ")
    super.onOptionsMenuClosed(menu)
  }
}
/*
 * 使用系统创建的ActionBar时，主题必需不是 xxx.NoActionBar 或者 覆盖 xxx.NoActionBar 与ActionBar 相关的属性。
 *    详情主题设置见 [R.style.Theme.Catalog.System.ActionBar]
 *    当主题不是与  xxx.NoActionBar 相关的主题时，也可在使用Activity 中使用requestFeature(WindowCompat(Window).FEATURE_ACTION_BAR)
 *    或 （继承于AppCompatActivity的Activity中）supportRequestWindowFeature(WindowCompat(Window).FEATURE_ACTION_BAR)，且方法调用的顺序要在setContentView()之前。
 */
