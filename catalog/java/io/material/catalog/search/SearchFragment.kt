package io.material.catalog.search

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

class SearchFragment : DemoLandingFragment() {
  override val titleResId: Int
    get() = R.string.cat_searchbar_title
  override val descriptionResId: Int
    get() = R.string.cat_searchbar_description
  override val mainDemo: Demo
    get() = object : Demo() {
      override val activityIntent: Intent
        get() = Intent(context, SearchMainDemoActivity::class.java)
    }
  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_searchbar_recycler_title) {
        override val activityIntent: Intent
          get() = Intent(context, SearchRecyclerDemoActivity::class.java)
      }
    )

}

@dagger.Module
abstract class SearchFragmentModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): SearchFragment

  companion object {
    @IntoSet
    @Provides
    @ActivityScope
    @JvmStatic
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_searchbar_title, R.drawable.ic_search_bar) {
        override val landingFragment: Fragment
          get() = SearchFragment()
      }
    }
  }

}
