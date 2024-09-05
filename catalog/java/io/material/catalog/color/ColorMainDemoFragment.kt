package io.material.catalog.color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class ColorMainDemoFragment : DemoFragment() {
  private lateinit var colorsLayoutSurfaces: LinearLayout
  private lateinit var colorsLayoutContent: LinearLayout
  private lateinit var colorsLayoutUtility: LinearLayout
  private val colorRolesSurfaces = listOf(
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_background, android.R.attr.colorBackground),
      ColorRoleItem(R.string.cat_color_role_on_background, R.attr.colorOnBackground)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_surface, R.attr.colorSurface),
      ColorRoleItem(R.string.cat_color_role_on_surface, R.attr.colorOnSurface)
    ),

    ColorRow(
      ColorRoleItem(R.string.cat_color_role_surface_variant, R.attr.colorSurfaceVariant),
      ColorRoleItem(R.string.cat_color_role_on_surface_variant, R.attr.colorOnSurfaceVariant)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_inverse_surface, R.attr.colorSurfaceInverse),
      ColorRoleItem(R.string.cat_color_role_inverse_on_surface, R.attr.colorOnSurfaceInverse)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_surface_bright, R.attr.colorSurfaceBright),
      ColorRoleItem(R.string.cat_color_role_surface_dim, R.attr.colorSurfaceDim)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_surface_container_low, R.attr.colorSurfaceContainerLow),
      ColorRoleItem(
        R.string.cat_color_role_surface_container_high,
        R.attr.colorSurfaceContainerHigh
      )
    ),
    ColorRow(
      ColorRoleItem(
        R.string.cat_color_role_surface_container_lowest,
        R.attr.colorSurfaceContainerLowest
      ),
      ColorRoleItem(
        R.string.cat_color_role_surface_container_highest,
        R.attr.colorSurfaceContainerHighest
      )
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_surface_container, R.attr.colorSurfaceContainer),
      null
    )
  )
  private val colorsRolesContent = listOf(
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_primary, R.attr.colorPrimary),
      ColorRoleItem(R.string.cat_color_role_on_primary, R.attr.colorOnPrimary)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_primary_container, R.attr.colorPrimaryContainer),
      ColorRoleItem(R.string.cat_color_role_on_primary_container, R.attr.colorOnPrimaryContainer)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_primary_fixed, R.attr.colorPrimaryFixed),
      ColorRoleItem(R.string.cat_color_role_primary_fixed_dim, R.attr.colorPrimaryFixedDim)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_on_primary_fixed, R.attr.colorOnPrimaryFixed),
      ColorRoleItem(
        R.string.cat_color_role_on_primary_fixed_variant,
        R.attr.colorOnPrimaryFixedVariant
      )
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_inverse_primary, R.attr.colorPrimaryInverse),
      null
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_secondary, R.attr.colorSecondary),
      ColorRoleItem(R.string.cat_color_role_on_secondary, R.attr.colorOnSecondary)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_secondary_container, R.attr.colorSecondaryContainer),
      ColorRoleItem(
        R.string.cat_color_role_on_secondary_container,
        R.attr.colorOnSecondaryContainer
      )
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_secondary_fixed, R.attr.colorSecondaryFixed),
      ColorRoleItem(R.string.cat_color_role_secondary_fixed_dim, R.attr.colorSecondaryFixedDim)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_on_secondary_fixed, R.attr.colorOnSecondaryFixed),

      ColorRoleItem(
        R.string.cat_color_role_on_secondary_fixed_variant,
        R.attr.colorOnSecondaryFixedVariant
      )
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_tertiary, R.attr.colorTertiary),
      ColorRoleItem(R.string.cat_color_role_on_tertiary, R.attr.colorOnTertiary)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_tertiary_container, R.attr.colorTertiaryContainer),
      ColorRoleItem(R.string.cat_color_role_on_tertiary_container, R.attr.colorOnTertiaryContainer)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_tertiary_fixed, R.attr.colorTertiaryFixed),
      ColorRoleItem(R.string.cat_color_role_tertiary_fixed_dim, R.attr.colorTertiaryFixedDim)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_on_tertiary_fixed, R.attr.colorOnTertiaryFixed),
      ColorRoleItem(
        R.string.cat_color_role_on_tertiary_fixed_variant,
        R.attr.colorOnTertiaryFixedVariant
      )
    )
  )
  private val colorRolesUtility = listOf(

    ColorRow(
      ColorRoleItem(R.string.cat_color_role_error, R.attr.colorError),
      ColorRoleItem(R.string.cat_color_role_on_error, R.attr.colorOnError)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_error_container, R.attr.colorErrorContainer),
      ColorRoleItem(R.string.cat_color_role_on_error_container, R.attr.colorOnErrorContainer)
    ),
    ColorRow(
      ColorRoleItem(R.string.cat_color_role_outline, R.attr.colorOutline),
      ColorRoleItem(R.string.cat_color_role_outline_variant, R.attr.colorOutlineVariant)
    )
  )


  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_colors_fragment,container,false)
    colorsLayoutSurfaces = view.findViewById(R.id.cat_colors_surfaces)
    colorsLayoutContent = view.findViewById(R.id.cat_colors_content)
    colorsLayoutUtility = view.findViewById(R.id.cat_colors_utility)
    colorRolesSurfaces.forEach {
      it.addTo(inflater,colorsLayoutSurfaces)
    }
    colorsRolesContent.forEach {
      it.addTo(inflater,colorsLayoutContent)
    }

    colorRolesUtility.forEach {
      it.addTo(inflater,colorsLayoutUtility)
    }
    return view
  }
}
