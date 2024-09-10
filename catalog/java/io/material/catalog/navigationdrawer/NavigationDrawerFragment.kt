package io.material.catalog.navigationdrawer

import android.content.Intent
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

class NavigationDrawerFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_navigationdrawer_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_navigationdrawer_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val activityIntent: Intent
        get() = Intent(requireContext(), NavigationDrawerDemoActivity::class.java)
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_navigationdrawer_custom_title) {
        override val activityIntent: Intent
          get() = Intent(requireContext(), CustomNavigationDrawerDemoActivity::class.java)
      }
    )
}

@dagger.Module
abstract class NavigationDrawerModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): NavigationDrawerFragment

  companion object {
    @JvmStatic
    @IntoSet
    @Provides
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_navigationdrawer_title, R.drawable.ic_side_drawer) {
        override val landingFragment: Fragment
          get() = NavigationDrawerFragment()
      }
    }
  }
}
