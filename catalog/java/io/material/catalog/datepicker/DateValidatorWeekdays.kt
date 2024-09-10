package io.material.catalog.datepicker

import android.os.Parcel
import android.os.Parcelable.Creator
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import java.util.Calendar
import java.util.TimeZone

class DateValidatorWeekdays : DateValidator {

  private var utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))


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

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is DateValidatorWeekdays) return false
    return true
  }

  override fun hashCode(): Int {
    val hashedFields = arrayOf<Any>()
    return hashedFields.contentHashCode()
  }

  /** Returns true if the provided `date` is enabled.  */
  override fun isValid(date: Long): Boolean {
    utc.timeInMillis = date
    val dayOfWeek = utc.get(Calendar.DAY_OF_WEEK)
    return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
  }

  companion object {
    @JvmField
    val CREATOR = object : Creator<DateValidatorWeekdays> {
      override fun createFromParcel(source: Parcel?): DateValidatorWeekdays {
        return DateValidatorWeekdays()
      }

      override fun newArray(size: Int): Array<DateValidatorWeekdays?> {
        return arrayOfNulls(size)
      }
    }
  }

}
