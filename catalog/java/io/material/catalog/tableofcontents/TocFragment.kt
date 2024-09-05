package io.material.catalog.tableofcontents

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.transition.MaterialSharedAxis
import dagger.android.support.DaggerFragment
import io.material.catalog.R
import io.material.catalog.feature.FeatureDemo
import io.material.catalog.feature.FeatureDemoUtils
import io.material.catalog.preferences.CatalogPreferencesDialogFragment
import java.text.Collator
import javax.inject.Inject
import kotlin.math.abs

private const val FRAGMENT_CONTENT = "fragment_content"
private const val GRID_SPAN_COUNT_MIN = 1
private const val GRID_SPAN_COUNT_MAX = 4

@Dimension(unit = Dimension.DP)
private const val CATALOG_NARROW_SCREEN_SIZE_CUTOFF = 350

class TocFragment : DaggerFragment() {
  //@JvmSuppressWildcards 注解用于指示 Kotlin 编译器不要生成带有通配符类型的 Java 泛型。
  //在 Kotlin 中，泛型类型参数默认情况下会被编译成 Java 的通配符类型。例如，List<String> 会被编译成 List<? extends String>。这在大多数情况下是没问题的，但有时您可能希望 Java 代码看到具体的泛型类型，而不是通配符类型。
  //在这种情况下，您可以使用 @JvmSuppressWildcards 注解来抑制通配符类型的生成
  @Inject
  lateinit var featureDemos: Set<@JvmSuppressWildcards FeatureDemo>

  @Inject
  lateinit var resourceProvider: TocResourceProvider

  private lateinit var appbarLayout: AppBarLayout

  private lateinit var searchView: SearchView

  private lateinit var headerContainer: ConstraintLayout

  private lateinit var preferenceBtn: ImageButton

  private lateinit var searchBtn: ImageButton

  private lateinit var adapter: TocAdapter

  private lateinit var gridTopDivider: View

  private lateinit var openSearchViewTransition: Transition

