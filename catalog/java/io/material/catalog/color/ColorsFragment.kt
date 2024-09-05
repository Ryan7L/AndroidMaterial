package io.material.catalog.color

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.HarmonizedColors
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoSet
import io.material.catalog.R
import io.material.catalog.application.scope.ActivityScope
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.feature.Demo
import io.material.catalog.feature.DemoLandingFragment
import io.material.catalog.feature.FeatureDemo

class ColorsFragment : DemoLandingFragment() {
  override val titleResId: Int
    get() = R.string.cat_colors_title
  override val descriptionResId: Int
    get() = R.string.cat_colors_description
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = ColorMainDemoFragment()
    }
  override val additionalDemos: List<Demo>
    get() {
      val demos: MutableList<Demo> = ArrayList()
      if (DynamicColors.isDynamicColorAvailable()) {
        demos.add(
          object : Demo(R.string.cat_color_dynamic_palette) {
            override val fragment: Fragment
              get() = ColorDynamicDemoFragment()
          }
        )
      }
      if (HarmonizedColors.isHarmonizedColorAvailable()) {
        demos.add(
          object : Demo(R.string.cat_color_harmonization) {
            override val activityIntent: Intent
              get() = Intent(context, ColorHarmonizationDemoActivity::class.java)
          }
        )
      }
      return demos.toList()
    }
}

@dagger.Module
abstract class ColorModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): ColorsFragment

  companion object {
    @JvmStatic
    @ActivityScope
    @IntoSet
    @Provides
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_colors_title, R.drawable.ic_placeholder) {
        override val landingFragment: DemoLandingFragment
          get() = ColorsFragment()
      }
    }
  }
}
