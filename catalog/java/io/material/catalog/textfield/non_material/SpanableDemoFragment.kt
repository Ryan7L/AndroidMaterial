package io.material.catalog.textfield.non_material

import android.graphics.BlurMaskFilter
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpanWatcher
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.LocaleSpan
import android.text.style.MaskFilterSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.TextAppearanceSpan
import android.text.style.TypefaceSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.feature.DemoUtils
import java.util.Locale

class SpanableDemoFragment : DemoFragment() {
  private val TAG = "SpanableDemoFragment"
  lateinit var tv: TextView
  private val spanWatcher = object : SpanWatcher {
    override fun onSpanAdded(text: Spannable?, what: Any?, start: Int, end: Int) {
      Log.d(
        TAG,
        "onSpanAdded: test = $text,spanType = ${what?.javaClass?.simpleName},start = $start,end = $end"
      )
    }

    override fun onSpanRemoved(text: Spannable?, what: Any?, start: Int, end: Int) {
      Log.d(
        TAG,
        "onSpanRemoved: test = $text,spanType = ${what?.javaClass?.simpleName},start = $start,end = $end"
      )
    }

    override fun onSpanChanged(
      text: Spannable?,
      what: Any?,
      ostart: Int,
      oend: Int,
      nstart: Int,
      nend: Int
    ) {
      Log.d(
        TAG,
        "onSpanChanged: text = $text,spanType = ${what?.javaClass?.simpleName},ostart = $ostart,oend = $oend,nstart = $nstart,nend = $nend"
      )
    }
  }

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.spanable_demo_layout, container, false)
    tv = view.findViewById(R.id.tv)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    val controllerRoot = view.findViewById<ViewGroup>(R.id.controller_root)
    val inputLayouts = DemoUtils.findViewsWithType(controllerRoot, TextInputLayout::class.java)
    setSpan(inputLayouts)
    view.findViewById<Button>(R.id.update_button).setOnClickListener {
      setSpan(inputLayouts)
    }
  }

  private fun setSpan(inputLayouts: List<TextInputLayout>) {
    val spannableStringBuilder = SpannableStringBuilder()
    spannableStringBuilder.setSpan(
      spanWatcher,
      0,
      spannableStringBuilder.length,
      Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )

    inputLayouts.forEach {
      val str =
        (if (it.editText?.text?.isNotEmpty() == true) it.editText?.text else it.editText?.hint)
          ?: "placeholder"
      when (it.id) {
        R.id.text_input_AbsoluteSizeSpan -> {

          setSpanString(spannableStringBuilder, str, AbsoluteSizeSpan(30, true))
        }

        R.id.text_input_BackgroundColorSpan -> {
          setSpanString(spannableStringBuilder, str, BackgroundColorSpan(Color.RED))
        }

        R.id.text_input_ClickableSpan -> {
          setSpanString(spannableStringBuilder, str, object : ClickableSpan() {
            override fun onClick(widget: View) {
              Snackbar.make(it, "Clicked", Snackbar.LENGTH_SHORT).show()
            }

          })
        }

        R.id.text_input_ImageSpan -> {
          val bitmap = resources.getDrawable(R.drawable.ic_textfield)
          setSpanString(
            spannableStringBuilder,
            str,
            ImageSpan(bitmap, DynamicDrawableSpan.ALIGN_CENTER)
          )
        }

        R.id.text_input_ForegroundColorSpan -> {
          setSpanString(spannableStringBuilder, str, ForegroundColorSpan(Color.GREEN))
        }

        R.id.text_input_LocalSpan -> {
          setSpanString(spannableStringBuilder, str, LocaleSpan(Locale.US))
        }

        R.id.text_input_MaskFilterSpan -> {
          setSpanString(
            spannableStringBuilder,
            str,
            MaskFilterSpan(BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL))
          )
        }

        R.id.text_input_RelativeSizeSpan -> {
          setSpanString(spannableStringBuilder, str, RelativeSizeSpan(2f))
        }

        R.id.text_input_ScaleXSpan -> {
          setSpanString(spannableStringBuilder, str, ScaleXSpan(1.5f))
        }

        R.id.text_input_StrikethroughSpan -> {
          setSpanString(spannableStringBuilder, str, StrikethroughSpan())
        }

        R.id.text_input_StyleSpan -> {
          setSpanString(spannableStringBuilder, str, StyleSpan(Typeface.BOLD_ITALIC))
        }

        R.id.text_input_URLSpan -> {
          setSpanString(spannableStringBuilder, str, URLSpan("www.baidu.com"))
          tv.movementMethod = LinkMovementMethod.getInstance()
          tv.setOnClickListener {
            Snackbar.make(tv, "Clicked URL", Snackbar.LENGTH_SHORT).show()
          }
        }

        R.id.text_input_SubscriptSpan -> {
          setSpanString(spannableStringBuilder, str, SubscriptSpan())
        }

        R.id.text_input_SuperscriptSpan -> {
          setSpanString(spannableStringBuilder, str, SuperscriptSpan())
        }

        R.id.text_input_TextAppearanceSpan -> {
          setSpanString(
            spannableStringBuilder,
            str,
            TextAppearanceSpan(requireContext(), android.R.style.TextAppearance_Material_Large)
          )
        }

        R.id.text_input_UnderlineSpan -> {
          setSpanString(spannableStringBuilder, str, UnderlineSpan())
        }

        R.id.text_input_TypefaceSpan -> {
          setSpanString(spannableStringBuilder, str, TypefaceSpan("serif"))
        }
      }
    }
    tv.text = spannableStringBuilder
  }

  private fun setSpanString(
    spanBuilder: SpannableStringBuilder,
    str: CharSequence,
    span: UpdateAppearance
  ) {
    spanBuilder.append(" ")
    spanBuilder.append(str, span, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE)
  }

}
