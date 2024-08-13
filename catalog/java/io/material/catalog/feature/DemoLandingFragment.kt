package io.material.catalog.feature

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import dagger.android.support.DaggerFragment
import io.material.catalog.R

const val FRAGMENT_DEMO_CONTENT = "fragment_demo_content"

abstract class DemoLandingFragment : DaggerFragment() {
  /**
   * 菜单项Icon 未选中时的颜色
   */
  @ColorInt
  private var menuIconColorUnchecked: Int = 0

  /**
   * 菜单项Icon选中时的颜色
   */
  @ColorInt
  private var menuIconColorChecked: Int = 0

  /**
   * 该Fragment 演示的功能是否应标记为受限
   * 受限功能的示例可能是依赖于高于 MDC 最低 SDK 版本的 API 级别的功能。如果覆盖此方法，
   * 您还应覆盖restrictedMessageId 并提供有关该功能受限原因的信息。
   */
  open val isRestricted = false

  /**
   * 受限原因 的资源Id
   */
  @StringRes
  open val restrictedMessageId = 0

  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  @get:StringRes
  abstract val titleResId: Int

  /**
   * 演示功能的描述的资源ID
   */
  @get:StringRes
  abstract val descriptionResId: Int

  /**
   * 主要的Demo
   */
  abstract val mainDemo: Demo

  /**
   * 资源链接数组 的资源ID，(换句话说就是超链接)
   */
  @ArrayRes
  open val linksArrayResId: Int = -1

  open val linkUrlArrayResId: Int = -1

