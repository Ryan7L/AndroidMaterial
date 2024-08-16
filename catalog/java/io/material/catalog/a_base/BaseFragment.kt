package io.material.catalog.a_base

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.feature.Demo
import io.material.catalog.feature.DemoLandingFragment
import io.material.catalog.feature.FeatureDemo
import io.material.catalog.feature.FeatureDemoUtils

class BaseFragment : DemoLandingFragment() {

  private var categoryMap = mapOf(
    R.string.cat_category_view to object : Demo(R.string.cat_category_view) {
      override val fragment: Fragment?
        get() = ViewFragment()
    },
    R.string.cat_category_view_group to object : Demo(R.string.cat_category_view_group) {
      override val fragment: Fragment
        get() = ViewGroupFragment()
    }
  )

  private fun addCategoryView(layoutInflater: LayoutInflater,container: LinearLayout, @StringRes titleResId: Int,demo: Demo) {
    val layout = LayoutInflater.from(context).inflate(R.layout.category_layout, container, false)
    container.addView(layout)
    layout.findViewById<TextView>(R.id.category_title).setText(titleResId)
    val categoryContainer = layout.findViewById<LinearLayout>(R.id.category_container)
    val defaultDemoClassName = FeatureDemoUtils.getDefaultDemo(requireContext())
    addDemoView(layoutInflater, categoryContainer, demo, false, defaultDemoClassName)
  }

  override fun clearAndAddDemoViews(view: View, layoutInflater: LayoutInflater) {
    super.clearAndAddDemoViews(view, layoutInflater)
    val container = view.findViewById<LinearLayout>(R.id.demo_root)
    categoryMap.forEach { (titleResId, demo) ->
      addCategoryView(layoutInflater,container, titleResId,demo)
    }

  }


  override val titleResId: Int
    get() = R.string.cat_base_title
  override val descriptionResId: Int
    get() = R.string.cat_base_description
  override val mainDemo: Demo
    get() = object : Demo(R.string.cat_category_view_group) {
      override val fragment: Fragment
        get() = ViewGroupFragment()
    }
}

@dagger.Module
abstract class BaseModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): BaseFragment


  companion object {
    @JvmStatic
    @IntoSet
    @Provides
    @ActivityScope
    fun providerFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_base_title, R.drawable.ic_dynamic_color_24px) {
        override val landingFragment: Fragment
          get() = BaseFragment()

      }
    }
  }
}
