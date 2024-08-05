package io.material.catalog.feature

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.material.catalog.R
import io.material.catalog.preferences.PreferencesDialogHelper
import io.material.catalog.preferences.PreferencesFragment
import javax.inject.Inject


/**
 * 为单个类型的单个功能 提供演示的屏幕结构的基类。
 */
abstract class DemoFragment : Fragment(), PreferencesFragment, HasAndroidInjector {
  companion object {
    @JvmField
    val ARG_DEMO_TITLE = "demo_title"
  }

  @Inject
  lateinit var childFragmentInjector: DispatchingAndroidInjector<Any>

  private lateinit var toolbar: Toolbar

  private lateinit var demoContainer: ViewGroup

  private var preferencesDialogHelper: PreferencesDialogHelper? = null

  /**
   * Demo 的 标题的资源Id
   */
  @StringRes
  open val demoTitleResId: Int = 0

  override fun onAttach(context: Context) {
    safeInject()
    super.onAttach(context)
    preferencesDialogHelper = PreferencesDialogHelper.createHelper(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.cat_demo_fragment, container, false)
    arguments?.let {
      val transitionName = it.getString(FeatureDemoUtils.ARG_TRANSITION_NAME)
      ViewCompat.setTransitionName(rootView, transitionName)
    }
    toolbar = rootView.findViewById(R.id.toolbar)
    demoContainer = rootView.findViewById(R.id.cat_demo_fragment_container)
    initDemoActionBar()
    val demoView = onCreateDemoView(inflater, container, savedInstanceState)
    demoContainer.addView(demoView)

    (demoView?.layoutParams as CoordinatorLayout.LayoutParams).behavior =
      AppBarLayout.ScrollingViewBehavior()

    val liftOnScrollTargetViewId = liftOnScrollTargetViewId
    if (liftOnScrollTargetViewId != View.NO_ID) {
      rootView.findViewById<AppBarLayout>(R.id.cat_demo_fragment_appbar)
        .liftOnScrollTargetViewId = liftOnScrollTargetViewId
    }
    val children = demoContainer.getChildAt(0) as ViewGroup
    DemoUtils.addBottomSpaceInsetsIfNeeded(children, demoContainer)
    return rootView
  }

  override val isShouldShowDefaultDemoActionBar: Boolean = true

  /**
   * 设置AppBarLayout应该使用哪个视图的 id 来确定是否应该被抬起
   * 当该视图滚动时，AppBarLayout 会提升 (lift)。 作用:
   * 联动滚动: 通常情况下，AppBarLayout 会根据其直接子视图 (例如 Toolbar) 的滚动行为来决定是否提升。
   * 但是，有时你可能希望 AppBarLayout 根据其他视图的滚动行为来提升，例如 RecyclerView 或 NestedScrollView。 这时，你就可以使用 setLiftOnScrollTargetViewId() 方法来指定目标视图的 ID。
   * 提升效果: 当目标视图向上滚动时，AppBarLayout 会向上提升，从而显示更多内容。 当目标视图向下滚动时，AppBarLayout 会向下移动，直到回到其初始位置。
   */
  open val liftOnScrollTargetViewId: Int = View.NO_ID

  override fun androidInjector(): AndroidInjector<Any> = childFragmentInjector

  private fun safeInject() {
    try {
      AndroidSupportInjection.inject(this)
    } catch (e: Exception) {
      //忽略异常，并非所有DemoFragment子类都需要注入
      e.printStackTrace()
    }
  }

  private fun initDemoActionBar() {
    if (isShouldShowDefaultDemoActionBar) {
      (activity as AppCompatActivity).let {
        it.setSupportActionBar(toolbar)
        it.supportActionBar?.let { bar -> setDemoActionBarTitle(bar) }
      }
    } else {
      toolbar.visibility = View.GONE
    }
  }

  private fun setDemoActionBarTitle(actionBar: ActionBar) {
    if (demoTitleResId != 0) {
      actionBar.setTitle(demoTitleResId)
    } else {
      actionBar.title = defaultDemoTitle
    }
  }

  protected open val defaultDemoTitle = arguments?.getString(ARG_DEMO_TITLE, "") ?: ""

  @Deprecated("Deprecated in Java")
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    @Suppress("DEPRECATION")
    super.onCreateOptionsMenu(menu, inflater)
    preferencesDialogHelper?.onCreateOptionsMenu(menu, inflater)
  }

  @Suppress("DEPRECATION")
  @Deprecated("Deprecated in Java")
  override fun onOptionsItemSelected(item: MenuItem): Boolean =
    preferencesDialogHelper?.onOptionsItemSelected(item) == true || super.onOptionsItemSelected(item)

  abstract fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View?
}
