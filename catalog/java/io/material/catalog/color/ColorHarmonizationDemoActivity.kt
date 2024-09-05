package io.material.catalog.color

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.IdRes
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.HarmonizedColorAttributes
import com.google.android.material.color.HarmonizedColors
import com.google.android.material.color.HarmonizedColorsOptions
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoActivity

class ColorHarmonizationDemoActivity : DemoActivity() {
  companion object {
    private val HARMONIZABLE_BUTTON_DATA_LIST = arrayOf(
      HarmonizableButtonData(
        R.id.red_button_dark,
        R.color.error_reference,
        isLightButton = false
      ),
      HarmonizableButtonData(
        R.id.red_button_light,
        R.color.error_reference,
        isLightButton = true
      ),
      HarmonizableButtonData(
        R.id.yellow_button_dark,
        R.color.yellow_reference,
        isLightButton = false
      ),
      HarmonizableButtonData(
        R.id.yellow_button_light,
        R.color.yellow_reference,
        isLightButton = true
      ),
      HarmonizableButtonData(
        R.id.green_button_dark,
        R.color.green_reference,
        isLightButton = false
      ),
      HarmonizableButtonData(
        R.id.green_button_light,
        R.color.green_reference,
        isLightButton = true
      ),
      HarmonizableButtonData(
        R.id.blue_button_dark,
        R.color.blue_reference,
        isLightButton = false
      ),
      HarmonizableButtonData(
        R.id.blue_button_light,
        R.color.blue_reference,
        isLightButton = true
      )
    )
    private val HARMONIZATION_GRID_ROW_DATA_LIST = arrayOf(
      ColorHarmonizationGridRowData(
        R.id.cat_colors_error,
        R.id.cat_colors_harmonized_error,
        intArrayOf(
          R.attr.colorError,
          R.attr.colorOnError,
          R.attr.colorErrorContainer,
          R.attr.colorOnErrorContainer
        ),
        R.array.cat_error_strings
      ),
      ColorHarmonizationGridRowData(
        R.id.cat_colors_yellow,
        R.id.cat_colors_harmonized_yellow,
        R.color.yellow_reference,
        R.array.cat_yellow_strings
      ),
      ColorHarmonizationGridRowData(
        R.id.cat_colors_green,
        R.id.cat_colors_harmonized_green,
        R.color.green_reference,
        R.array.cat_green_strings
      ),
      ColorHarmonizationGridRowData(
        R.id.cat_colors_blue,
        R.id.cat_colors_harmonized_blue,
        R.color.blue_reference,
        R.array.cat_blue_strings
      )
    )
  }

  private lateinit var dynamicColorsContext: Context
  private lateinit var harmonizedContext: Context
  private lateinit var demoView: View
  private val harmonizableButtonList = mutableListOf<HarmonizableButton>()

  override fun onCreateDemoView(
    layoutInflater: LayoutInflater,
    viewGroup: ViewGroup?,
    bundle: Bundle?
  ): View? {
    demoView = layoutInflater.inflate(R.layout.cat_colors_harmonization_fragment, viewGroup, false)

    dynamicColorsContext = DynamicColors.wrapContextIfAvailable(this)
    val options = HarmonizedColorsOptions.Builder()
      .setColorResourceIds(
        intArrayOf(R.color.yellow_reference, R.color.blue_reference, R.color.green_reference)
      ).setColorAttributes(HarmonizedColorAttributes.createMaterialDefaults()).build()
    harmonizedContext = HarmonizedColors.wrapContextIfAvailable(dynamicColorsContext, options)

    for (colorHarmonizationGridRowData in HARMONIZATION_GRID_ROW_DATA_LIST) {
      createColorGridAndPopulateLayout(
        dynamicColorsContext,
        colorHarmonizationGridRowData,
        colorHarmonizationGridRowData.leftLayoutId
      )
      createColorGridAndPopulateLayout(
        harmonizedContext,
        colorHarmonizationGridRowData,
        colorHarmonizationGridRowData.rightLayoutId
      )
    }
    HARMONIZABLE_BUTTON_DATA_LIST.forEach {
      harmonizableButtonList.add(HarmonizableButton.create(demoView, it))
    }
    updateButtons(false)
    demoView.findViewById<MaterialSwitch>(R.id.cat_color_enabled_switch)
      .setOnCheckedChangeListener { buttonView, isChecked ->
        updateButtons(isChecked)
      }

    return demoView
  }

  private fun createColorGridAndPopulateLayout(
    context: Context,
    colorHarmonizationGridRowData: ColorHarmonizationGridRowData,
    @IdRes layoutId: Int
  ) {
    val grid = createColorGrid(context, colorHarmonizationGridRowData)
    val layout = demoView.findViewById<LinearLayout>(layoutId)
    layout.addView(grid.renderView(layoutInflater, layout))
  }

  private fun createColorGrid(
    context: Context,
    colorHarmonizationGridRowData: ColorHarmonizationGridRowData
  ): ColorGrid {
    return if (colorHarmonizationGridRowData.colorAttributeResIds.isNotEmpty() && colorHarmonizationGridRowData.colorResId == 0) {
      ColorGrid.createFromAttrResId(
        context,
        resources.getStringArray(colorHarmonizationGridRowData.colorNameIds),
        colorHarmonizationGridRowData.colorAttributeResIds
      )
    } else {
      ColorGrid.createFromColorGridData(
        ColorGridData.createFromColorResId(
          context,
          colorHarmonizationGridRowData.colorResId,
          colorHarmonizationGridRowData.colorNameIds
        )
      )
    }
  }

  //这将禁用应用程序范围内的颜色协调，以免与此演示中的协调逻辑发生冲突。
  override val isColorHarmonizationEnabled: Boolean
    get() = false

  private fun updateButtons(harmonize: Boolean) {
    harmonizableButtonList.forEach {
      it.updateColors(harmonize)
    }
  }
}