  /**
   *  额外的功能演示集合
   */
  open val additionalDemos: List<Demo> = emptyList()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    //允许 Fragment 拥有选项菜单
    setHasOptionsMenu(true)
    if (savedInstanceState == null && isFavoriteLaunch()) {
      startDefaultDemoIfNeeded()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    val rootView = inflater.inflate(R.layout.cat_demo_landing_fragment, container, false)

    arguments?.let {
      ViewCompat.setTransitionName(rootView, it.getString(FeatureDemoUtils.ARG_TRANSITION_NAME))
    }
    val toolbar = rootView.findViewById<Toolbar>(R.id.toolbar)
    (activity as? AppCompatActivity)?.let {
      it.setSupportActionBar(toolbar)
      it.supportActionBar?.title = getString(titleResId)
      it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    val toolbarContext = toolbar.context
    toolbarContext.theme.obtainStyledAttributes(
      intArrayOf(
        R.attr.colorOnSurfaceVariant, R.attr.colorPrimary
      )
    ).let {
      menuIconColorChecked = it.getColor(1, 0)
      menuIconColorUnchecked = it.getColor(0, 0)
      it.recycle()
    }

    val descriptionTextView = rootView.findViewById<TextView>(R.id.cat_demo_landing_description)
    val mainDemoContainer =
      rootView.findViewById<ViewGroup>(R.id.cat_demo_landing_main_demo_container)
    //无论该功能是否受到限制，都应添加链接。
    addLinks(inflater, rootView)
    //如果此Fragment Demo 受到限制，由于子类设置的条件，请提前退出而不显示任何演示，仅显示受限消息
    if (isRestricted) {
      val additionalDemosSection =
        rootView.findViewById<ViewGroup>(R.id.cat_demo_landing_additional_demos_section)
      descriptionTextView.setText(restrictedMessageId)
      mainDemoContainer.visibility = View.GONE
      additionalDemosSection.visibility = View.GONE
      return rootView
    }
    descriptionTextView.setText(descriptionResId)
    clearAndAddDemoViews(rootView, inflater)
    DemoUtils.addBottomSpaceInsetsIfNeeded(rootView as ViewGroup, container!!)
    return rootView
  }

  private fun setMenuItemChecked(item: MenuItem, isChecked: Boolean) {
    item.isChecked = isChecked
    MenuItemCompat.setIconTintList(
      item, ColorStateList.valueOf(
        if (isChecked) menuIconColorChecked else menuIconColorUnchecked
      )
    )
  }

  private fun clearAndAddDemoViews(view: View, layoutInflater: LayoutInflater) {
    val mainDemoContainer = view.findViewById<ViewGroup>(R.id.cat_demo_landing_main_demo_container)
    val additionalDemosSection =
      view.findViewById<ViewGroup>(R.id.cat_demo_landing_additional_demos_section)
    val additionalDemosContainer =
      view.findViewById<ViewGroup>(R.id.cat_demo_landing_additional_demos_container)
    mainDemoContainer.removeAllViews()
    additionalDemosContainer.removeAllViews()

    val defaultDemoClassName = FeatureDemoUtils.getDefaultDemo(requireContext())
    addDemoView(layoutInflater, mainDemoContainer, mainDemo, false, defaultDemoClassName)
    additionalDemos.forEach {
      addDemoView(layoutInflater, additionalDemosContainer, it, true, defaultDemoClassName)
    }
    additionalDemosSection.visibility = if (additionalDemos.isEmpty()) View.GONE else View.VISIBLE
  }

  private fun addLinks(layoutInflater: LayoutInflater, view: View) {
    val linksSection = view.findViewById<ViewGroup>(R.id.cat_demo_landing_links_section)
    if (linksArrayResId != -1 && linkUrlArrayResId != -1) {
      val linksStringArray = resources.getStringArray(linksArrayResId)
      val urlArray = resources.getStringArray(linkUrlArrayResId)
      val descToUrlMap = linksStringArray.zip(urlArray).toMap()
      descToUrlMap.forEach {
        addLinkView(layoutInflater, linksSection, it.toPair())
      }
      linksSection.visibility = View.VISIBLE
    } else {
      linksSection.visibility = View.GONE
    }

  }

  private fun addLinkView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup,
    linkString: Pair<String, String>
  ) {
    layoutInflater.inflate(R.layout.cat_demo_landing_link_entry, viewGroup, false).let {
      val desc = SpannableString(linkString.first).apply {
        setSpan(
          URLSpan(linkString.second),
          0,
          linkString.first.length,
          Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
      }

      (it as TextView).apply {
        text = desc
        movementMethod = LinkMovementMethod.getInstance()
      }
      viewGroup.addView(it)
    }
  }


  private fun addDemoView(
    layoutInflater: LayoutInflater,
    demoContainer: ViewGroup,
    demo: Demo,
    isAdditional: Boolean,
    defaultDemoClassName: String
  ) {
    val demoView = layoutInflater.inflate(R.layout.cat_demo_landing_row, demoContainer, false)
    val rootView = demoView.findViewById<View>(R.id.cat_demo_landing_row_root)
    val titlesView = demoView.findViewById<View>(R.id.cat_demo_landing_row_titles)
    val titleTextView = demoView.findViewById<TextView>(R.id.cat_demo_landing_row_title)

    val subtitleTextView = demoView.findViewById<TextView>(R.id.cat_demo_landing_row_subtitle)

    val favoriteButton = demoView.findViewById<MaterialButton>(R.id.cat_demo_landing_row_favorite)
    val transitionName = getString(demo.titleResId)
    ViewCompat.setTransitionName(rootView, transitionName)
    rootView.setOnClickListener {
      startDemo(demo, it, transitionName)
    }
    titleTextView.setText(demo.titleResId)
    subtitleTextView.text = demo.demoClassName
    favoriteButton.isChecked = defaultDemoClassName == demo.demoClassName
    favoriteButton.setOnClickListener {
      updateFavoriteDemoLandingPreference(favoriteButton.isChecked)
      updateFavoriteDemoPreference(demo, favoriteButton.isChecked)
      //确保Demo行和工具栏中最喜欢的图标处于正确的状态。
      clearAndAddDemoViews(requireView(), layoutInflater)
      requireActivity().invalidateOptionsMenu()
    }
    if (isAdditional) {
      setMarginStart(titlesView, R.dimen.cat_list_text_margin_from_icon_large)
    }
    demoContainer.addView(demoView)
  }

  private fun startDemo(demo: Demo) {
    startDemo(demo, null, null)
  }

  private fun startDemo(demo: Demo, sharedElement: View?, transitionName: String?) {
    demo.fragment?.let {
      startDemoFragment(it, sharedElement, transitionName)
      return
    }
    demo.activityIntent?.let {
      startDemoActivity(it, sharedElement, transitionName)
      return
    }
    throw IllegalStateException("Demo must implement createFragment or createActivityIntent")
  }


  private fun startDemoFragment(fragment: Fragment, sharedElement: View?, transitionName: String?) {
    Bundle().apply {
      putString(DemoFragment.ARG_DEMO_TITLE, getString(titleResId))
      putString(FeatureDemoUtils.ARG_TRANSITION_NAME, transitionName)
      fragment.arguments = this
    }
    FeatureDemoUtils.startFragment(
      activity!!,
      fragment,
      FRAGMENT_DEMO_CONTENT,
      sharedElement,
      transitionName
    )
  }

  private fun startDemoActivity(intent: Intent, sharedElement: View?, transitionName: String?) {
    intent.putExtra(DemoActivity.EXTRA_DEMO_TITLE, getString(titleResId))

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && sharedElement != null && transitionName != null) {
      intent.putExtra(DemoActivity.EXTRA_TRANSITION_NAME, transitionName)
      //设置共享元素过渡并禁用覆盖，以便视图不会显示在系统栏上方
      activity?.let {
        it.setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        it.window.setSharedElementsUseOverlay(false)
        val options =
          ActivityOptions.makeSceneTransitionAnimation(it, sharedElement, transitionName)
        startActivity(intent, options.toBundle())

      }
    } else {
      startActivity(intent)
    }
  }

