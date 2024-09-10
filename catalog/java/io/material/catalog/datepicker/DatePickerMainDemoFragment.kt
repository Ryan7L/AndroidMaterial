package io.material.catalog.datepicker

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.annotation.AttrRes
import androidx.core.util.Pair
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import java.util.Calendar
import java.util.TimeZone

class DatePickerMainDemoFragment : DemoFragment() {

  private lateinit var snackBar: Snackbar
  private var today = 0L
  private var nextMonth = 0L
  private var janThisYear = 0L
  private var decThisYear = 0L
  private var oneYearForward = 0L
  private var todayPair: Pair<Long, Long>? = null
  private var nextMonthPair: Pair<Long, Long>? = null

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.picker_main_demo, container, false)
    snackBar = Snackbar.make(container!!, R.string.cat_picker_no_action, Snackbar.LENGTH_LONG)

    initView(view)
    return view
  }

  private fun initView(view: View) {
    val root = view.findViewById<LinearLayout>(R.id.picker_launcher_buttons_layout)
    val launcherBtn = root.findViewById<MaterialButton>(R.id.cat_picker_launch_button)
    val dialogTheme = resolveOrThrow(requireContext(), R.attr.materialCalendarTheme)
    val fullscreenTheme = resolveOrThrow(requireContext(), R.attr.materialCalendarFullscreenTheme)

    val selectionMode = root.findViewById<RadioGroup>(R.id.cat_picker_date_selector_group)
    val theme = root.findViewById<RadioGroup>(R.id.cat_picker_theme_group)
    val bounds = root.findViewById<RadioGroup>(R.id.cat_picker_bounds_group)
    val validation = root.findViewById<RadioGroup>(R.id.cat_picker_validation_group)
    val title = root.findViewById<RadioGroup>(R.id.cat_picker_title_group)
    val opening = root.findViewById<RadioGroup>(R.id.cat_picker_opening_month_group)
    val selection = root.findViewById<RadioGroup>(R.id.cat_picker_selection_group)
    val inputMode = root.findViewById<RadioGroup>(R.id.cat_picker_input_mode_group)
    val dayViewDecoratorGroup =
      root.findViewById<RadioGroup>(R.id.cat_picker_day_view_decorator_group)
    val positiveButton = root.findViewById<RadioGroup>(R.id.cat_picker_positive_button_group)
    val negativeButton = root.findViewById<RadioGroup>(R.id.cat_picker_negative_button_group)
    launcherBtn.setOnClickListener {
      initSetting()
      val selectionModeChoice = selectionMode.checkedRadioButtonId
      val themeChoice = theme.checkedRadioButtonId
      val boundsChoice = bounds.checkedRadioButtonId
      val validationChoice = validation.checkedRadioButtonId
      val titleChoice = title.checkedRadioButtonId
      val openingChoice = opening.checkedRadioButtonId
      val selectionChoice = selection.checkedRadioButtonId
      val inputModeChoices = inputMode.checkedRadioButtonId
      val dayViewDecoratorChoice = dayViewDecoratorGroup.checkedRadioButtonId
      val positiveButtonChoice = positiveButton.checkedRadioButtonId
      val negativeButtonChoice = negativeButton.checkedRadioButtonId
      val builder = setupDateSelectorBuilder(selectionModeChoice, selectionChoice, inputModeChoices)
      val constraintsBuilder =
        setupConstraintsBuilder(boundsChoice, openingChoice, validationChoice)
      val pikerTheme = when (themeChoice) {
        R.id.cat_picker_theme_dialog -> dialogTheme
        R.id.cat_picker_theme_fullscreen -> fullscreenTheme
        R.id.cat_picker_theme_custom -> R.style.ThemeOverlay_Catalog_MaterialCalendar_Custom
        else -> dialogTheme
      }
      builder.setTheme(pikerTheme)

      if (titleChoice == R.id.cat_picker_title_custom) {
        builder.setTitleText(R.string.cat_picker_title_custom)
      } else if (titleChoice == R.id.cat_picker_title_with_description) {
        builder.setTheme(R.style.ThemeOverlay_Catalog_MaterialCalendar_WithDescription)
        builder.setTitleText(titleWithDescription)
      }

      if (positiveButtonChoice == R.id.cat_picker_positive_button_custom) {
        builder.setPositiveButtonText(R.string.cat_picker_positive_button_text)
        builder.setPositiveButtonContentDescription(R.string.cat_picker_positive_button_content_description)
      }
      if (negativeButtonChoice == R.id.cat_picker_negative_button_custom) {
        builder.setNegativeButtonText(R.string.cat_picker_negative_button_text)
        builder.setNegativeButtonContentDescription(R.string.cat_picker_negative_button_content_description)
      }
      setUpDayViewDecorator(builder, dayViewDecoratorChoice)
      try {
        builder.setCalendarConstraints(constraintsBuilder.build())
        val piker = builder.build()
        addSnackBarListeners(piker)
        piker.show(childFragmentManager, piker.toString())
      } catch (e: Exception) {
        snackBar.setText(e.message.toString())
        snackBar.show()
      }
    }
  }

  private fun initSetting() {
    today = MaterialDatePicker.todayInUtcMilliseconds()
    val calendar = utcCalendar.apply {
      timeInMillis = today
      add(Calendar.MONTH, 1)
    }
    nextMonth = calendar.timeInMillis
    calendar.timeInMillis = today
    calendar.set(Calendar.MONTH, Calendar.JANUARY)
    janThisYear = calendar.timeInMillis

    calendar.timeInMillis = today
    calendar.set(Calendar.MONTH, Calendar.DECEMBER)
    decThisYear = calendar.timeInMillis

    calendar.timeInMillis = today
    calendar.add(Calendar.YEAR, 1)
    oneYearForward = calendar.timeInMillis

    todayPair = Pair(today, today)
    nextMonthPair = Pair(nextMonth, nextMonth)
  }

  private fun setupDateSelectorBuilder(
    selectionModeChoice: Int,
    selectionChoice: Int,
    inputModeChoice: Int
  ): MaterialDatePicker.Builder<*> {
    val inputMode =
      if (inputModeChoice == R.id.cat_picker_input_mode_calendar) MaterialDatePicker.INPUT_MODE_CALENDAR else MaterialDatePicker.INPUT_MODE_TEXT
    if (selectionModeChoice == R.id.cat_picker_date_selector_single) {
      val builder = MaterialDatePicker.Builder.datePicker()
      if (selectionChoice == R.id.cat_picker_selection_today) {
        builder.setSelection(today)
      } else if (selectionChoice == R.id.cat_picker_selection_next_month) {
        builder.setSelection(nextMonth)
      }
      builder.setInputMode(inputMode)
      return builder
    } else {
      val builder = MaterialDatePicker.Builder.dateRangePicker()
      if (selectionChoice == R.id.cat_picker_selection_today) {
        builder.setSelection(todayPair!!)
      } else if (selectionChoice == R.id.cat_picker_selection_next_month) {
        builder.setSelection(nextMonthPair!!)
      }
      builder.setInputMode(inputMode)
      return builder
    }
  }

  private fun setupConstraintsBuilder(
    boundsChoice: Int,
    openingChoice: Int,
    validationChoice: Int
  ): CalendarConstraints.Builder {
    return CalendarConstraints.Builder().apply {
      if (boundsChoice == R.id.cat_picker_bounds_this_year) {
        setStart(janThisYear)
        setEnd(decThisYear)
      } else if (boundsChoice == R.id.cat_picker_bounds_one_year_forward) {
        setStart(today)
        setEnd(oneYearForward)
      }
      if (openingChoice == R.id.cat_picker_opening_month_today) {
        setOpenAt(today)
      } else if (openingChoice == R.id.cat_picker_opening_month_next) {
        setOpenAt(nextMonth)
      }
      if (validationChoice == R.id.cat_picker_validation_today_onward) {
        setValidator(DateValidatorPointForward.now())
      } else if (validationChoice == R.id.cat_picker_validation_weekdays) {
        setValidator(DateValidatorWeekdays())
      } else if (validationChoice == R.id.cat_picker_validation_last_two_weeks) {
        val lowerBoundCalendar = utcCalendar
        lowerBoundCalendar.add(Calendar.DAY_OF_MONTH, -14)
        val lowerBound = lowerBoundCalendar.timeInMillis
        val validators = listOf(
          DateValidatorPointForward.from(lowerBound),
          DateValidatorPointBackward.now()
        )
        setValidator(CompositeDateValidator.allOf(validators))
      } else if (validationChoice == R.id.cat_picker_validation_multiple_range) {
        val validatorsMultiple = mutableListOf<CalendarConstraints.DateValidator>()
        val utc = utcCalendar.apply {
          timeInMillis = today
          set(Calendar.DATE, 10)
        }
        val pointBackward = DateValidatorPointBackward.before(utc.timeInMillis)
        utc.set(Calendar.DATE, 20)
        val validatorsComposite = mutableListOf<CalendarConstraints.DateValidator>()
        val pointForwardComposite = DateValidatorPointForward.from(utc.timeInMillis)
        utc.set(Calendar.DATE, 26)

        val pointBackwardComposite = DateValidatorPointBackward.before(utc.timeInMillis)
        validatorsComposite.add(pointForwardComposite)
        validatorsComposite.add(pointBackwardComposite)
        val compositeDateValidator = CompositeDateValidator.allOf(validatorsComposite)
        validatorsMultiple.add(pointBackward)
        validatorsMultiple.add(compositeDateValidator)
        setValidator(CompositeDateValidator.anyOf(validatorsMultiple))

      }
    }
  }

  private val titleWithDescription: CharSequence
    get() {
      val alarmTimes =
        requireContext().resources.getString(R.string.cat_picker_title_description_colored)
      val titleAndDescription =
        requireContext().resources.getString(R.string.cat_picker_title_description_main) + alarmTimes
      val spannable = SpannableString(titleAndDescription)
      val alarmTimesColor = resolveOrThrow(requireContext(), R.attr.colorPrimary)
      val spanStart = titleAndDescription.indexOf(alarmTimes)
      val spanEnd = spanStart + alarmTimes.length
      spannable.setSpan(
        ForegroundColorSpan(alarmTimesColor),
        spanStart,
        spanEnd,
        SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
      )
      spannable.setSpan(
        StyleSpan(Typeface.BOLD),
        spanStart,
        spanEnd,
        SpannableString.SPAN_EXCLUSIVE_INCLUSIVE
      )
      return spannable
    }

  private fun setUpDayViewDecorator(
    builder: MaterialDatePicker.Builder<*>,
    dayViewDecoratorChoice: Int
  ) {
    if (dayViewDecoratorChoice == R.id.cat_picker_day_view_decorator_dots) {
      builder.setDayViewDecorator(CircleIndicatorDecorator())
    } else if (dayViewDecoratorChoice == R.id.cat_picker_day_view_decorator_highlights) {
      builder.setDayViewDecorator(BackgroundHighlightDecorator())
    }
  }

  private fun addSnackBarListeners(picker: MaterialDatePicker<*>) {
    picker.addOnPositiveButtonClickListener {
      snackBar.setText(picker.headerText)
      snackBar.show()
    }
    picker.addOnCancelListener {
      snackBar.setText(R.string.cat_picker_cancel)
      snackBar.show()
    }
    picker.addOnNegativeButtonClickListener {
      snackBar.setText(R.string.cat_picker_user_clicked_cancel)
      snackBar.show()
    }
  }

  companion object {
    private val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private fun resolveOrThrow(context: Context, @AttrRes attr: Int): Int {
      TypedValue().let {
        return if (context.theme.resolveAttribute(attr, it, true)) {
          it.data
        } else throw IllegalArgumentException(context.resources.getResourceName(attr))

      }
    }
  }
}
