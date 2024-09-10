package io.material.catalog.preferences

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

private const val TAG = "BaseCatalogActivity"

open class BaseCatalogActivity : AppCompatActivity(), HasAndroidInjector {
  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Any>

  override fun onCreate(savedInstanceState: Bundle?) {
    safeInject()
    super.onCreate(savedInstanceState)
  }

  open val isPreferencesEnabled = false

  open val isColorHarmonizationEnabled = true

  private fun safeInject() {
    try {
      AndroidInjection.inject(this)
      Log.i(TAG, "safeInject: yes")
    } catch (e: Exception) {
      Log.i(TAG, "safeInject: no")
      e.printStackTrace()
    }
  }

  /** Returns an [AndroidInjector].  */
  override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
