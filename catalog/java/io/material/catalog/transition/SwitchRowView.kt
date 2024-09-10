package io.material.catalog.transition

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R

class SwitchRowView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val titleView: TextView
  private val subtitleView: TextView
  private val materialSwitch: MaterialSwitch

  init {
    LayoutInflater.from(context).inflate(R.layout.switch_row_view, this)
    titleView = findViewById(R.id.switch_row_title)
    subtitleView = findViewById(R.id.switch_row_subtitle)
    materialSwitch = findViewById(R.id.switch_row_switch)
  }

  var subtitle: CharSequence? = ""
    set(value) {
      field = value
      updateSubtitle()
    }
  var subtitleOn: CharSequence? = null
    set(value) {
      field = value
      updateSubtitle()
    }
  var subtitleOff: CharSequence? = null
    set(value) {
      field = value
      updateSubtitle()
    }
  var title: String
    set(value) {
      titleView.text = value
    }
    get() {
      return titleView.text.toString()
    }

  init {
    var title: CharSequence?
    context.obtainStyledAttributes(attrs, R.styleable.SwitchRowView).apply {
      title = getString(R.styleable.SwitchRowView_title)
      subtitle = getString(R.styleable.SwitchRowView_subtitle)
      subtitleOn = getString(R.styleable.SwitchRowView_subtitleOn)
      subtitleOff = getString(R.styleable.SwitchRowView_subtitleOff)
      recycle()
    }
    title?.let {
      titleView.text = it
    }
    updateSubtitle()
    materialSwitch.setOnCheckedChangeListener { buttonView, isChecked -> updateSubtitle() }
  }

  private fun updateSubtitle() {
    subtitleView.text = getSubtitleText(materialSwitch.isChecked)
  }

  private fun getSubtitleText(isChecked: Boolean): CharSequence {
    return when (isChecked) {
      true -> subtitleOn ?: subtitle ?: ""
      false -> subtitleOff ?: subtitle ?: ""
    }
  }

}
