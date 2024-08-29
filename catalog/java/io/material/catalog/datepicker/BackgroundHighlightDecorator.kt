package io.material.catalog.datepicker

import android.content.Context
import android.content.res.ColorStateList
import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.android.material.color.MaterialColors
import com.google.android.material.datepicker.DayViewDecorator
import io.material.catalog.R
import java.util.Calendar
import java.util.TimeZone

class BackgroundHighlightDecorator : DayViewDecorator() {

  private val today = utcCalendar

  private val highlightDays = listOf(
    addDays(today, 1),
    addDays(today, 3),
    addDays(today, -2)
  )
  private var backgroundHighlightColor: ColorStateList? = null

  private var textHighlightColor: ColorStateList? = null

  override fun initialize(context: Context) {
    val highlightColor = MaterialColors.getColor(
      context,
      R.attr.colorTertiary,
      BackgroundHighlightDecorator::class.java.simpleName
    )
    backgroundHighlightColor = ColorStateList.valueOf(highlightColor)
    val textColor = MaterialColors.getColor(
      context,
      R.attr.colorOnTertiary,
      BackgroundHighlightDecorator::class.java.simpleName
    )
    textHighlightColor = ColorStateList.valueOf(textColor)
  }

  override fun getBackgroundColor(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    valid: Boolean,
    selected: Boolean
  ): ColorStateList? {
    return if (shouldShowHighlight(
        year,
        month,
        day,
        valid,
        selected
      )
    ) backgroundHighlightColor else null
  }

  override fun getTextColor(
    context: Context,
    year: Int,
    month: Int,
    day: Int,
    valid: Boolean,
    selected: Boolean
  ): ColorStateList? {
    return if (shouldShowHighlight(year, month, day, valid, selected)) textHighlightColor else null
  }

  private fun shouldShowHighlight(
    year: Int,
    month: Int,
    days: Int,
    valid: Boolean,
    selected: Boolean
  ): Boolean {
    return valid && !selected && shouldShowHighlight(year, month, days)

  }

  private fun shouldShowHighlight(year: Int, month: Int, days: Int): Boolean {
    highlightDays.forEach {
      if (it.get(Calendar.YEAR) == year && it.get(Calendar.MONTH) == month && it.get(Calendar.DAY_OF_MONTH) == days) {
        return true
      }
    }
    return false
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
    return if (!valid || selected || !shouldShowHighlight(year, month, day)) {
      originalContentDescription
    } else {
      String.format(
        context.getString(R.string.cat_picker_day_view_decorator_highlights_content_description),
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

    @JvmField
    val CREATOR = object : Creator<BackgroundHighlightDecorator> {
      override fun createFromParcel(source: Parcel?): BackgroundHighlightDecorator {
        return BackgroundHighlightDecorator()
      }

      override fun newArray(size: Int): Array<BackgroundHighlightDecorator?> {
        return arrayOfNulls(size)
      }
    }
  }


}
