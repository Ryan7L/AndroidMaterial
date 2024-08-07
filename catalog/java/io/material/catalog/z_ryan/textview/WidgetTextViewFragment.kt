package io.material.catalog.z_ryan.textview

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class WidgetTextViewFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.cat_text_view_widget_text_view_fragment, container, false)
    val content = view.findViewById<ViewGroup>(R.id.content)
    View.inflate(context, R.layout.text_view_content, content)
    initLink(content)
    return view
  }

  private fun initLink(rootView: ViewGroup) {
    rootView.findViewById<TextView>(R.id.link_widget_text_view).apply {
      val string = activity?.resources?.getString(R.string.widget_text_view_resource) ?: "TEXT"
      val spannableString = SpannableString(string)
      spannableString.setSpan(
        URLSpan(activity?.resources?.getString(R.string.widget_text_view_url) ?: "www.baidu.com"),
        0,
        string.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      text = spannableString
      movementMethod = LinkMovementMethod.getInstance()
    }
  }
}
