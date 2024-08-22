package io.material.catalog.transition.non_material

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.children
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.Explode
import androidx.transition.Fade
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment
import io.material.catalog.transition.CustomTransition

class BaseTransitionDemoFragment : DemoFragment() {
  private lateinit var sceneContainer: ViewGroup
  private val idToSceneMap = mutableMapOf<Int, Scene>()
  private var isUseCustomTransition = false
  private var transitionManager: TransitionManager? = null
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.base_transition_fragment, container, false)
    initScene(view)
    initClickListener(view)
    return view
  }

  private fun initClickListener(view: View) {
    var transition: Transition? = null

    val transitionGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.transitionGroup).apply {
      addOnButtonCheckedListener { group, checkedId, isChecked ->
        if (isChecked) {
          transition = when (checkedId) {
            R.id.fade -> Fade().apply { duration = 2000 }
            R.id.slide -> Slide().apply { duration = 2000 }
            R.id.explode -> Explode().apply { duration = 2000 }
            R.id.color -> CustomTransition().apply { duration = 2000 }
            R.id.changeBounds -> ChangeBounds().apply { duration = 2000 }
            else -> AutoTransition()
          }
        }
      }
    }
    resetSceneToggleSize(transitionGroup)
    resetSceneToggleSize(view.findViewById(R.id.sceneGroup))
    view.findViewById<MaterialButtonToggleGroup>(R.id.sceneGroup)
      .addOnButtonCheckedListener { _, checkedId, isChecked ->
        if (isChecked) {
          changeScene(idToSceneMap[checkedId], if (isUseCustomTransition) transition else null)
        }
      }
    view.findViewById<MaterialSwitch>(R.id.customTransition)
      .setOnCheckedChangeListener { _, isChecked ->
        isUseCustomTransition = isChecked
        TransitionManager.beginDelayedTransition(view.findViewById(R.id.config_container))
        transitionGroup.visibility = if (isChecked) View.VISIBLE else View.GONE
        transitionGroup.isEnabled = true

      }
    view.findViewById<MaterialSwitch>(R.id.customTransitionManager)
      .setOnCheckedChangeListener { _, isChecked ->
        //在XML中定义的TransitionManager，不仅可以定义切换场景的过渡动画类型，还可以定义跳转场景和目标场景
        //TransitionInflater：用于在代码中解析定义在TransitionManager，也可以解析xml中定义的TransitionSet
        transitionManager = if (isChecked) TransitionInflater.from(view.context)
          .inflateTransitionManager(R.transition.transition_manager, sceneContainer) else null
//        从xml中解析Transition
//        val transition = TransitionInflater.from(view.context).inflateTransition(R.transition.custom_transition)
      }


  }

  private fun resetSceneToggleSize(group: MaterialButtonToggleGroup) {
//    val sceneGroup = view.findViewById<MaterialButtonToggleGroup>(R.id.sceneGroup)
    val displayWidth = resources.displayMetrics.widthPixels
    val marginParent =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics)
    val itemWidth = (displayWidth - 2 * marginParent) / group.childCount
    group.children.forEach {
      it.layoutParams = it.layoutParams.apply {
        width = itemWidth.toInt()
      }
    }
  }

  private fun initScene(view: View) {
    sceneContainer = view.findViewById(R.id.scene_container)
    idToSceneMap.apply {
      set(
        R.id.baseScene,
        Scene.getSceneForLayout(sceneContainer, R.layout.transition_scene_base_layout, view.context)
      )
      set(
        R.id.scene1,
        Scene.getSceneForLayout(sceneContainer, R.layout.transition_scene_1_layout, view.context)
      )
      set(
        R.id.scene2,
        Scene.getSceneForLayout(sceneContainer, R.layout.transition_scene_2_layout, view.context)
      )
      set(
        R.id.scene3,
        Scene.getSceneForLayout(sceneContainer, R.layout.transition_scene_3_layout, view.context)
      )
    }
  }

  private fun changeScene(scene: Scene?, transition: Transition?) {
    scene?.display(transition)
    addView(transition)
  }

  private fun addView(transition: Transition?) {
    if (transition == null) {
      TransitionManager.beginDelayedTransition(sceneContainer)
    } else {
      TransitionManager.beginDelayedTransition(sceneContainer, transition)
    }
    addView(requireView())
  }

  private fun Scene.display(transition: Transition?) {
    transition?.let {
      //go()和transitionTo()：go()是类的静态方法，可以指定过渡动画，transitionTo()是实例方法，不能指定过渡动画,除非调用transitionManager.setTransition()设置过渡动画
      transitionManager?.transitionTo(this) ?: TransitionManager.go(this, it)

    } ?: kotlin.run {
      transitionManager?.transitionTo(this) ?: TransitionManager.go(this)
    }
  }

  private fun addView(view: View) {
    val viewGroup = view.findViewById<RelativeLayout>(R.id.container)
    val newView = View(view.context)
    val size =
      TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()
    val layoutParams = RelativeLayout.LayoutParams(size, size).apply {
      addRule(RelativeLayout.CENTER_IN_PARENT)
    }
    newView.layoutParams = layoutParams
    newView.setBackgroundColor(resources.getColor(R.color.blue_reference))
    viewGroup.addView(newView)
  }

}

