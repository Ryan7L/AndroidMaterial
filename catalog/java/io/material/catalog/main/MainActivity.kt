package io.material.catalog.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.common.base.Optional
import dagger.BindsOptionalOf
import dagger.android.ContributesAndroidInjector
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.feature.FeatureDemoUtils
import io.material.catalog.internal.InternalOptionsMenuPresenter
import io.material.catalog.preferences.BaseCatalogActivity
import io.material.catalog.preferences.ThemeOverlayUtils
import io.material.catalog.tableofcontents.TocFragment
import io.material.catalog.tableofcontents.TocModule
import io.material.catalog.windowpreferences.WindowPreferencesManager
import javax.inject.Inject

class MainActivity : BaseCatalogActivity() {

  @Inject
  lateinit var internalOptionsMenu: Optional<InternalOptionsMenuPresenter>
//  private val tocFragment: TocFragment by lazy {
//    TocFragment()
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    ThemeOverlayUtils.applyThemeOverlays(this)
    super.onCreate(savedInstanceState)
    WindowPreferencesManager(this).applyEdgeToEdgePreference(window)
    setContentView(R.layout.cat_main_activity)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction().add(R.id.container, TocFragment()).commit()
    }
  }

  /**
   * 创建选项菜单。
   *
   * 选项菜单(Option Menu)是位于ActionBar上的菜单
   *
   * @param menu Menu 菜单
   * @return Boolean true: 表示显示菜单。false: 表示不显示菜单。 通常情况下，应该返回 true 以显示菜单。 如果返回 false，则菜单将不会显示，即使在菜单资源文件中定义了菜单项
   */
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//    menuInflater.inflate(R.menu.action_bar_menu,menu),创建菜单
    super.onCreateOptionsMenu(menu)
    if (internalOptionsMenu.isPresent) {
      internalOptionsMenu.get().onCreateOptionsMenu(menu, menuInflater)
    }
    return true
  }

  /**
   *
   * @param item MenuItem
   * @return Boolean true: 表示你已经处理了菜单项的点击事件，系统不需要进行任何 further 的处理。false: 表示你没有处理菜单项的点击事件，系统会将事件传递给其他处理程序
   */
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    //首先让Fragment 处理该事件，如果当前activity不存在fragment则再让Activity处理
    FeatureDemoUtils.getCurrentFragment(this)?.let {
      if (it.onOptionsItemSelected(item))
        return true
    }
    //android.R.id.home 是ActionBar 或 ToolBar 上的 返回按键(向左的箭头)
    if (item.itemId == android.R.id.home) {
      onBackPressed()
      return true
    }
    if (internalOptionsMenu.isPresent) {
      internalOptionsMenu.get().onOptionsItemSelected(item)
    }
    //调用super 将由系统处理事件
    return super.onOptionsItemSelected(item)
  }

  override val isPreferencesEnabled: Boolean = true

  /**
   * 用于[MainActivity]的依赖模块
   */
  @dagger.Module
  abstract class Module {
    @ActivityScope
    @ContributesAndroidInjector(modules = [TocModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @BindsOptionalOf
    abstract fun optionalInternalOptionsMenu(): InternalOptionsMenuPresenter
  }
}
