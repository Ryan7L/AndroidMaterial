package io.material.catalog.z_ryan.bar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoSet;
import io.material.catalog.R;
import io.material.catalog.application.scope.ActivityScope;
import io.material.catalog.application.scope.FragmentScope;
import io.material.catalog.feature.FeatureDemo;

@dagger.Module
public abstract class BarModule {
  @IntoSet
  @Provides
  @ActivityScope
  static FeatureDemo provideFeatureDemo() {
    return new FeatureDemo(R.string.cat_bar_title, R.drawable.ic_topappbar) {
      @Nullable
      @Override
      public Fragment getLandingFragment() {
        return new BarFragment();
      }
    };
  }

  @FragmentScope
  @ContributesAndroidInjector
  abstract BarFragment contributeInjector();
}
