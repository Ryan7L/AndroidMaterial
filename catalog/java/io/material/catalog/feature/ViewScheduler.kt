package io.material.catalog.feature

import java.lang.ref.WeakReference
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * 定期触发视图回调的类。
 */
class ViewScheduler {
  private val scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
  private var task: ScheduledFuture<*>? = null
  private var listenerRef: WeakReference<Runnable>? = null

  /**
   * 以固定的pollingIntervalMs周期运行runnable 。可运行对象将保留弱引用，以避免泄漏任何上下文。保留对可运行对象的引用，以免其被回收。
   */
  fun start(runnable: Runnable, pollingIntervalMs: Long) {
    cancel()
    this.listenerRef = WeakReference(runnable)
    task = scheduledExecutorService.scheduleAtFixedRate({
      if (listenerRef == null || listenerRef?.get() == null) {
        cancel()
        return@scheduleAtFixedRate
      }
      listenerRef!!.get()!!.run()
    }, 0, pollingIntervalMs, TimeUnit.MILLISECONDS)
  }

  fun cancel() {
    listenerRef = null
    task?.cancel(true)
  }
  val isRunning: Boolean
    get() = !(task?.isDone ?: true)
}
