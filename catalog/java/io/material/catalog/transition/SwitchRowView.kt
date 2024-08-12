//package io.material.catalog.transition
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.LayoutInflater
//import android.widget.FrameLayout
//import android.widget.TextView
//import com.google.android.material.materialswitch.MaterialSwitch
//import io.material.catalog.R
//
//class SwitchRowView @JvmOverloads constructor(
//  context: Context,
//  attrs: AttributeSet? = null,
//  defStyleAttr: Int = 0
//) :
//  FrameLayout(context, attrs, defStyleAttr) {
//  init {
//    LayoutInflater.from(context).inflate(R.layout.switch_row_view, this)
//  }
//
//  private val titleView: TextView = findViewById(R.id.switch_row_title)
//  private val subtitleView: TextView = findViewById(R.id.switch_row_subtitle)
//  private val materialSwitch: MaterialSwitch = findViewById(R.id.switch_row_switch)
//
//  var subTitle: CharSequence = ""
//  var subTitleOn: CharSequence? = null
//    set(value) {
//      field = value
//      updateSubtitle()
//    }
//  var subTitleOff: CharSequence? = null
//    set(value) {
//      field = value
//      updateSubtitle()
//    }
//
//  init {
//    attrs?.let {
//      context.obtainStyledAttributes(it, R.styleable.SwitchRowView).apply {
//        subTitle = getText(R.styleable.SwitchRowView_subtitle)
//        subTitleOn = getText(R.styleable.SwitchRowView_subtitleOn)
//        subTitleOff = getText(R.styleable.SwitchRowView_subtitleOff)
//        titleView.text = getText(R.styleable.SwitchRowView_title)
//        updateSubtitle()
//        materialSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
//          updateSubtitle()
//        }
//        recycle()
//      }
//    }
//  }
//
//  fun updateSubtitle() {
//    subtitleView.text = getSubtitleText(materialSwitch.isChecked)
//  }
//
//  fun getSubtitleText(isChecked: Boolean): CharSequence = when {
//    isChecked -> subTitleOn ?: subTitle
//    else -> subTitleOff ?: subTitle
//  }
//}