  private lateinit var closeSearchViewTransition: Transition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState == null) {
      startDefaultDemoLandingIfNeeded()
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View? {
    val rootView = inflater.inflate(R.layout.cat_toc_fragment, container, false)
    (activity as AppCompatActivity).let {
      it.setSupportActionBar(rootView.findViewById(R.id.toolbar))
      it.supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    val content = rootView.findViewById<ViewGroup>(R.id.content)
    View.inflate(context, R.layout.cat_toc_header, content)

    appbarLayout = rootView.findViewById(R.id.cat_toc_app_bar_layout)
    gridTopDivider = rootView.findViewById(R.id.cat_toc_grid_top_divider)
    headerContainer = rootView.findViewById(R.id.cat_toc_header_container)
    val rv = rootView.findViewById<RecyclerView>(R.id.cat_toc_grid)
    preferenceBtn = rootView.findViewById(R.id.cat_toc_preferences_button)
    searchView = rootView.findViewById(R.id.cat_toc_search_view)
    searchBtn = rootView.findViewById(R.id.cat_toc_search_button)

    View.inflate(context, resourceProvider.logoLayout, headerContainer)

    ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
      @Suppress("DEPRECATION") appbarLayout.findViewById<CollapsingToolbarLayout>(R.id.cat_toc_collapsingtoolbarlayout)
        .setPadding(0, insets.systemWindowInsetTop, 0, 0)
      insets
    }
      addGridTopDividerVisibilityListener()

    val spanCount = calculateGridSpanCount()
    rv.layoutManager = GridLayoutManager(context, spanCount)
    rv.addItemDecoration(
      GridDividerDecoration(
        resources.getDimensionPixelSize(R.dimen.cat_toc_grid_divider_size),
        ContextCompat.getColor(requireContext(), R.color.cat_toc_grid_divider_color),
        spanCount
      )
    )

    val demoList = featureDemos.toList()

    //按字母顺序排序
    val collator = Collator.getInstance()
    demoList.sortedWith { demo1, demo2 ->
      collator.compare(
        requireContext().getString(demo1.titleResId), requireContext().getString(demo2.titleResId)
      )
    }
    try {
      demoList.forEach {
        Log.d("TAG", "onCreateView: ${it.landingFragment.javaClass.name}")
      }
    }catch (e: Exception){
      Log.d("TAG", "onCreateView: ${e.message}")
    }
//    demoList.forEach {
//      Log.d("TAG", "onCreateView: ${it.landingFragment.javaClass.name}")
//    }
    adapter = TocAdapter(requireActivity(), demoList.toMutableList())
    rv.adapter = adapter
    adjustLogoConstraintsForNarrowScreenWidths()
    initPreferenceBtn()
    initSearchBtn()
    initSearchView()
    initSearchViewTransition()
    return rootView
  }

  private fun adjustLogoConstraintsForNarrowScreenWidths() {
    //调整logo约束，使logo文本不会与搜索图像按钮重叠。
    if (screenWidth < CATALOG_NARROW_SCREEN_SIZE_CUTOFF) {
      createNarrowHeaderConstraintSet(headerContainer).applyTo(headerContainer)
    }
  }

  @get:Dimension(Dimension.DP)
  private val screenWidth: Int
    get() = resources.configuration.screenWidthDp

  private fun createNarrowHeaderConstraintSet(layout: ConstraintLayout): ConstraintSet {
    return ConstraintSet().apply {
      clone(layout)
      connect(R.id.header_logo, ConstraintSet.END, R.id.cat_toc_search_button, ConstraintSet.START)
      //在logo的起始侧添加一点空间，以补偿搜索图标按钮由于其 48dp 最小触摸目标宽度而在每侧包含的额外 12dp 空间。
      setMargin(
        R.id.header_logo,
        ConstraintSet.START,
        resources.getDimensionPixelOffset(R.dimen.cat_toc_header_additional_start_margin)
      )
    }
  }

  private fun addGridTopDividerVisibilityListener() {
    appbarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
      if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
        // CollapsingToolbarLayout 已折叠，隐藏顶部分隔线
        gridTopDivider.visibility = View.GONE
      } else {
        // CollapsingToolbarLayout 已展开或正在展开，显示顶部分隔线
        gridTopDivider.visibility = View.VISIBLE
      }
    }
  }

  private fun calculateGridSpanCount(): Int {
    val displayMetrics = resources.displayMetrics
    val displayWith = displayMetrics.widthPixels
    val itemSize = resources.getDimensionPixelSize(R.dimen.cat_toc_item_size)
    val gridSpanCount = displayWith / itemSize
    return gridSpanCount.coerceIn(GRID_SPAN_COUNT_MIN, GRID_SPAN_COUNT_MAX)
  }

  private fun initPreferenceBtn() {
    preferenceBtn.setOnClickListener {
      CatalogPreferencesDialogFragment().show(parentFragmentManager, "preferences-screen")
    }
  }

  private fun initSearchBtn() {
    searchBtn.setOnClickListener {
      openSearchView()
    }
  }

  private fun initSearchView() {
    searchView.setOnClickListener {
      closeSearchView()
    }
    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      /**
       * 当用户提交查询时调用。这可能是由于按下键盘上的某个键或按下提交按钮所致。侦听器可以通过返回 true 来覆盖标准行为，以表明它已处理提交请求。
       * 否则返回 false，让 SearchView 通过启动任何相关意图来处理提交。
       * @param query String  要提交的查询文本
       * @return Boolean 如果 SearchView 应该执行显示任何可用建议的默认操作，则为 false；如果该操作由侦听器处理，则为 true。
       *                  返回 false 表示您没有处理提交事件，SearchView 应该执行默认操作（但实际上默认操作是什么都不做)
       */
      override fun onQueryTextSubmit(query: String?): Boolean {
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        adapter.filter.filter(newText)
        return false
      }

    })
  }

  private fun initSearchViewTransition() {
    openSearchViewTransition = createSearchViewTransition(true)
    closeSearchViewTransition = createSearchViewTransition(false)
  }

  private fun openSearchView() {
    //用于在当前布局上执行延迟过渡动画。  作用： 当您调用 beginDelayedTransition() 方法时，它会捕获当前布局的状态，并在您修改布局后自动应用过渡动画。
    // 这意味着您无需手动创建和启动动画， TransitionManager 会为您处理一切
    TransitionManager.beginDelayedTransition(headerContainer, openSearchViewTransition)
    headerContainer.visibility = View.GONE
    searchView.visibility = View.VISIBLE
    searchView.requestFocus()
  }

  private fun closeSearchView() {
    TransitionManager.beginDelayedTransition(headerContainer, closeSearchViewTransition)
    headerContainer.visibility = View.VISIBLE
    searchView.visibility = View.GONE
    clearSearchView()
  }

  private fun createSearchViewTransition(entering: Boolean): MaterialSharedAxis {
    return MaterialSharedAxis(MaterialSharedAxis.X, entering).apply {
      //addTarget() 方法用于指定哪些 View 应该参与过渡动画,这意味着当过渡动画发生时， 这两个 View 会沿着 X 轴进行移动和淡入淡出
      addTarget(headerContainer)
      addTarget(searchView)
    }
  }


  override fun onPause() {
    super.onPause()
    clearSearchView()
  }

  private fun clearSearchView() {
    searchView.setQuery("", true)
  }

  private fun startDefaultDemoLandingIfNeeded() {
    val defaultDemoLanding = FeatureDemoUtils.getDefaultDemoLanding(requireContext())
    Log.d("TAG", "startDefaultDemoLandingIfNeeded: $defaultDemoLanding")
    if (defaultDemoLanding.isNotEmpty()) {
      featureDemos.forEach {
        val fragment = it.landingFragment
        if (fragment.javaClass.name == defaultDemoLanding) {
          val args = fragment.arguments ?: Bundle()
          fragment.arguments = args.apply { putBoolean(FeatureDemo.KEY_FAVORITE_LAUNCH, true) }
          FeatureDemoUtils.startFragment(requireActivity(), fragment, FRAGMENT_CONTENT)
          return

        }
      }
    }
  }
}
