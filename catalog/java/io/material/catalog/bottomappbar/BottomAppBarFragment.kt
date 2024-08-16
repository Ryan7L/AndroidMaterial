//package io.material.catalog.bottomappbar
//
//import androidx.fragment.app.Fragment
//import dagger.Provides
//import dagger.android.ContributesAndroidInjector
//import dagger.multibindings.IntoSet
//import io.material.catalog.R
//import io.material.catalog.application.scope.ActivityScope
//import io.material.catalog.application.scope.FragmentScope
//import io.material.catalog.feature.Demo
//import io.material.catalog.feature.DemoLandingFragment
//import io.material.catalog.feature.FeatureDemo
//
//class BottomAppBarFragment : DemoLandingFragment() {
//  override val titleResId: Int
//    get() = R.string.cat_bottomappbar_title
//  override val descriptionResId: Int
//    get() = R.string.cat_bottomappbar_description
//  override val mainDemo: Demo
//    get() = object : Demo() {
//      override val fragment: Fragment
//        get() = BottomAppBarMainDemoFragment()
//    }
//}
//
//@dagger.Module
//abstract class BottomAppBarFragmentModule {
//  @FragmentScope
//  @ContributesAndroidInjector
//  abstract fun contributeInjector(): BottomAppBarFragment
//  companion object {
//    @JvmStatic
//    @IntoSet
//    @Provides
//    @ActivityScope
//    fun provideFeatureDemo(): FeatureDemo {
//      return object : FeatureDemo(R.string.cat_bottomappbar_title, R.drawable.ic_bottomappbar) {
//        override val landingFragment: Fragment
//          get() = BottomAppBarFragment()
//      }
//    }
//  }
//}
