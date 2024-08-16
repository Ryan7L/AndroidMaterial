package io.material.catalog.tableofcontents

import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.material.catalog.adaptive.AdaptiveFragment
import io.material.catalog.animation.AnimationFragmentModule
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.bottomappbar.BottomAppBarFragment
import io.material.catalog.bottomnav.BottomNavigationFragment
import io.material.catalog.bottomsheet.BottomSheetFragment
import io.material.catalog.button.ButtonsFragment
import io.material.catalog.card.CardFragment
import io.material.catalog.carousel.CarouselFragment
import io.material.catalog.checkbox.CheckBoxFragment
import io.material.catalog.chip.ChipFragment
import io.material.catalog.color.ColorsFragment
import io.material.catalog.datepicker.DatePickerDemoLandingFragment
import io.material.catalog.dialog.DialogDemoLandingFragment
import io.material.catalog.divider.DividerFragment
import io.material.catalog.elevation.ElevationFragment
import io.material.catalog.fab.FabFragment
import io.material.catalog.font.FontFragment
import io.material.catalog.imageview.ShapeableImageViewFragment
import io.material.catalog.materialswitch.SwitchFragment
import io.material.catalog.menu.MenuFragment
import io.material.catalog.navigationdrawer.NavigationDrawerFragment
import io.material.catalog.navigationrail.NavigationRailFragment
import io.material.catalog.preferences.CatalogPreferencesDialogFragment
import io.material.catalog.progressindicator.ProgressIndicatorFragment
import io.material.catalog.radiobutton.RadioButtonFragment
import io.material.catalog.search.SearchFragmentModule
import io.material.catalog.shapetheming.ShapeThemingFragment
import io.material.catalog.sidesheet.SideSheetFragment
import io.material.catalog.slider.SliderFragment
import io.material.catalog.tabs.TabsFragment
import io.material.catalog.textfield.TextFieldFragment
import io.material.catalog.timepicker.TimePickerDemoLandingFragment
import io.material.catalog.topappbar.TopAppBarModule
import io.material.catalog.transition.TransitionModule

@dagger.Module(
  includes = [AdaptiveFragment.Module::class, BottomAppBarFragment.Module::class, ButtonsFragment.Module::class, BottomNavigationFragment.Module::class, BottomSheetFragment.Module::class, CardFragment.Module::class, CarouselFragment.Module::class, CheckBoxFragment.Module::class, ChipFragment.Module::class, ColorsFragment.Module::class, DatePickerDemoLandingFragment.Module::class, DialogDemoLandingFragment.Module::class, DividerFragment.Module::class, ElevationFragment.Module::class, FabFragment.Module::class, FontFragment.Module::class, MenuFragment.Module::class, NavigationDrawerFragment.Module::class, NavigationRailFragment.Module::class, ProgressIndicatorFragment.Module::class, RadioButtonFragment.Module::class,
    SearchFragmentModule::class, ShapeableImageViewFragment.Module::class, ShapeThemingFragment.Module::class, SideSheetFragment.Module::class, SliderFragment.Module::class, SwitchFragment.Module::class, TabsFragment.Module::class, TextFieldFragment.Module::class, TimePickerDemoLandingFragment.Module::class, TopAppBarModule::class, TransitionModule::class, AnimationFragmentModule::class]
)
abstract class TocModule {
  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeTocFragment(): TocFragment

  @FragmentScope
  @ContributesAndroidInjector
  abstract fun contributeCatalogPreferencesDialogFragment(): CatalogPreferencesDialogFragment

  companion object {
    @Provides
    fun provideTocResourceProvider(): TocResourceProvider {
      return TocResourceProvider()
    }
  }
}
