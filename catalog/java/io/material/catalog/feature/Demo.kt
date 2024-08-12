package io.material.catalog.feature

import android.content.Intent
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import io.material.catalog.R

abstract class Demo(@StringRes val titleResId: Int = R.string.cat_demo_landing_row_demo_header) {


  open val fragment: Fragment? = null
  open val activityIntent: Intent? = null

  val demoClassName: String
    get() {
      fragment?.let {
        return it.javaClass.name
      }
      activityIntent?.let {
        val className = it.component!!.className
        return className.substring(className.lastIndexOf(".") + 1)
      }
      throw IllegalStateException("Demo must implement createFragment or createActivityIntent")
    }

}

