package io.material.catalog.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.ActionMode
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity
import io.material.catalog.feature.DemoUtils

/**
 * 顶部应用程序栏操作栏演示的基类Activity
 */
abstract class BaseTopAppBarActionBarDemoActivity : DemoActivity() {
  /**
   * ActionMode 是 Android 中一个特殊的 UI 模式，它提供了一种在用户与屏幕上的某个元素进行交互时提供上下文操作的方式。
   * 它通常以一个浮动工具栏的形式出现在屏幕顶部， 包含与当前上下文相关的操作按钮。简单来说就是临时代替toolbar
   * ActionMode 的特点：
   *    临时性： ActionMode 是一种临时 UI，它会在用户完成操作或取消选择后消失。
   *    上下文相关： ActionMode 中显示的操作按钮与用户当前选择的元素或正在执行的操作相关。
   *    浮动工具栏： ActionMode 通常以浮动工具栏的形式出现在屏幕顶部，但也可以出现在底部。
   * ActionMode 的用途：
   *    文本选择： 当用户在 TextView 或 EditText 中选择文本时，ActionMode 可以提供复制、剪切、粘贴等操作。
   *    列表项选择： 当用户在 ListView 或 RecyclerView 中选择一个或多个列表项时，ActionMode 可以提供删除、分享等操作。
   *    其他上下文操作： ActionMode 可以用于提供任何与当前上下文相关的操作， 例如在图片查看器中提供编辑、 旋转等操作
   */
  private var actionMode: ActionMode? = null

  private var inActionMode = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setSubtitle(R.string.cat_topappbar_action_bar_subtitle)
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    val view = layoutInflater.inflate(R.layout.cat_topappbar_action_bar_activity, viewGroup, false)
    val demoDescriptionTv = view.findViewById<TextView>(R.id.action_bar_demo_description)
    demoDescriptionTv.setText(R.string.cat_topappbar_action_bar_description)
    val actionModeBtn = view.findViewById<Button>(R.id.action_bar_demo_action_mode_button)
    actionModeBtn.setOnClickListener {
      inActionMode = !inActionMode
      if (inActionMode) {
        if (actionMode == null) {
          actionMode = startSupportActionMode(object : ActionMode.Callback {
            /**
             * 首次创建操作模式时调用。提供的菜单将用于生成操作模式的操作按钮。
             *
             * @param mode 正在创建 ActionMode
             * @param menu 用于填充操作按钮的菜单
             * @return 如果应创建动作模式则为 true ，如果应中止进入此模式则为 false 。
             */
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
              menuInflater.inflate(R.menu.cat_topappbar_menu_actionmode, menu)
              return true
            }

            /**
             * 每当操作模式无效时调用以刷新操作模式的操作菜单。
             *
             * @param mode ActionMode 正在准备中
             * @param menu 用于填充操作按钮的菜单
             * @return 如果菜单或操作模式已更新，则为 true，否则为 false。
             */
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
              return false
            }

            /**
             * 调用以报告用户单击操作按钮。
             *
             * @param mode 当前的动作模式
             * @param item 被点击的项目
             * @return 如果此回调处理了该事件，则返回 true；如果标准 MenuItem 调用应该继续，则返回 false。
             */
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
              return false
            }

            /**
             * 当动作模式即将退出并销毁时调用。
             *
             * @param mode 当前的 ActionMode 被销毁
             */
            override fun onDestroyActionMode(mode: ActionMode?) {
              actionMode = null
            }

          })

        }
        actionMode?.setTitle(R.string.cat_topappbar_action_bar_action_mode_title)
        actionMode?.setSubtitle(R.string.cat_topappbar_action_bar_action_mode_subtitle)
      } else {
        actionMode?.finish()
      }

    }
    return view
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.cat_topappbar_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return DemoUtils.showSnackBar(this, item) || super.onOptionsItemSelected(item)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false

  override val isShouldSetUpContainerTransform: Boolean
    get() = false
  override val isShouldApplyEdgeToEdgePreference: Boolean
    get() = false
}
