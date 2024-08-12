package io.material.catalog.transition

import android.content.Intent
import android.os.Build
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
import io.material.catalog.musicplayer.MusicPlayerDemoActivity

open class TransitionFragment : DemoLandingFragment() {
  private val PLATFORM_TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
  override val titleResId: Int = R.string.cat_transition_title
  override val descriptionResId: Int = R.string.cat_transition_description
  override val mainDemo: Demo = object : Demo() {
    override val activityIntent: Intent
      get() = Intent(context, MusicPlayerDemoActivity::class.java)

  }
  override val additionalDemos: List<Demo>
    get() = mutableListOf<Demo>().apply {
      if (PLATFORM_TRANSITIONS_AVAILABLE) {
        add(
          object : Demo(R.string.cat_transition_container_transform_activity_title) {
            override val activityIntent: Intent
              get() = Intent(context, TransitionContainerTransformStartDemoActivity::class.java)
          }
        )
      }
      add(
        object : Demo(R.string.cat_transition_container_transform_fragment_title) {
          override val fragment: Fragment
            get() = TransitionContainerTransformDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_container_transform_view_title) {
          override val fragment: Fragment
            get() = TransitionContainerTransformViewDemoFragment()
        }
      )
      if (PLATFORM_TRANSITIONS_AVAILABLE) {
        add(
          object : Demo(R.string.cat_transition_shared_axis_activity_title) {
            override val activityIntent: Intent
              get() = Intent(context, TransitionSharedAxisStartDemoActivity::class.java)
          }
        )
      }
      add(
        object : Demo(R.string.cat_transition_shared_axis_fragment_title) {
          override val fragment: Fragment
            get() = TransitionSharedAxisDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_shared_axis_view_title) {
          override val fragment: Fragment
            get() = TransitionSharedAxisViewDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_fade_through_title) {
          override val fragment: Fragment
            get() = TransitionFadeThroughDemoFragment()
        }
      )
      add(
        object : Demo(R.string.cat_transition_fade_title) {
          override val fragment: Fragment
            get() = TransitionFadeDemoFragment()
        }
      )
    }.toList()

}

@dagger.Module
abstract class TransitionModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): TransitionFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_transition_title, R.drawable.ic_transition) {
        override val landingFragment: Fragment
          get() = TransitionFragment()
      }

    }
  }
}
