# View

## 概念：

代表用户界面组件的基本构建块。视图占据屏幕上的一个矩形区域，负责绘图和事件处理。 View
是widgets的基类，用于创建交互式 UI
组件（按钮、文本字段等）

## 常用属性：

| 属性                                       | 作用                                                                                     | 方法                                              |
|------------------------------------------|----------------------------------------------------------------------------------------|-------------------------------------------------|
| allowClickWhenDisable                    | 是否允许点击禁用视图                                                                             | setAllowClickWhenDisabled                       |
| alpha                                    | 透明度 0(完全透明) -1(完全不透明)                                                                  | getAlpha()/setAlpha                             |
| background                               | 用作背景的可绘制图像                                                                             | getBackground()/setBackground                   |
| backgroundTint                           | 应用于背景的色调                                                                               | getBackgroundTintList()/setBackgroundTintList   |
| backgroundTintMode                       | 用于应用背景色调的混合模式。取值详情查看 android.graphics.BlendMode                                        | getBackgroundTintMode()/setBackgroundTintMode() |
| clickable                                | 是否可以响应单击事件                                                                             | setClickable()/isClickable()                    |
| clipToOutLine                            | 用于决定 View 的内容是否应当被裁剪成 View 的轮廓（Outline）的形状。Outline <br/>是一个用于描述视图的轮廓（例如圆形、矩形等简单形状）的对象。 | getClipToOutline()/setClipToOutline()/          |
| elevation                                | Z轴的高度                                                                                  | getElevation()/setZ()                           |
| fitsSystemWindows                        | 是否根据系统窗口（例如状态栏）调整视图布局。                                                                 |                                                 |
| focusable                                | 是否可以获取焦点                                                                               | getFocusable()/setFocusable()                   |
| id                                       | 标识符                                                                                    | getId()/setId()                                 |
| keepScreenOn                             | 是否该视图的所在窗口常亮                                                                           | getKeepScreenOn()/setKeepScreenOn()             |
| longClickable                            | 是否可以响应长按事件                                                                             | isLongClickable()/setLongClickable()            |
| minHeight/minWidth/maxHeight/maxWidth    | 最小高/宽度，最da高/宽度                                                                         | getMinHeight()/setMinHeight()                   |
| padding(left/right/start/end/top/bottom) | 内边距                                                                                    | setPadding()/getPadding()                       |
| paddingHorizontal/paddingVertical        | 左右/上下的内边距                                                                              |                                                 |
| rotation(rotationX/rotationY)            | 视图的旋转                                                                                  | getRotation()/setRotation()                     |
| scaleX/scaleY                            | 视图的大小比例                                                                                | getScaleX()/setScaleX()                         |
| stateListAnimator                        | 设置基于状态的动画器                                                                             | getStateListAnimator()/setStateListAnimator()   |
| tag                                      | 标签                                                                                     | getTag()/setTag()                               |
| textAlignment                            | 文本对齐方向                                                                                 | getTextAlignment()/setTextAlignment()           |
| textDirection                            | 文本方向                                                                                   | getTextDirection()/setTextDirection()           |
| theme                                    | 主题                                                                                     |                                                 |
| transformPivotX/transformPivotY          | 用于指定视图在进行变换（如旋转、缩放、平移等）时的X/Y方向的轴心点（即 X/Y 轴上的位置）                                        |                                                 |
| translationX/translationY/translationZ   | X/Y/Z 方向上的平移距离                                                                         | getTranslationX()/setTranslationX()             |
| visibility                               | 可见性                                                                                    | getVisibility()/getVisibility()                 |
|                                          |                                                                                        |                                                 |
