package io.material.catalog.preferences

import android.content.Context
import android.util.SparseIntArray
import com.google.common.collect.ImmutableList
import io.material.catalog.R

private const val SHAPE_CORNER_FAMILY_OPTION_ID_ROUNDED = 1
private const val SHAPE_CORNER_FAMILY_OPTION_ID_CUT = 2
private const val SHAPE_CORNER_FAMILY_OPTION_ID_DEFAULT = 3

private val DEFAULT_OPTION = Option(
  SHAPE_CORNER_FAMILY_OPTION_ID_DEFAULT,
  0,
  R.string.shape_corner_family_preference_option_default
)
private val optionList = ImmutableList.of(
  DEFAULT_OPTION,
  Option(
    SHAPE_CORNER_FAMILY_OPTION_ID_ROUNDED,
    R.drawable.ic_rounded_corners_24px,
    R.string.shape_corner_family_preference_option_rounded
  ),
  Option(
    SHAPE_CORNER_FAMILY_OPTION_ID_CUT,
    R.drawable.ic_cut_corners_24px,
    R.string.shape_corner_family_preference_option_cut
  )
)
private val SHAPE_CORNER_FAMILY_OPTION_ID_TO_THEME_OVERLAY = SparseIntArray().apply {
  append(SHAPE_CORNER_FAMILY_OPTION_ID_ROUNDED, R.style.ThemeOverlay_Shapes_Rounded)
  append(SHAPE_CORNER_FAMILY_OPTION_ID_CUT, R.style.ThemeOverlay_Shapes_Cut)
  append(SHAPE_CORNER_FAMILY_OPTION_ID_DEFAULT, ThemeOverlayUtils.NO_THEME_OVERLAY)
}

/**
 * 形状配置项
 */
class ShapeCornerFamilyPreference :
  CatalogPreference(R.string.shape_corner_family_preference_description) {
  override val isShouldRecreateActivityOnOptionChanged: Boolean = true


  override fun apply(context: Context, selectedOption: Option) {
    ThemeOverlayUtils.setThemeOverlay(
      R.id.theme_feature_corner_family,
      SHAPE_CORNER_FAMILY_OPTION_ID_TO_THEME_OVERLAY.get(selectedOption.optionId)
    )
  }

  override val options: List<Option> = optionList

  override val defaultOption: Option = DEFAULT_OPTION
}