/**
 * TransitionManager.beginDelayedTransition 与 TransitionManager.go 区别
 * TransitionManager.beginDelayedTransition：
 *    触发方式: 这个方法不会立即执行动画，而是等待布局发生改变时，自动启动过渡动画。
 *    使用场景: 适用于需要对单个 ViewGroup 中的子视图的变化（如显示、隐藏、添加、移除、调整大小或位置）自动应用过渡动画的场景。通常在你手动更改布局之前调用此方法
 *
 * TransitionManager.go:
 *    触发方式: 立即触发动画，将当前布局切换到另一个 Scene，并在切换过程中应用指定的过渡动画。
 *    使用场景: 适用于需要在预定义的场景（Scene）之间切换的场景。通常用于从一个布局状态切换到另一个完全不同的布局状态，并伴随过渡动画
 */
/**
 * 默认的TransitionManager 内部的采用[androidx.transition.AutoTransition],
 * 可以通过在xml中自定义来实现自定义的TransitionManager，包括Scene 切换 Transition
 * 当采用自定义的manager时，调用transitionTo()方法时，转换效果将会是在xml中定义的Transition
 * go与transitionTo方法是不同的，go可以指定转换效果，transitionTo不能，只能使用默认的效果（xml中定义的效果，或者调用setTransition()方法指定的效果）
 */
/**
 * Scene:场景表示应用场景时视图层次结构中各种属性的值的集合。
 *       可以配置场景以在应用时自动运行过渡，这将为场景变化期间发生的各种属性变化添加动画效果。
 *       简单来说，Scene 代表UI的某个状态，且包含了视图的层次结构以及相关属性，再简单来说 就是一个Layout文件
 * Transition:过渡动画，系统内置了Fade，Slide，Explode，ChangeBounds等过渡动画
 * TransitionManager:负责管理Scene之间的过渡动画，常用方法：go():执行Scene切换，beginDelayedTransition():为布局更改添加过渡动画，无需创建Scene
 *
 * Scene 定义 UI 状态，Transition 定义动画效果，TransitionManager 负责管理 Scene 切换和应用 Transition。 它们共同为 Android 应用提供了一种简单而强大的方式来创建流畅的 UI 过渡动画
 *
 *  beginDelayedTransition() 方法之所以不用创建 Scene 对象，是因为它针对的是单个 ViewGroup 内的布局变化，而不是整个场景的切换。
 *
 * Scene 用于表示不同的 UI 状态，例如登录界面和主界面。 每个 Scene 包含完整的视图层次结构和相关属性。 当你需要在不同的 Scene 之间切换时，就需要创建 Scene 对象。
 * beginDelayedTransition() 则专注于单个 ViewGroup 内的动画效果。 它不需要定义不同的 UI 状态，只需要捕获 ViewGroup 内发生的布局变化，并应用过渡动画。
 * 打个比方：
 * Scene 就像更换舞台上的整个布景，需要提前准备好不同的布景 (Scene)。
 * beginDelayedTransition() 就像在同一个布景中添加一些特效，例如灯光变化或演员移动， 不需要更换整个布景。
 * 因此，当你只需要为单个 ViewGroup 内的布局变化添加动画时，使用 beginDelayedTransition() 更加方便快捷，无需创建 Scene 对象。
 * 总结：
 * 如果你需要在不同的 UI 状态之间切换，例如不同的 Activity 或者 Fragment，那么你需要使用 Scene。
 * 如果你只需要为单个 ViewGroup 内的布局变化添加动画，例如添加、删除或修改 View，那么你可以使用 beginDelayedTransition()
 *
 *
 * TransitionInflater:从xml文件加载TransitionManager和Transition的帮助类
 */

