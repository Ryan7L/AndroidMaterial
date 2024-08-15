package io.material.catalog.animation

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch
import io.material.catalog.R
import io.material.catalog.feature.DemoFragment

class FrameDemoFragment : DemoFragment() {

  private lateinit var animationDrawable: AnimationDrawable
  private lateinit var img: ImageView
  override fun onCreateDemoView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.layout_frame_fragment, container, false)
    img = view.findViewById(R.id.img)
    initFrameAnim(img)
    view.findViewById<MaterialButton>(R.id.startAnim).setOnClickListener {
      animationDrawable.start()
    }
    view.findViewById<MaterialButton>(R.id.stopAnim).setOnClickListener {
      animationDrawable.stop()
    }
    val oneShotBtn = view.findViewById<MaterialSwitch>(R.id.oneShot).let {
      it.setOnCheckedChangeListener { _, isChecked ->
        if (!animationDrawable.isRunning) {
          animationDrawable.isOneShot = isChecked
        }
      }
      it
    }
    view.findViewById<MaterialSwitch>(R.id.useCode)
      .setOnCheckedChangeListener { buttonView, isChecked ->
        if (::animationDrawable.isInitialized && !animationDrawable.isRunning) {
          oneShotBtn.isEnabled = isChecked
          initFrameAnimByCode(img)
        }

      }
    return view
  }

  private fun initFrameAnim(img: ImageView): AnimationDrawable {
    //将动画文件设置为ImageView的背景，然后通过获取ImageView的背景获取 AnimationDrawable对象
    animationDrawable = img.background as AnimationDrawable
    //添加一帧
//    animationDrawable.addFrame()
    //获取某帧
//    animationDrawable.getFrame()
    //是否在运行
//    animationDrawable.isRunning
    //设置获取是否执行一次动画
//    animationDrawable.isOneShot
    return animationDrawable
  }

  private fun initFrameAnimByCode(img: ImageView) {
    val drawableList = intArrayOf(
      R.drawable.flower_1,
      R.drawable.flower_2,
      R.drawable.flower_3,
      R.drawable.flower_4,
      R.drawable.flower_5,
      R.drawable.flower_6,
      R.drawable.flower_7,
      R.drawable.flower_8,
      R.drawable.flower_9,
      R.drawable.flower_10,
      R.drawable.flower_11,
      R.drawable.flower_12,
      R.drawable.flower_13,
      R.drawable.flower_14,
      R.drawable.flower_15,

      )
    animationDrawable = AnimationDrawable()
    drawableList.forEach {
      animationDrawable.addFrame(resources.getDrawable(it, null), 100)
    }
    animationDrawable.isOneShot = true
    img.background = animationDrawable

  }
}


