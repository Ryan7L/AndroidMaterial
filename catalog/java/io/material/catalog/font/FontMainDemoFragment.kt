package io.material.catalog.font

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.StyleRes
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.resources.TextAppearance
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class FontMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_font_styles_fragment, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val rv = view.findViewById<RecyclerView>(R.id.recycler_view)
    rv.layoutManager = LinearLayoutManager(context)
    rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    rv.adapter = FontStyleAdapter(requireContext())
    ViewCompat.setOnApplyWindowInsetsListener(rv){_, windowInsetsCompat ->
      rv.clipToPadding = windowInsetsCompat.systemWindowInsetBottom == 0
      rv.setPadding(
        rv.paddingLeft,
        rv.paddingTop,
        rv.paddingRight,
        windowInsetsCompat.systemWindowInsetBottom)
      return@setOnApplyWindowInsetsListener windowInsetsCompat
    }
  }

  inner class FontStyleAdapter(context: Context) : RecyclerView.Adapter<FontStyleViewHolder>() {
    private val styles = mutableListOf<Int>()
    private val names = mutableListOf<String>()
    private val attributeNames = mutableListOf<String>()

    init {
      val stylesArray = context.resources.obtainTypedArray(fontStyles)
      val namesArray = context.resources.obtainTypedArray(fontStyleNames)
      val value = TypedValue()
      for (i in 0 until stylesArray.length()) {
        // 1. 从数组中获取属性：?attr/textAppearanceBodyLarge
        stylesArray.getValue(i, value)
        val attribute = value.data
        // 2. 从属性获取样式：@style/TextAppearance.Material3.BodyLarge
        val a = context.obtainStyledAttributes(intArrayOf(attribute))
        val style = a.getResourceId(0, 0)
        a.recycle()
        styles.add(style)
        names.add(namesArray.getString(i) ?: "")
        attributeNames.add(context.resources.getResourceEntryName(attribute))
      }
      stylesArray.recycle()
      namesArray.recycle()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FontStyleViewHolder {
      return FontStyleViewHolder(parent)
    }

    override fun onBindViewHolder(holder: FontStyleViewHolder, position: Int) {
      holder.bind(styles[position], names[position], attributeNames[position])
    }

    override fun getItemCount(): Int {
      return styles.size
    }
  }

  inner class FontStyleViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.cat_font_styles_item, parent, false)
  ) {

    private val nameView = itemView.findViewById<TextView>(R.id.name)
    private val descriptionView = itemView.findViewById<TextView>(R.id.description)
    private var textAppearance: TextAppearance? = null
    private val infoView = itemView.findViewById<ImageView>(R.id.info).also {
      it.setOnClickListener { v ->
        MaterialAlertDialogBuilder(it.context)
          .setTitle(name)
          .setMessage(
            v.context.getString(
              R.string.cat_font_style_dialog_message,
              attributeName,
              getFontFamilyName(textAppearance?.fontFamily),
              getTextStyleName(textAppearance?.textStyle ?: 0),
              px2sp(textAppearance?.textSize ?: 0f),
              getFontVariationSettingsDescription(textAppearance?.fontVariationSettings)
            )
          )
          .show()
      }
    }
    private lateinit var name: String
    private lateinit var attributeName: String

    fun bind(@StyleRes style: Int, name: String, attributeName: String) {
      this.name = name
      this.attributeName = attributeName
      this.textAppearance = TextAppearance(itemView.context, style)
      nameView.text = name
      descriptionView.text = createDescription(name, textAppearance)
      TextViewCompat.setTextAppearance(nameView, style)
    }

    private fun createDescription(name: String, textAppearance: TextAppearance?): String {
      return "$name - " + convertFontFamilyToDescription(
        textAppearance?.fontFamily,
        textAppearance?.textStyle ?: 0
      ) + " " + px2sp(textAppearance?.textSize ?: 0f) + "sp"
    }

    private fun px2sp(px: Float): Int {
      return (px / itemView.resources.displayMetrics.scaledDensity).toInt()
    }
  }

  @ArrayRes
  private val fontStyles = R.array.cat_font_styles_array

  @ArrayRes
  private val fontStyleNames = R.array.cat_font_style_names_array
  private fun getFontFamilyName(fontFamily: String?): String {
    return fontFamily?.let {
      when (it.lowercase()) {
        "sans-serif-light" -> "Light"
        "sans-serif-medium" -> "Medium"
        else -> "Regular"
      }
    } ?: "Regular"
  }

  private fun getTextStyleName(textStyle: Int): String {
    return when (textStyle) {
      Typeface.ITALIC -> "Italic"
      Typeface.BOLD -> "Bold"
      Typeface.BOLD_ITALIC -> "Bold-Italic"
      else -> "Normal"
    }
  }

  private fun convertFontFamilyToDescription(fontFamily: String?, textStyle: Int): String {
    return getFontFamilyName(fontFamily) + "/" + getTextStyleName(textStyle)
  }

  private fun getFontVariationSettingsDescription(fontVariationSettings: String?): String {
    return if (fontVariationSettings == null) {
      "Unsupported"
    } else if (fontVariationSettings.isEmpty()) {
      "None"
    } else {
      fontVariationSettings
    }
  }
}
