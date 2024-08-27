package io.material.catalog.tableofcontents

import dagger.Provides
import dagger.android.ContributesAndroidInjector
import io.material.catalog.a_base.BaseModule
import io.material.catalog.adaptive.AdaptiveFragment
import io.material.catalog.animation.AnimationFragmentModule
import io.material.catalog.application.scope.FragmentScope
import io.material.catalog.bottomappbar.BottomAppBarModule
import io.material.catalog.bottomnav.BottomNavigationModule
import io.material.catalog.bottomsheet.BottomSheetModule
import io.material.catalog.button.ButtonModule
import io.material.catalog.card.CardModule
import io.material.catalog.carousel.CarouselModule
import io.material.catalog.checkbox.CheckBoxModule
import io.material.catalog.chip.ChipModule
import io.material.catalog.color.ColorsFragment
import io.material.catalog.datepicker.DatePickerDemoLandingFragment
import io.material.catalog.dialog.DialogDemoLandingFragment
import io.material.catalog.divider.DividerFragment
import io.material.catalog.elevation.ElevationFragment
import io.material.catalog.fab.FabFragment
import io.material.catalog.font.FontFragment
import io.material.catalog.imageview.ShapeableImageViewFragment
import io.material.catalog.loadingindicator.LoadingIndicatorFragment
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
  includes = [AdaptiveFragment.Module::class, BottomAppBarModule::class, ButtonModule::class, BottomNavigationModule::class, BottomSheetModule::class, CardModule::class, CarouselModule::class, CheckBoxModule::class, ChipModule::class, ColorsFragment.Module::class, DatePickerDemoLandingFragment.Module::class, DialogDemoLandingFragment.Module::class, DividerFragment.Module::class, ElevationFragment.Module::class, FabFragment.Module::class, FontFragment.Module::class, MenuFragment.Module::class, NavigationDrawerFragment.Module::class, NavigationRailFragment.Module::class, ProgressIndicatorFragment.Module::class, RadioButtonFragment.Module::class,
    SearchFragmentModule::class, ShapeableImageViewFragment.Module::class, BaseModule::class, LoadingIndicatorFragment.Module::class, ShapeThemingFragment.Module::class, SideSheetFragment.Module::class, SliderFragment.Module::class, SwitchFragment.Module::class, TabsFragment.Module::class, TextFieldFragment.Module::class, TimePickerDemoLandingFragment.Module::class, TopAppBarModule::class, TransitionModule::class, AnimationFragmentModule::class]
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
