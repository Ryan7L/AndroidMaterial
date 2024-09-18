package io.material.catalog.windowpreferences

import android.content.Context
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import com.google.android.material.internal.EdgeToEdgeUtils


private const val PREFERENCES_NAME = "window_preferences"
private const val KEY_EDGE_TO_EDGE_ENABLED = "edge_to_edge_enabled"

class WindowPreferencesManager(context: Context) {
  private val sharedPreferences =
    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
  val isEdgeToEdgeEnabled: Boolean
    get() = sharedPreferences.getBoolean(
      KEY_EDGE_TO_EDGE_ENABLED,
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    )

  private val listener = OnApplyWindowInsetsListener { v, insets ->

    /**
     * When [set][ViewCompat.setOnApplyWindowInsetsListener]
     * on a View, this listener method will be called instead of the view's own
     * `onApplyWindowInsets` method.
     *
     * @param v      The view applying window insets
     * @param insets The insets to apply
     * @return The insets supplied, minus any insets that were consumed
     */
    var leftInset = insets.stableInsetLeft
    var rightInset = insets.stableInsetRight
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
      leftInset = insets.getInsets(WindowInsets.Type.systemBars()).left
      rightInset = insets.getInsets(WindowInsets.Type.systemBars()).right
    }

    v.setPadding(leftInset, 0, rightInset, 0)
    insets
  }

  fun toggleEdgeToEdgeEnabled() {
    sharedPreferences
      .edit()
      .putBoolean(KEY_EDGE_TO_EDGE_ENABLED, !isEdgeToEdgeEnabled)
      .commit()
  }

  fun applyEdgeToEdgePreference(window: Window?) {
    window?.let {
      EdgeToEdgeUtils.applyEdgeToEdge(it, isEdgeToEdgeEnabled)
      ViewCompat.setOnApplyWindowInsetsListener(
        it.decorView, if (isEdgeToEdgeEnabled) listener else null
      )
    }
  }
}
