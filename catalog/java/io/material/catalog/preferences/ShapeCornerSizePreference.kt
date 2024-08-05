package io.material.catalog.preferences

import android.content.Context
import android.util.SparseIntArray
import com.google.common.collect.ImmutableList
import io.material.catalog.R

private const val SHAPE_CORNER_SIZE_OPTION_ID_NONE = 1
private const val SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_SMALL = 2
private const val SHAPE_CORNER_SIZE_OPTION_ID_SMALL = 3
private const val SHAPE_CORNER_SIZE_OPTION_ID_MEDIUM = 4
private const val SHAPE_CORNER_SIZE_OPTION_ID_LARGE = 5
private const val SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_LARGE = 6
private const val SHAPE_CORNER_SIZE_OPTION_ID_FULL = 7
private const val SHAPE_CORNER_SIZE_OPTION_ID_DEFAULT = 8
private val DEFAULT_OPTION = Option(
  SHAPE_CORNER_SIZE_OPTION_ID_DEFAULT,
  0,
  R.string.shape_corner_size_preference_option_default
)
private val optionList = ImmutableList.of(
  DEFAULT_OPTION,
  Option(SHAPE_CORNER_SIZE_OPTION_ID_NONE, 0, R.string.shape_corner_size_preference_option_none),
  Option(
    SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_SMALL,
    0,
    R.string.shape_corner_size_preference_option_extra_small
  ),
  Option(SHAPE_CORNER_SIZE_OPTION_ID_SMALL, 0, R.string.shape_corner_size_preference_option_small),
  Option(
    SHAPE_CORNER_SIZE_OPTION_ID_MEDIUM,
    0,
    R.string.shape_corner_size_preference_option_medium
  ),
  Option(SHAPE_CORNER_SIZE_OPTION_ID_LARGE, 0, R.string.shape_corner_size_preference_option_large),
  Option(
    SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_LARGE,
    0,
    R.string.shape_corner_size_preference_option_extra_large
  ),
  Option(SHAPE_CORNER_SIZE_OPTION_ID_FULL, 0, R.string.shape_corner_size_preference_option_full)
)

private val OPTION_ID_TO_THEME_OVERLAY = SparseIntArray().apply {
  append(SHAPE_CORNER_SIZE_OPTION_ID_NONE, R.style.ThemeOverlay_ShapeCornerSize_None)
  append(
    SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_SMALL,
    R.style.ThemeOverlay_ShapeCornerSize_ExtraSmall
  )
  append(SHAPE_CORNER_SIZE_OPTION_ID_SMALL, R.style.ThemeOverlay_ShapeCornerSize_Small)
  append(SHAPE_CORNER_SIZE_OPTION_ID_MEDIUM, R.style.ThemeOverlay_ShapeCornerSize_Medium)
  append(SHAPE_CORNER_SIZE_OPTION_ID_LARGE, R.style.ThemeOverlay_ShapeCornerSize_Large)
  append(
    SHAPE_CORNER_SIZE_OPTION_ID_EXTRA_LARGE,
    R.style.ThemeOverlay_ShapeCornerSize_ExtraLarge
  )
  append(SHAPE_CORNER_SIZE_OPTION_ID_FULL, R.style.ThemeOverlay_ShapeCornerSize_Full)
  append(SHAPE_CORNER_SIZE_OPTION_ID_DEFAULT, ThemeOverlayUtils.NO_THEME_OVERLAY)

}

/**
 * 形状大小配置项
 */
class ShapeCornerSizePreference :
  CatalogPreference(R.string.shape_corner_size_preference_description) {

  override val isShouldRecreateActivityOnOptionChanged: Boolean = true

  override fun apply(context: Context, selectedOption: Option) {
    ThemeOverlayUtils.setThemeOverlay(
      R.id.theme_feature_corner_size,
      OPTION_ID_TO_THEME_OVERLAY.get(selectedOption.optionId)
    )
  }

  override val options: List<Option> = optionList

  override val defaultOption: Option = DEFAULT_OPTION

}
