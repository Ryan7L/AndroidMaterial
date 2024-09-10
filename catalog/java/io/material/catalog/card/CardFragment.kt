package io.material.catalog.card

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

class CardFragment : DemoLandingFragment() {
  /**
   * ActionBar 或 ToolBar 的标题的资源ID
   */
  override val titleResId: Int
    get() = R.string.cat_card_title

  /**
   * 演示功能的描述的资源ID
   */
  override val descriptionResId: Int
    get() = R.string.cat_card_description

  /**
   * 主要的Demo
   */
  override val mainDemo: Demo
    get() = object : Demo() {
      override val fragment: Fragment
        get() = CardMainDemoFragment()
    }

  override val additionalDemos: List<Demo>
    get() = listOf(
      object : Demo(R.string.cat_card_selection_mode) {
        override val activityIntent: Intent
          get() = Intent(context, CardSelectionModeActivity::class.java)
      },
      object : Demo(R.string.cat_card_draggable_card) {
        override val fragment: Fragment
          get() = DraggableCardFragment()
      },
      object : Demo(R.string.cat_card_states) {
        override val fragment: Fragment
          get() = CardStatesFragment()
      },
      object : Demo(R.string.cat_card_rich_media_demo) {
        override val fragment: Fragment
          get() = CardRichMediaDemoFragment()
      },
      object : Demo(R.string.cat_card_list) {
        override val fragment: Fragment
          get() = CardListDemoFragment()
      },
      object : Demo(R.string.cat_card_swipe_dismiss) {
        override val fragment: Fragment
          get() = CardSwipeDismissFragment()
      }

    )
}

@dagger.Module
abstract class CardModule {

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeInjector(): CardFragment

  companion object {
    @JvmStatic
    @Provides
    @IntoSet
    @ActivityScope
    fun provideFeatureDemo(): FeatureDemo {
      return object : FeatureDemo(R.string.cat_card_title, R.drawable.ic_card) {
        override val landingFragment: Fragment
          get() = CardFragment()
      }
    }
  }
}
