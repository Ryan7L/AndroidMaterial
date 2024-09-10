package io.material.catalog.timepicker

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

class TimePickerDemoLandingFragment : DemoLandingFragment() {
  override val titleResId: Int
    get() = R.string.cat_time_picker_demo_title
  override val descriptionResId: Int
    get() = R.string.cat_time_picker_description
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = TimePickerMainDemoFragment()
    }
}

@dagger.Module
abstract class TimePickerModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TimePickerDemoLandingFragment

  companion object {
    @ActivityScope
    @IntoSet
    @Provides
    @JvmStatic
    fun featureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_time_picker_demo_title, R.drawable.ic_placeholder) {
        override val landingFragment: Fragment
          get() = TimePickerDemoLandingFragment()
      }
    }
  }
}
