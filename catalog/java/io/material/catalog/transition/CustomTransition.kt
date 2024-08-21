package io.material.catalog.transition

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import androidx.transition.Transition
import androidx.transition.TransitionValues

/**
 * 改变视图背景颜色的过渡动画
 */
class CustomTransition : Transition() {

  private val PROPERTY_BACKGROUND_COLOR = "io.material.catalog:transition:background_color"

  private fun captureValues(transitionValues: TransitionValues) {
    transitionValues.values[PROPERTY_BACKGROUND_COLOR] = transitionValues.view.background
  }

  /**
   *
   * 捕获开始场景中此Transition监视的属性的值。然后，这些值将在稍后调用 [createAnimator] 时作为 startValues 结构传递。
   * 实现的主要关注点是转换关心的属性是什么以及所有这些属性的值是什么。稍后在 [createAnimator] 方法中将比较开始值和结束值，
   * 以确定应运行哪些动画（如果有）。
   *
   * 子类必须实现该方法。该方法只能由转换系统调用；它不适合从外部类调用。
   *
   * @param transitionValues 转换希望存储的任何值的持有者。值存储在此 TransitionValues 对象的“values”字段中，
   * 并通过字符串值进行键入。例如，要存储视图的旋转值，转换可能会调用“transitionValues.values.put("appname:transitionname:rotation",
   * view.getRotation())”。调用此方法时，目标视图已存储在transitionValues结构中。
   */
  override fun captureStartValues(transitionValues: TransitionValues) {
    captureValues(transitionValues)
  }

  /**
   * 捕获最终场景中此过渡监视的属性的值。然后，这些值将在稍后调用 [createAnimator] 时作为 endValues 结构传递。
   * 实现的主要关注点是转换关心的属性是什么以及所有这些属性的值是什么。稍后在 [createAnimator] 方法中将比较开始值和结束值，
   * 以确定应运行哪些动画（如果有）。
   *
   * @param transitionValues 转换希望存储的任何值的持有者。值存储在此 TransitionValues 对象的“values”字段中，并通过字符串值进行键入。
   * 例如，要存储视图的旋转值，转换可能会调用“transitionValues.values.put
   * ("appname:transitionname:rotation", view.getRotation())”。调用此方法时，目标视图已存储在transitionValues结构中。
   */
  override fun captureEndValues(transitionValues: TransitionValues) {
    captureValues(transitionValues)
  }

  /**
   *
   * 为每个适用的目标对象调用该方法，该目标对象存储在 [TransitionValues.view] 字段中。
   * 此方法根据先前捕获的开始和结束场景的 startValues 和 endValues 结构中的信息创建将为此转换运行的动画。Transition 的子类应重写此方法。
   * 此方法只能由转换系统调用；不应从外部类调用。
   * 此方法由转换的父级（一直到层次结构中最顶层的转换）使用 sceneRoot 和开始/ 结束值调用，转换可能需要这些值来设置初始目标值并构建适当的动画。
   * 例如，如果整体转换是一个由多个子转换按顺序组成的TransitionSet ，那么一些子转换可能希望在整体转换开始之前在目标视图上设置初始值，
   * 以使它们处于适当的状态以适应该开始和子转换开始时间之间的延迟。例如，淡入项目的转换可能希望将起始 alpha 值设置为 0，以避免在转换实际开始动画之前闪烁。
   * 这是必要的，因为触发转换的场景变化将自动在所有目标视图上设置结束场景，因此想要从不同值进行动画处理的转换应该在从此方法返回之前设置该值。
   * 此外，过渡可以执行逻辑来确定过渡是否需要在给定的目标和起始/ 结束值上运行。例如，调整屏幕上对象大小的过渡可能希望避免在起始或结束场景中不存在的视图上运行。
   * 如果此方法创建并返回了一个动画器，则过渡机制将向该动画应用任何适用的持续时间、启动延迟和插值器并启动它。
   * 返回值为null表示不应运行任何动画。默认实现返回 null。
   * 每个适用的目标对象都会调用该方法，并将其存储在TransitionValues. view字段中。
   * @param sceneRoot   转换层次结构的根。
   * @param startValues 开始场景中特定目标的值。
   * @param endValues   最终场景中目标的值。
   * @return 在适当的时间启动动画师
   * 此场景变化的过渡。空值意味着不应运行动画。
   */
  //为开始场景和结束场景中的每个目标创建一个动画。对于每对目标，如果它们的背景属性值为颜色（而不是图形），
  // 则创建一个基于 ArgbEvaluator 的 ValueAnimator， 该 ArgbEvaluator 在起始颜色和结束颜色之间进行插值。
  // 还创建一个更新侦听器，为每个动画帧设置视图背景颜色
  override fun createAnimator(
    sceneRoot: ViewGroup,
    startValues: TransitionValues?,
    endValues: TransitionValues?
  ): Animator? {
    //此过渡只能应用于同时位于开始场景和结束场景的视图。
    if (startValues == null || endValues == null) return null
    val view = endValues.view
    val startValue = startValues.values[PROPERTY_BACKGROUND_COLOR]
    val endValue = endValues.values[PROPERTY_BACKGROUND_COLOR]
    if (startValue is ColorDrawable && endValue is ColorDrawable) {
      if (startValue.color != endValue.color) {
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), startValue.color, endValue.color)
        animator.addUpdateListener {
          val value = it.animatedValue as? Int
          value?.let { v ->
            view.setBackgroundColor(v)
          }
        }
        return animator
      }
    }
    return null
  }
}
