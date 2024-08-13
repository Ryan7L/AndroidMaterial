package io.material.catalog.transition

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IdRes
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

const val SHARED_ELEMENT_END_ROOT = "shared_element_end_root"

class TransitionContainerTransformStartDemoActivity : DemoActivity() {
  companion object {
    @JvmField
    var configurationHelper: ContainerTransformConfigurationHelper? = null
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    //启用Activity过渡动画 也可在 主题中设置，详见Theme.Catlog.Transition
    window?.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
    val callback =
      MaterialContainerTransformSharedElementCallback()
    //当使用ActivityOptions.makeSceneTransitionAnimation(Activity, View, String)启动 Activity 时，将调用回调来处理启动Activity 上的共享元素。
    // 大多数调用仅在从启动的 Activity 返回时发生
    setExitSharedElementCallback(callback)
    super.onCreate(savedInstanceState)
    configurationHelper = ContainerTransformConfigurationHelper()
    addTransitionAbleTarget(R.id.start_fab)
    addTransitionAbleTarget(R.id.single_line_list_item)
    addTransitionAbleTarget(R.id.vertical_card_item)
    addTransitionAbleTarget(R.id.horizontal_card_item)
    addTransitionAbleTarget(R.id.grid_card_item)
    addTransitionAbleTarget(R.id.grid_tall_card_item)
  }

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    return layoutInflater.inflate(
      R.layout.cat_transition_container_transform_start_fragment,
      viewGroup,
      false
    )
  }

  override fun onDestroy() {
    super.onDestroy()
    configurationHelper = null
  }

  /**
   * 初始化 Activity 标准选项菜单的内容。您应该将菜单项放入菜单中。
   * 此方法仅调用一次，即第一次显示选项菜单时。若要在每次显示菜单时更新菜单，请使用 [onPrepareOptionsMenu] 。
   * 必须返回 true 才能显示菜单；如果返回 false 则不会显示
   */
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.configure_menu, menu)

    return super.onCreateOptionsMenu(menu)
  }

  /**
   * 每当选择选项菜单中的某项时，都会调用此钩子。
   * 默认实现只是返回 false 以进行正常处理（根据需要调用该项的 Runnable 或向其 Handler 发送消息）。您可以将此方法用于任何您想要处理而不需要其他工具的项。
   *
   * 布尔值返回 false 以允许系统处理，返回 true 以在此处理它。
   */
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.configure){
      configurationHelper?.showConfigurationChooser(this,null)
      return true
    }else{
      return super.onOptionsItemSelected(item)
    }
  }
  override val demoTitleResId: Int
    get() = R.string.cat_transition_container_transform_activity_title

  private fun addTransitionAbleTarget(@IdRes id: Int) {
    findViewById<View>(id)?.setOnClickListener {
      startEndActivity(it)
    }
  }

  private fun startEndActivity(sharedElement: View) {
    Intent(this, TransitionContainerTransformEndDemoActivity::class.java).run {

      //创建过渡动画
      val options = ActivityOptions.makeSceneTransitionAnimation(
        //这个 sharedElement 参数告诉系统，在 Activity 转换过程中，要把这个 View 作为共享元素进行动画处理。
        // 这意味着在启动新的 Activity 时，这个 View 会以动画的形式从当前 Activity 的位置移动到新 Activity 的对应位置
        this@TransitionContainerTransformStartDemoActivity, sharedElement,
        SHARED_ELEMENT_END_ROOT
      )
      startActivity(this, options.toBundle())
    }
  }
}
/**
 * 重要概念：
 * 1.ActivityOptions 是一个用于定义 Activity 启动选项的类，用于构建可与[Context.startActivity(Intent, Bundle)]和相关方法一起使用的选项 Bundle 的辅助类
 *    它可以让你自定义 Activity 的启动方式和动画效果。 这些选项可以使你的应用程序更具视觉吸引力， 并为用户提供更好的体验。
 *    ActivityOptions 的主要用途：
 *    自定义 Activity 转换动画: 你可以使用 ActivityOptions 来指定 Activity 进入和退出时的动画效果，例如淡入淡出、滑动或爆炸效果。  你还可以使用它来创建共享元素转换，其中一个 Activity 中的视图会平滑地转换为另一个 Activity 中的视图。
 *    指定 Activity 启动位置: 你可以使用 ActivityOptions 来指定 Activity 在屏幕上的启动位置，例如从屏幕的中心启动或从某个特定视图的位置启动。
 *    控制 Activity 的 Z 轴顺序: 你可以使用 ActivityOptions 来控制 Activity 在 Z 轴上的顺序，例如将新 Activity 显示在当前 Activity 的上方或下方。
 *
 * 2.SharedElementCallback：
 *    SharedElementCallback 是一个用于监听和自定义共享元素转换过程的类。 在 Activity 之间进行共享元素转换时，你可以使用 SharedElementCallback 来监控转换的各个阶段，并对共享元素的外观和行为进行修改。
 *    SharedElementCallback 的主要作用：
 *    捕获共享元素转换的回调: SharedElementCallback 提供了一系列回调方法，例如 onSharedElementStart()、onSharedElementEnd() 和 onMapSharedElements()，这些方法会在共享元素转换的不同阶段被调用。 你可以在这些回调方法中执行自定义操作， 例如修改共享元素的尺寸、 位置或外观。
 *    映射共享元素: 在某些情况下，你可能需要在两个 Activity 之间更改共享元素的映射关系。例如，如果两个 Activity 中的共享元素具有不同的 ID 或 transitionName，你可以使用 onMapSharedElements() 回调方法来创建正确的映射关系。
 *    创建自定义属性映射: 你可以使用 SharedElementCallback 来创建自定义属性映射，以便在共享元素转换期间对视图的属性进行动画处理。
 *
 * 3.MaterialContainerTransform:MaterialContainerTransform 是 Material Design 中的一个过渡动画，用于实现容器之间的变形效果。 它可以将一个视图容器（ 例如 CardView 或 FrameLayout）平滑地转换为另一个视图容器，同时对容器内的内容进行动画处理
 *   可用于在两个 Activity、Fragment、View 或一个 View 和一个 Fragment 之间进行变换。
 *   MaterialContainerTransform 的特点：
 *   共享元素过渡: MaterialContainerTransform 可以与共享元素过渡一起使用，例如将一个列表项中的图片转换为详情页中的大图。
 *   可自定义的形状和颜色: 你可以自定义容器的起始和结束形状，以及过渡过程中的颜色和动画效果。
 *   支持多种过渡模式: MaterialContainerTransform 支持多种过渡模式，例如淡入淡出、滑动和爆炸效果
 *
 * 4.MaterialContainerTransformSharedElementCallback:用于MaterialContainerTransform的SharedElementCallback 。
 */
