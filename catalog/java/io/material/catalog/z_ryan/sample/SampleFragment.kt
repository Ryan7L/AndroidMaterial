package io.material.catalog.z_ryan.sample

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

class SampleFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int = R.string.cat_sample_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int = R.string.cat_sample_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo = object : Demo() {
    override val activityIntent: Intent
      get() = Intent(context, SampleActivity::class.java)
  }
}

@dagger.Module
abstract class SampleFragmentModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): SampleFragment

  companion object {
    @IntoSet
    @Provides
    @ActivityScope
    @JvmStatic
    fun provideFeatureDemo(): FeatureDemo =
      object : FeatureDemo(R.string.cat_text_view_title, R.drawable.ic_text_field_24px) {
        override val landingFragment: Fragment = SampleFragment()
      }
  }

}
