package io.material.catalog.application

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.material.catalog.preferences.BaseCatalogPreferences
import javax.inject.Inject


private const val TAG = "CatalogApplication"
private const val COMPONENT_OVERRIDE_KEY = "io.material.catalog.application.componentOverride"

open class CatalogApplication : MultiDexApplication(), HasAndroidInjector {

  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Any>

  @Inject
  lateinit var catalogPreferences: BaseCatalogPreferences

  override fun onCreate() {
    super.onCreate()
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    if (!overrideApplicationComponent(this)){
      DaggerCatalogApplicationComponent.builder().application(this).build().inject(this)
    }
    catalogPreferences.applyPreferences(this)
  }
  /**
   * 使用在 AndroidManifest.xml 元数据中指定的键为[COMPONENT_OVERRIDE_KEY]组件替换应用程序组件。如果组件已正确初始化并替换，则返回true ，否则返回false 。
   * 这假设替换组件可以按照与默认组件完全相同的方式进行初始化。
   * 抑制未经检查的警告，因为我们无法在此方法中为 Class 的实例提供静态类型的类参数。
   */
  private fun overrideApplicationComponent(catalogApplication: CatalogApplication): Boolean {
    try {
      val applicationInfo =
        packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
      val className = applicationInfo.metaData.getString(COMPONENT_OVERRIDE_KEY)
      className ?: let {
        Log.i(
          TAG,
          "overrideApplicationComponent: Component override metadata not found, using default component."
        )
        return false
      }
      Log.i(TAG, "overrideApplicationComponent: $className")
      val builderObject = Class.forName(className).getMethod("builder").invoke(null)
      val builderClass = builderObject::class.java
      builderClass.getMethod("application", Application::class.java)
        .invoke(builderObject, catalogApplication)
      val component = builderClass.getMethod("build").invoke(builderObject)
      component.javaClass.getMethod("inject", CatalogApplication::class.java)
        .invoke(component, catalogApplication)
      return true

    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
  }

  /**
   * 返回一个 [AndroidInjector]。
   */
  override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
