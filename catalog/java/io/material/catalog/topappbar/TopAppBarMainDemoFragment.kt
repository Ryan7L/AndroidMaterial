package io.material.catalog.topappbar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils

class TopAppBarMainDemoFragment : DemoFragment() {
  /**
   * 创建可折叠的应用栏 (App Bar)。 它是一个垂直排列的 LinearLayout，可以包含 Toolbar、TabLayout 等其他 View
   *
   * 主要作用：
   *    实现可折叠的 App Bar: AppBarLayout 可以与 CoordinatorLayout 和可滚动 View (例如 NestedScrollView) 结合使用，实现 App Bar 的折叠效果。 当用户向上滚动内容时，App Bar 可以折叠或隐藏，从而为内容区域提供更多空间。
   *    组织 App Bar 内容: AppBarLayout 可以包含多个子 View，例如 Toolbar、TabLayout、ImageView 等，并将它们组织在一个垂直的布局中。
   *    定义滚动行为: AppBarLayout 的子 View 可以通过 app:layout_scrollFlags 属性来定义它们的滚动行为。 例如，您可以指定某个 View 始终显示在顶部，或者在滚动时折叠。
   * 常用场景：
   *    折叠 Toolbar: 这是 AppBarLayout 最常见的用途之一。 当用户向上滚动内容时，Toolbar 可以折叠或隐藏，从而为内容区域提供更多空间。
   *    显示 Tab: 您可以将 TabLayout 添加到 AppBarLayout 中，并在 App Bar 中显示 Tab。
   *    显示 Banner 图片: 您可以在 AppBarLayout 中添加 ImageView 来显示 Banner 图片，并在滚动时折叠或隐藏图片。
   * 关键属性：
   *    app:layout_scrollFlags：用于定义子 View 的滚动行为。 常用的值包括 scroll、enterAlways、enterAlwaysCollapsed、exitUntilCollapsed 等
   */
  private lateinit var appbarLayout: AppBarLayout

  private lateinit var toobar: Toolbar

  private var badgeDrawable: BadgeDrawable? = null

  private lateinit var editMenuToggle: MaterialSwitch

  private lateinit var liftOnScrollToggle: MaterialSwitch

  private lateinit var liftToggle: MaterialSwitch

  private lateinit var incrementBadgeNumber: MaterialButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    @Suppress("DEPRECATION")
    setHasOptionsMenu(true)
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val view = inflater.inflate(R.layout.cat_topappbar_fragment, container, false)
    appbarLayout = view.findViewById(R.id.appbarlayout)
    toobar = view.findViewById(R.id.toolbar)
    (activity as AppCompatActivity).setSupportActionBar(toobar)

    editMenuToggle = view.findViewById(R.id.cat_topappbar_switch_edit_menu)

    editMenuToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      //对favorite 菜单项的badge 先清除在重新设置是应为，当ToolBar 菜单中对 showAsAction属性值为ifRoom/always 的item，改变其可见性会导致其他item位置发生改变
      //ToolBar需要重新分配控件，从而会导致其他item 刷新从而位置发生改变
      BadgeUtils.detachBadgeDrawable(badgeDrawable, toobar, R.id.cat_topappbar_item_favorite)
      toobar.menu.findItem(R.id.cat_topappbar_item_edit).setVisible(isChecked)
      BadgeUtils.attachBadgeDrawable(badgeDrawable!!, toobar, R.id.cat_topappbar_item_favorite)
    }
    liftOnScrollToggle = view.findViewById(R.id.cat_topappbar_switch_lift_on_scroll)
    liftOnScrollToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      //与appbarlayout 协同的view滚动时，appbarlayout是否允许抬升
      appbarLayout.isLiftOnScroll =
        isChecked//用于控制 App Bar 在滚动时的抬升效果。 当 liftOnScroll 设置为 true 时，App Bar 会在关联的可滚动视图向上滚动时抬升，并在向下滚动时恢复到其原始位置,所谓的抬升可以理解为 view的z轴变高了
      liftToggle.isEnabled = !isChecked
      if (!isChecked) {
        appbarLayout.setLifted(liftToggle.isChecked)//是否属于抬起状态
      }
    }
    liftToggle = view.findViewById(R.id.cat_topappbar_switch_lifted)
    liftToggle.setOnCheckedChangeListener { buttonView, isChecked ->
      appbarLayout.setLifted(isChecked)
    }
    incrementBadgeNumber = view.findViewById(R.id.cat_topappbar_button_increment_badge)
    incrementBadgeNumber.setOnClickListener {
      badgeDrawable!!.number += 1
      badgeDrawable!!.isVisible = true
    }
    return view
  }

  @Deprecated("Deprecated in Java")
  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    //将菜单设置给ToolBar或ActionBar
    inflater.inflate(R.menu.cat_topappbar_menu, menu)
    @Suppress("DEPRECATION")
    super.onCreateOptionsMenu(menu, inflater)
    badgeDrawable = BadgeDrawable.create(requireContext())
    badgeDrawable?.number = 1
    BadgeUtils.attachBadgeDrawable(badgeDrawable!!, toobar, R.id.cat_topappbar_item_favorite)
  }

  @Deprecated("Deprecated in Java")
  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.cat_topappbar_item_favorite) {
      badgeDrawable?.clearNumber()
      badgeDrawable?.isVisible = false
    }
    @Suppress("DEPRECATION")
    return super.onOptionsItemSelected(item) || DemoUtils.showSnackBar(requireActivity(), item)
  }

  override val isShouldShowDefaultDemoActionBar: Boolean
    get() = false
}
