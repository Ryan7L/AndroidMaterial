package io.material.catalog.datepicker

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.DayViewDecorator
import io.material.catalog.R
import java.util.Calendar
import java.util.TimeZone

class CircleIndicatorDecorator : DayViewDecorator() {

  private val today = utcCalendar
  private val indicatorDays = listOf(
    addDays(today, 1),
    addDays(today, 3),
    addDays(today, -2)
  )
  private var indicatorDrawables: IndicatorDrawables? = null
  override fun initialize(context: Context) {
    indicatorDrawables = IndicatorDrawables(context)
  }

  private fun selectIndicatorDrawable(
    year: Int,
    month: Int,
    days: Int,
    valid: Boolean,
    selected: Boolean
  ): Drawable? {
    if (!valid || !shouldShowIndicator(year, month, days)) {
      return indicatorDrawables?.indicatorDrawableNone
    }
    if (selected) {
      return indicatorDrawables?.indicatorDrawableSelected
    }
    return indicatorDrawables?.indicatorDrawableDefault
  }

  private fun shouldShowIndicator(year: Int, month: Int, day: Int): Boolean {
    indicatorDays.forEach {
      if (it.get(Calendar.YEAR) == year && it.get(Calendar.MONTH) == month && it.get(Calendar.DAY_OF_MONTH) == day) {
        return true
      }
    }
    return false
  }

  override fun getCompoundDrawableTop(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    valid: Boolean,
    selected: Boolean
  ): Drawable? {
    return indicatorDrawables?.topSpacerDrawable
  }

  override fun getCompoundDrawableBottom(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    valid: Boolean,
    selected: Boolean
  ): Drawable? {
    return selectIndicatorDrawable(year, month, day, valid, selected)
  }

  override fun getContentDescription(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    valid: Boolean,
    selected: Boolean,
    originalContentDescription: CharSequence?
  ): CharSequence? {
    return if (!valid || !shouldShowIndicator(year, month, day)) {
      originalContentDescription
    } else {
      context.getString(
        R.string.cat_picker_day_view_decorator_dots_content_description,
        originalContentDescription
      )
    }
  }

  /**
   * Describe the kinds of special objects contained in this Parcelable
   * instance's marshaled representation. For example, if the object will
   * include a file descriptor in the output of [.writeToParcel],
   * the return value of this method must include the
   * [.CONTENTS_FILE_DESCRIPTOR] bit.
   *
   * @return a bitmask indicating the set of special object types marshaled
   * by this Parcelable object instance.
   */
  override fun describeContents(): Int {
    return 0
  }

  /**
   * Flatten this object in to a Parcel.
   *
   * @param dest The Parcel in which the object should be written.
   * @param flags Additional flags about how the object should be written.
   * May be 0 or [.PARCELABLE_WRITE_RETURN_VALUE].
   */
  override fun writeToParcel(dest: Parcel, flags: Int) {

  }

  companion object {
    val utcCalendar: Calendar
      get() = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    fun addDays(source: Calendar, days: Int): Calendar {
      return utcCalendar.apply {
        time = source.time
        add(Calendar.DATE, days)
      }
    }

    private class IndicatorDrawables(context: Context) {

      private val indicatorRadius =
        context.resources.getDimensionPixelSize(R.dimen.cat_picker_demo_circle_indicator_size)
      private val indicatorMarginBottom =
        context.resources.getDimensionPixelOffset(R.dimen.cat_picker_demo_circle_indicator_margin_bottom)

      val topSpacerDrawable =
        createDrawable(context.resources.getDimensionPixelSize(R.dimen.cat_picker_demo_circle_indicator_top_spacer_size))
      val indicatorDrawableNone = createIndicatorDrawable(Color.TRANSPARENT)
      val indicatorDrawableDefault = createIndicatorDrawable(
        MaterialColors.getColor(
          context,
          R.attr.colorTertiary,
          CircleIndicatorDecorator::class.java.simpleName
        )
      )
      val indicatorDrawableSelected = createIndicatorDrawable(
        MaterialColors.getColor(
          context,
          R.attr.colorOnPrimary,
          CircleIndicatorDecorator::class.java.simpleName
        )
      )


      private fun createDrawable(size: Int): Drawable {
        return ColorDrawable(Color.TRANSPARENT).apply {
          setBounds(0, 0, size, size)
        }
      }

      private fun createIndicatorDrawable(color: Int): Drawable {

        val shape = GradientDrawable().apply {
          shape = GradientDrawable.OVAL
          setColor(color)
          cornerRadius = indicatorRadius.toFloat()
        }
        return InsetDrawable(shape, 0, 0, 0, indicatorMarginBottom).apply {
          setBounds(0, 0, indicatorRadius, indicatorRadius + indicatorMarginBottom)
        }
      }

      @JvmField
      val CREATER = object : Creator<CircleIndicatorDecorator> {
        override fun createFromParcel(source: Parcel?): CircleIndicatorDecorator {
          return CircleIndicatorDecorator()
        }

        override fun newArray(size: Int): Array<CircleIndicatorDecorator?> {
          return arrayOfNulls(size)
        }
      }

    }
  }
}
