package io.material.catalog.feature

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import io.material.catalog.R
import io.material.catalog.preferences.BaseCatalogActivity
import io.material.catalog.windowpreferences.WindowPreferencesManager

abstract class DemoActivity : BaseCatalogActivity() {
  companion object {
    @JvmField
    val EXTRA_DEMO_TITLE = "demo_title"

    @JvmField
    val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
  }

  private lateinit var tootBar: Toolbar

  private lateinit var demoContainer: ViewGroup

  /**
   * 功能演示的标题的资源ID
   */
  open val demoTitleResId: Int = 0
  override fun onCreate(savedInstanceState: Bundle?) {
    if (isShouldSetUpContainerTransform) {
      findViewById<ViewGroup>(android.R.id.content).transitionName = intent?.getStringExtra(
        EXTRA_TRANSITION_NAME
      )
      setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
      window?.sharedElementEnterTransition = buildContainerTransform(true)
      window?.sharedElementReturnTransition = buildContainerTransform(false)
    }
    super.onCreate(savedInstanceState)
    if (isShouldApplyEdgeToEdgePreference) {
      WindowPreferencesManager(this).applyEdgeToEdgePreference(window)
    }
    setContentView(R.layout.cat_demo_activity)
    tootBar = findViewById(R.id.toolbar)
    demoContainer = findViewById(R.id.cat_demo_activity_container)
    initDefaultActionBar()
    demoContainer.addView(
      onCreateDemoView(
        LayoutInflater.from(this),
        demoContainer,
        savedInstanceState
      )
    )
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return if (item.itemId == android.R.id.home) {
      @Suppress("DEPRECATION")
      onBackPressed()
      true
    } else super.onOptionsItemSelected(item)
  }
  /**
   * 是否应该对容器内的东西设置转换动画效果
   */
  protected open val isShouldSetUpContainerTransform: Boolean
    get() =
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && intent?.getStringExtra(
        EXTRA_TRANSITION_NAME
      ) != null
  /**
   * 是否显示操作栏
   */
  protected open val isShouldShowDefaultDemoActionBar: Boolean
    get() = true
  /**
   * 是否显示操作栏上的关闭按钮
   */
  protected open val isShouldShowDefaultDemoActionBarCloseButton: Boolean
    get() = true
  /**
   * 是否需要配置边到边
   */
  protected open val isShouldApplyEdgeToEdgePreference: Boolean
    get() = true
  /**
   * 创建转换动画
   * @param entering Boolean 是否是进入效果
   * @return MaterialContainerTransform
   */
  private fun buildContainerTransform(entering: Boolean): MaterialContainerTransform {
    return MaterialContainerTransform(this, entering).apply {
      addTarget(android.R.id.content)
      containerColor =
        MaterialColors.getColor(findViewById(android.R.id.content), R.attr.colorSurface)
      fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH

    }
  }

  private fun initDefaultActionBar() {
    if (isShouldShowDefaultDemoActionBar) {
      setSupportActionBar(tootBar)
      if (demoTitleResId != 0) {
        supportActionBar?.setTitle(demoTitleResId)
      } else {
        supportActionBar?.title = intent?.extras?.getString(EXTRA_DEMO_TITLE, "") ?: ""
      }
      if (isShouldShowDefaultDemoActionBarCloseButton) {
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_vd_theme_24px)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
      }
    } else {
      tootBar.visibility = View.GONE
    }

  }
  /**
   * 创建 要演示的功能的视图
   * @param layoutInflater LayoutInflater
   * @param viewGroup ViewGroup?
   * @param bundle Bundle?
   * @return View
   */
  abstract fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View?

}
