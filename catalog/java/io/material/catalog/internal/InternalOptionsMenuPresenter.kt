package io.material.catalog.internal

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

/**
 * 显示仅供内部/开发使用的可选菜单的界面
 */
interface InternalOptionsMenuPresenter {

  fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater): Boolean

  fun onOptionsItemSelected(item: MenuItem?): Boolean

}
