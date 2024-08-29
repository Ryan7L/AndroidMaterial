package io.material.catalog.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class DialogMainDemoFragment : DemoFragment() {
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.dialog_main_demo, container, false)
    initDialogList(view)
    return view
  }

  private fun initDialogList(view: View) {
    val dialogListContainer = view.findViewById<ViewGroup>(R.id.dialog_launcher_buttons_layout)
    val choices = arrayOf("Choice1", "Choice2", "Choice3")
    val choicesInitial = booleanArrayOf(false, true, false)
    val multiLineMessage = StringBuilder()
    val line = resources.getString(R.string.line)
    repeat(100) {
      multiLineMessage.append(line).append(it).append("\n")
    }
    val positiveText = resources.getString(R.string.positive)
    val negativeText = resources.getString(R.string.negative)
    val title = resources.getString(R.string.title)
    val message = resources.getString(R.string.message)
    val longMessage = resources.getString(R.string.long_message)

    addDialogLauncher(dialogListContainer, R.string.app_compat_alert_dialog) {
      AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .show()
    }

    addDialogLauncher(dialogListContainer, R.string.message_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.long_message_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setMessage(longMessage)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_message_3_long_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(resources.getString(R.string.long_positive), null)
        .setNegativeButton(resources.getString(R.string.long_negative), null)
        .setNeutralButton(resources.getString(R.string.long_neutral), null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.long_title_message_too_long_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(resources.getString(R.string.long_title))
        .setMessage(message)
        .setPositiveButton(resources.getString(R.string.too_long_positive), null)
        .setNegativeButton(resources.getString(R.string.too_long_negative), null)
        .setNeutralButton(resources.getString(R.string.too_long_neutral), null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.icon_title_message_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .setIcon(R.drawable.ic_dialogs_24px)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.icon_title_message_2_actions_centered) {
      MaterialAlertDialogBuilder(requireContext(), centeredTitleThemeOverlay)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .setIcon(R.drawable.ic_dialogs_24px)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.edit_text) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setView(R.layout.edit_text)
        .setPositiveButton(
          positiveText
        ) { dialog, _ ->
          val input = (dialog as AlertDialog).findViewById<TextView>(android.R.id.text1)
          Toast.makeText(requireContext(), input?.text, Toast.LENGTH_LONG).show()
        }
        .setNegativeButton(negativeText, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_choices_as_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(positiveText, null)
        .setItems(choices, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_checkboxes_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(positiveText) { dialog, which ->
          val checkedItemPositions = (dialog as AlertDialog).listView.checkedItemPositions
          val result = mutableListOf<CharSequence>()
          for (i in choices.indices) {
            if (checkedItemPositions[i]) {
              result.add(choices[i])
            }
          }
          Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_LONG).show()
        }
        .setNegativeButton(negativeText, null)
        .setMultiChoiceItems(choices, choicesInitial, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_radiobuttons_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(positiveText) { dialog, which ->
          val checkedItemPosition = (dialog as AlertDialog).listView.checkedItemPosition
          if (checkedItemPosition != AdapterView.INVALID_POSITION) {
            Toast.makeText(requireContext(), choices[checkedItemPosition], Toast.LENGTH_LONG).show()
          }
        }
        .setNegativeButton(negativeText, null)
        .setSingleChoiceItems(choices, 1, null)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_slider_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .setView(R.layout.seekbar_layout)
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_scrolling_2_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(multiLineMessage.toString())
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .show()
    }

    addDialogLauncher(dialogListContainer, R.string.title_scrolling) {
      MaterialAlertDialogBuilder(requireContext())
        .setMessage(multiLineMessage.toString())
        .show()
    }
    addDialogLauncher(dialogListContainer, R.string.title_2_short_actions) {
      MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setPositiveButton(R.string.short_text_1, null)
        .setNegativeButton(R.string.short_text_2, null)
        .show()
    }
    addDialogLauncher(
      dialogListContainer,
      R.string.icon_title_message_2_actions_fullwidth_buttons
    ) {
      MaterialAlertDialogBuilder(requireContext(), dialogFullWidthButtonsStyle)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .setIcon(R.drawable.ic_dialogs_24px)
        .show()
    }
    addDialogLauncher(
      dialogListContainer,
      R.string.icon_title_message_2_actions_centered_fullwidth_buttons
    ) {
      MaterialAlertDialogBuilder(requireContext(), centeredDialogFullWidthButtonsStyle)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveText, null)
        .setNegativeButton(negativeText, null)
        .setIcon(R.drawable.ic_dialogs_24px)
        .show()
//        .setCanceledOnTouchOutside(false)
    }
  }

  @get:StyleRes
  private val centeredTitleThemeOverlay: Int
    get() = R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered

  @get:StyleRes
  private val dialogFullWidthButtonsStyle: Int
    get() = R.style.ThemeOverlay_Catalog_MaterialAlertDialog_FullWidthButtons

  @get:StyleRes
  private val centeredDialogFullWidthButtonsStyle: Int
    get() = R.style.ThemeOverlay_Catalog_MaterialAlertDialog_Centered_FullWidthButtons

  companion object {
    fun addDialogLauncher(
      viewGroup: ViewGroup,
      @StringRes stringRes: Int,
      listener: View.OnClickListener
    ) {
      val launcherBtn = MaterialButton(viewGroup.context).apply {
        setText(stringRes)
        setOnClickListener(listener)
      }
      viewGroup.addView(launcherBtn)
    }
  }
}
