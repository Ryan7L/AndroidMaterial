package io.material.catalog.color

import android.content.Context
import androidx.annotation.ColorRes

class ColorItem(context: Context,@ColorRes val colorRes: Int): ColorAdapterItem {

  private val colorSpec: MaterialColorSpec = MaterialColorSpec.createFromResource(context,colorRes)

  val colorResName: String
    get() = colorSpec.description
  val colorValue: Int
    get() = colorSpec.colorValue

}
