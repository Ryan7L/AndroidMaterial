package io.material.catalog.bottomappbar

import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import com.google.android.material.shape.ShapePath

class BottomAppBarCutCornersTopEdge(
  private val fabMargin: Float,
  roundedCornerRadius: Float,
  private val cradleVerticalOffset: Float
) : BottomAppBarTopEdgeTreatment(fabMargin, roundedCornerRadius, cradleVerticalOffset) {

  /**
   * 为该边缘处理生成一个ShapePath 。
   * EdgeTreatments 的原点为 (0, 0)，终点为 (0, length)（即它们代表顶部边缘），在应用于其他边缘时，
   * 会根据需要自动旋转和缩放。只需定义水平顶部 EdgeTreatment 即可将其应用于所有四个边缘
   * @param length Float 边的长度
   * @param center Float 到边缘中心的距离
   * @param interpolation Float 形状插值（插值是一种用于在两个已知值之间生成中间值的数学方法）的进度，取值范围为 0 到 1。 用于动画或形状转换
   * @param shapePath ShapePath 用于存储生成的边缘路径的 ShapePath 对象
   *
   * 注：
   * 边缘是水平的还是垂直的: center 参数只在 水平边缘 (例如，顶部和底部边缘) 上有意义。 对于垂直边缘 (例如，左侧和右侧边缘)，center 参数的值始终为 0。
   * 坐标系: getEdgePath() 方法使用 局部坐标系，其中 (0, 0) 表示边缘的左上角，(length, 0) 表示边缘的右上角
   *
   * interpolation 参数的用途
   * 动画： 尽管 getEdgePath() 本身不执行动画，但你可以将 interpolation 值与动画框架 (例如 ValueAnimator) 结合使用，以随着时间的推移改变边缘形状。  你可以在动画的每一帧中更新 interpolation 值，并调用 getEdgePath() 来生成新的边缘路径，从而创建动画效果。
   * 过渡： interpolation 也可以用于在两种不同形状之间创建平滑过渡。  例如，你可以使用 interpolation 值来控制 MaterialButton 从圆角矩形到切角矩形的过渡。
   * 其他动态效果： interpolation 参数可以用于任何需要根据某个值动态调整边缘形状的场景。  例如，你可以根据用户的滚动位置或其他交互来改变 interpolation 值，从而创建有趣的视觉效果。
   */
  override fun getEdgePath(
    length: Float,
    center: Float,
    interpolation: Float,
    shapePath: ShapePath
  ) {
    //fab直径
    val fabDiameter = fabDiameter
    if (fabDiameter == 0f) {
      shapePath.lineTo(length, 0f)
      return
    }
    val diamondSize = fabDiameter / 2f
    val middle = center + horizontalOffset

    val verticalOffsetRatio = cradleVerticalOffset / diamondSize
    if (verticalOffsetRatio >= 1f) {
      shapePath.lineTo(length, 0f)
    }
    shapePath.run {
      lineTo(middle - (fabMargin + diamondSize - cradleVerticalOffset), 0f)
      lineTo(middle, (diamondSize - cradleVerticalOffset + fabMargin) * interpolation)
      lineTo(middle + (fabMargin + diamondSize - cradleVerticalOffset), 0f)
      lineTo(length, 0f)
    }
  }
}
