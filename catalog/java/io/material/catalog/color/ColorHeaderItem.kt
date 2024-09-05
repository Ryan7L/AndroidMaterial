package io.material.catalog.color

import androidx.annotation.ColorRes

class ColorHeaderItem(colors: List<ColorItem>) : ColorAdapterItem {
  companion object {
    @JvmField
    val SYSTEM_PREFIX = "system_"

    @JvmField
    val MATERIAL_CUSTOM_PALETTE_NAME_SEARCH_WORD = "_ref_palette_dynamic_"

    @JvmField
    val MATERIAL_CUSTOM_PALETTE_TITLE_PREFIX = "Material custom "
    private val COLOR_600 = "600"
  }

  @ColorRes
  val backgroundColorRes: Int
  private val description: String

  init {
    var sample = colors[0]
    for (colorItem in colors) {
      if (colorItem.colorResName.contains(COLOR_600)) {
        sample = colorItem
        break
      }
    }
    backgroundColorRes = sample.colorRes
    description = sample.colorResName
  }

  val displayName: String
    get() {
      val splitIndex = description.lastIndexOf("_")
      val name = when {
        description.startsWith(SYSTEM_PREFIX) -> {
          // 将资源名称拆分为颜色名称和值，例如 system_accent1_500 拆分为 system_accent1 和 500
          description.substring(0, splitIndex)
        }

        description.contains(MATERIAL_CUSTOM_PALETTE_NAME_SEARCH_WORD) -> {
          // 获取去除搜索词后的颜色名称和值
          val trimmedResName =
            description.substringAfterLast(MATERIAL_CUSTOM_PALETTE_NAME_SEARCH_WORD)
          // 将资源名称拆分为颜色名称和值，例如 neutral92 拆分为 neutral 和 92
          MATERIAL_CUSTOM_PALETTE_TITLE_PREFIX + trimmedResName.split("(?<=\\D)(?=\\d)".toRegex())[0]
        }

        else -> {
          // 获取去除前缀后的颜色名称和值
          val trimmedResName = description.substring(splitIndex + 1)
          // 将资源名称拆分为颜色名称和值，例如 blue500 拆分为 blue 和 500
          trimmedResName.split("(?<=\\D)(?=\\d)".toRegex())[0]
        }
      }
      return name.replace('_', ' ').replaceFirstChar { it.uppercase() }
    }
}
