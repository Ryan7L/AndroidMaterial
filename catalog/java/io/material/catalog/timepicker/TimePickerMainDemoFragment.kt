package io.material.catalog.timepicker

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TimePickerMainDemoFragment : DemoFragment() {
  private val TAG = "TimePickerMainDemoFragment"
  private var hour: Int = 0
  private var minute: Int = 0
  private val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
  private var clockFormat: Int = 0
  private var timeInputMode: Int? = null
  private lateinit var textView: TextView

  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.time_picker_main_demo, container, false)
    initViews(view)
    return view
  }

  private fun initViews(view: View) {
    textView = view.findViewById(R.id.timepicker_time)
    val timeFormatToggle = view.findViewById<MaterialButtonToggleGroup>(R.id.time_format_toggle)
    timeFormatToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (isChecked) {
        when (checkedId) {
          R.id.time_format_12h -> clockFormat = TimeFormat.CLOCK_12H
          R.id.time_format_24h -> clockFormat = TimeFormat.CLOCK_24H
          R.id.time_format_system -> {
            val isSystem24Hour = DateFormat.is24HourFormat(context)
            clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
          }

          else -> {
            Log.d(TAG, "Invalid time format selection: $checkedId")
          }
        }
      }
    }
    val timeInputModeToggle =
      view.findViewById<MaterialButtonToggleGroup>(R.id.time_input_mode_toggle)
    timeInputModeToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
      if (isChecked) {
        when (checkedId) {
          R.id.time_input_mode_clock -> timeInputMode = MaterialTimePicker.INPUT_MODE_CLOCK
          R.id.time_input_mode_keyboard -> timeInputMode = MaterialTimePicker.INPUT_MODE_KEYBOARD
          R.id.time_input_mode_default -> {
            timeInputMode = null
          }

          else -> {
            Log.d(TAG, "Invalid time input mode selection: $checkedId")
          }
        }
      }
    }
    val frameworkSwitch = view.findViewById<SwitchCompat>(R.id.framework_switch)
    frameworkSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
      for (i in 0 until timeInputModeToggle.childCount) {
        timeInputModeToggle.getChildAt(i)?.isEnabled = !isChecked
      }
    }
    timeFormatToggle.check(R.id.time_format_system)
    timeInputModeToggle.check(R.id.time_input_mode_default)
    frameworkSwitch.isChecked = false
    val button = view.findViewById<Button>(R.id.timepicker_button)
    button.setOnClickListener {
      if (frameworkSwitch.isChecked) {
        showFrameworkTimepicker()
        return@setOnClickListener
      }
      val timePickerBuilder = MaterialTimePicker.Builder()
        .setTimeFormat(clockFormat)
        .setHour(hour)
        .setMinute(minute)
      timeInputMode?.let { mode ->
        timePickerBuilder.setInputMode(mode)
      }
      val timePicker = timePickerBuilder.build()
      timePicker.show(childFragmentManager, "fragment_tag")
      timePicker.addOnPositiveButtonClickListener {
        val newHour = timePicker.hour
        val newMinute = timePicker.minute
        onTimeSet(newHour, newMinute)
      }
    }

  }

  private fun showFrameworkTimepicker() {
    val timePickerDialog = TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
      onTimeSet(hourOfDay, minute)
    }, hour, minute, clockFormat == TimeFormat.CLOCK_24H)
    timePickerDialog.show()
  }

  private fun onTimeSet(newHour: Int, newMinute: Int) {
    val calendar = Calendar.getInstance().apply {
      set(Calendar.HOUR_OF_DAY, newHour)
      set(Calendar.MINUTE, newMinute)
      isLenient = false
    }
    val format = formatter.format(calendar.time)
    textView.text = format
    hour = newHour
    minute = newMinute
  }
}