  private fun updateFavoriteDemoLandingPreference(isChecked: Boolean) {
    FeatureDemoUtils.saveDefaultDemoLanding(
      requireContext(),
      if (isChecked) javaClass.name else ""
    )
  }

  private fun updateFavoriteDemoPreference(demo: Demo, isChecked: Boolean) {
    FeatureDemoUtils.saveDefaultDemo(requireContext(), if (isChecked) demo.demoClassName else "")
  }

  private fun clearFavoriteDemoPreference() {
    FeatureDemoUtils.saveDefaultDemo(requireContext(), "")
  }

  /**
   * 启动默认的Demo
   */
  private fun startDefaultDemoIfNeeded() {
    val defaultDemo = FeatureDemoUtils.getDefaultDemo(requireContext())
    if (defaultDemo.isNotEmpty()) {
      val allDemos = mutableListOf<Demo>()
      allDemos.add(mainDemo)
      allDemos.addAll(additionalDemos)
      allDemos.forEach {
        if (it.demoClassName == defaultDemo) {
          startDemo(it)
          return
        }
      }
    }
  }


  private fun isFavoriteLaunch(): Boolean =
    arguments?.getBoolean(FeatureDemo.KEY_FAVORITE_LAUNCH) ?: false


  private fun setMarginStart(view: View, @DimenRes marginResId: Int) {
    val margin = resources.getDimensionPixelOffset(marginResId)
    val layoutParams = view.layoutParams as? ViewGroup.MarginLayoutParams
    layoutParams?.marginStart = margin
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.mtrl_favorite_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onPrepareOptionsMenu(menu: Menu) {
    val item = menu.findItem(R.id.favorite_toggle)
    val isChecked =
      FeatureDemoUtils.getDefaultDemoLanding(requireContext()) == this::class.java.name
    setMenuItemChecked(item, isChecked)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.favorite_toggle) {
      val isChecked = !item.isChecked
      updateFavoriteDemoLandingPreference(isChecked)
      clearFavoriteDemoPreference()
      setMenuItemChecked(item, isChecked)
      clearAndAddDemoViews(requireView(), layoutInflater)
      return true
    }
    return super.onOptionsItemSelected(item)
  }

}
