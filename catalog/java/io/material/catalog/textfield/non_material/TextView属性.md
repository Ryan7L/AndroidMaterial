### 👉**前提**

> 文字是人类用符号记录表达信息以传之久远的方式和工具.几千年来我们都在乐此不疲地使用它.于你于我于她,没有高低贵贱之分.

TextView 是 Android 中最简单也是最常见的控件.

### 👉**实践过程**

#### 😜**不常用属性**

* **android:maxHeight**：设置文本的最大高度,当[layout_height="wrap_content"]
  时候有作用,否则被忽略,当内容超过最大高度时，会根据[maxLines]和[ellipsize]确定如何显示.
* **android:maxWidth**：设置文本宽度最大可以达到的像素,当[layout_width="wrap_content"]
  时候有作用,否则会被忽略.会根据[maxLines]和[ellipsize]确定如何显示
* **android:scrollHorizontally**：设置文本是否可以水平滚动(前提是内容超出宽度)
  ,可以和([android:ellipsize="marquee"],[android:
  focusable="true"],[android:focusableInTouchMode="true"] ,[android:marqueeRepeatLimit="marquee_forever"]
  ,[android:singleLine="true"], [android:scrollHorizontally="true"])结合形成跑马灯效果.
* **android:allowUndo**：设置是否支持撤销功能,EditText 同样具有该功能且默认是
  true,在手机上可能用的少,但是手机连上了键盘外设了可以用快捷键[Ctrl+Z]回到上一次.
  * **android:autoSizeMaxTextSize**：用于设置 TextView 自动调整文本大小功能的最大文本大小,默认是
    112sp,达到该值后不会再放大,需要
    [android:autoSizeTextType=uniform].在代码中设置:
    ``TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
    textView,
    12, // autoSizeMinTextSize
    24, // autoSizeMaxTextSize
    2, // autoSizeStepGranularity
    TypedValue.COMPLEX_UNIT_SP
    )``
* **android:autoSizeMinTextSize**：用于设置 TextView 自动调整文本大小功能的最大文本大小,默认是
  12sp,达到该值后不会再缩小,需要
  [android:autoSizeTextType=uniform].
* **android:autoSizePresetSizes**：自己预设放大缩小字体大小数组,首先创建[res/values/arrays.xml]
  ,在里面设置个数组字体大小 10sp/16sp/20sp/30sp/100sp,利用 [android:autoSizePresetSizes] 引用数组的
  name,然后放大缩小的时候就是按照这个梯度,不再按照 [autoSizeStepGranularity] 属性力度,需要 [android:
  autoSizeTextType=uniform].
* **android:autoSizeStepGranularity**：设置放大缩小的步长,假设该值为 2sp,则自动放大是 10sp-12sp-14sp
  这样以 2 自增的,需要 [android:autoSizeTextType=uniform].
* **android:autoSizeTextType**：用于控制 TextView 的自动调整文本大小功能.在代码中设置:
  `TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)`
  * none:禁用自动调整文本
  * uniform: 启用自动调整文本
    注：设置为 uniform后,还需要设置 [autoSizeMinTextSize] 和 [autoSizeMaxTextSize]文本自适应才会生效
* **android:autoText**：自动检查拼写错误.
* **android:breakStrategy**：换行策略
  * "simple"：一行显示不完换下一行,文本默认就是这种形式.
  * "balanced"：平衡方式,尽可能的保证每行宽度相同.
  * "high_quality"：影响性能,会自动添加连词符,适合只读文本.
* **android:bufferType**：用于指定 TextView 使用的缓冲区类型。 它影响 TextView 如何存储和管理文本内容.指定代码中
  getText 获得的文本类型,如果是 editable 则返回的类似于
  StringBuilder,可以利用 append 追加字符,如果是 spannable 则可在给定的字符区域使用样式.
  * NORMAL：这是默认的缓冲区类型。 TextView 使用普通的 CharSequence 缓冲区来存储文本内容。
  * SPANNABLE：TextView 使用 SpannableString 缓冲区来存储文本内容。 这允许您对文本应用样式和格式，例如粗体、
    斜体、 颜色和超链接。
  * EDITABLE：TextView 使用 Editable 缓冲区来存储文本内容。 这允许用户编辑文本内容
* **android:cursorVisible**：设置光标是否可见,主要是 EditText 的闪烁的那个线
* **android:digits**：主要用于 EditText,限定输入字符,如设置 android:digits="公众号：空名先生"
  ,输入的时候只接收这几个显示,其他的都不显示.复杂的限制可以使用 [InputFilter]类 实现
* **android:drawableTint**：设置 drawable 图片的颜色,提供了一种简单的方法来更改 TextView
  的drawable颜色，而无需提供多个drawable资源,和 [android:
  drawableBottom],[android:drawableEnd],[android:drawableRight],[android:drawableLeft],[android:
  drawableStart],这些属性同时使用.
* **android:drawableTintMode**：表示用什么样的形式将 [drawableTint] 和 [android:
  drawableBottom],[android:drawableEnd],[android:drawableRight],[android:drawableLeft],[android:
  drawableStart]这些结合绘制.
  * add：结合色调和 drawable 颜色以及 Alpha 通道,将结果 t 调整到有效的颜色值.
  * multiply：将 drawable 的颜色和 alpha 通道与色调相乘.
  * src_atop：色调在 drawable 上方绘制,但是 drawable 的 alpha 通道掩盖了结果.
  * src_in：色调被 drawable 的 alpha 通道遮盖, drawable 的颜色通道被抛出.
  * src_over：色彩绘制在 drawable 的顶部.
* **android:editable**：TextView 默认是 false,EditText 默认是 true,主要用于 EditText,代表是否能进行文本输入.
* **android:elegantTextHeight**：用于控制某些语言（例如中文、 日文和韩文） 的字体紧凑优化。
  启用此属性可以使文本在垂直方向上更加紧凑， 从而减少行高并提高文本的可读性.
* **android:hyphenationFrequency**：用于控制 TextView 中单词断字的频率。 断字是指在单词之间插入连字符以在行尾断开单词，
  从而使文本更均匀地分布在 TextView 中.
  * "normal"：正常断字频率,适用于聊天消息.
  * "none"：禁用断字（默认值）.
  * "full"：高断字频率.
  * normalFast：类似于 normal，但性能更高.
  * fullFast：类似于 full，但性能更高.
* **android:imeActionId**：主要在 EditText 使用,设置 IME 动作 ID.用于指定 IME（输入法编辑器）操作按钮的
  ID。 IME 操作按钮是出现在软键盘上的一个按钮，例如“ 完成” 、 “ 搜索” 或“ 发送” 。 当用户点击 IME
  操作按钮时，TextView 会触发一个 EditorActionListener.onEditorAction() 事件
* **android:imeActionLabel**：主要在 EditText 使用,用于为 IME（输入法编辑器）操作按钮设置自定义标签。
  IME 操作按钮是出现在软键盘上的一个按钮，例如“ 完成” 、 “ 搜索” 或“ 发送”.通常与 imeOptions 属性一起使用
* **android:imeOptions**：主要在 EditText 使用,附加功能,设置右下角 IME 动作与编辑框相关的动作.用于指定
  TextView 的 IME（输入法编辑器）操作选项。 这些选项会影响软键盘上操作按钮的行为和外观， 例如“ 完成” 、 “
  下一步” 、 “ 发送” 或“ 搜索”.
  * actionNone：没有操作按钮。
  * actionGo：执行“Go”操作，例如在浏览器中打开 URL。
  * actionSearch：执行搜索操作。
  * actionSend：发送消息或提交表单。
  * actionNext：移动到下一个输入字段。
  * actionDone：完成当前输入并关闭软键盘。
  * actionPrevious：移动到上一个输入字段。
  * flagNoExtractUi：不显示提取 UI。
  * flagNoAccessoryAction：不显示附件操作。
  * flagNoEnterAction：回车键不触发操作。
  * flagForceAscii：强制使用 ASCII 码。
  * normal：普通模式，没有特殊操作
* **android:includeFontPadding**：设置 TextView 是否包含额外的顶部和底部填充.
* **android:justificationMode**：对齐模式.
  * inter_word：通过拉伸字间距(空格)来实现对齐.
  * none：不对齐.
* **android:letterSpacing**：用于调整 TextView 中字符之间的间距。
  它允许您增加或减少字符之间的空间，从而微调文本的外观和可读性,可为浮点型.正值会增加字符间距，负值会减少字符间距.以“em”为单位，其中
  1 em 等于当前字体大小
* **android:lineSpacingExtra**：用于在 TextView 的文本行之间添加额外的间距。
  它允许您增加或减少行之间的距离，从而改善文本的可读性和外观,最后一行不算.
* **android:lineSpacingMultiplier**：与 [lineSpacingExtra]类似.[lineSpacingExtra]
  以像素为单位添加额外的行间距，而 [lineSpacingMultiplier] 则按因子缩放行间距
* **android:linksClickable**：默认为 true,如果是 false,autoLink 属性中的链接将没作用.仅在 autoLink
  属性设置为 [web]、[email]、[phone] 或 [all] 时有效.注：为了使链接可点击，您需要同时设置 autoLink 和
  linksClickable 属性
* **android:marqueeRepeatLimit**：用于控制 TextView 中跑马灯动画的重复次数。 跑马灯动画是一种文本滚动效果，当文本内容超出
  TextView 的宽度时，文本会水平滚动.marqueeRepeatLimit 属性接受一个整数或 [marquee_forever(无限重复)]
  作为参数
* **android:minHeight**：设置最小高度.
* **android:minWidth**：设置最小宽度.
* **android:numeric**：主要用于 EditText,默认为 false,设置后会是设置的数字类型.可以组合多个值以允许不同类型的数字
  * decimal：输入为数字,允许小数.
  * integer：数字类型.
  * signed：输入为数字,允许使用符号(正数负数).
* **android:privateImeOptions**：主要用于 EditText,向文本视图附加的输入法提供附加内容类型描述,该说明对输入法的实施是私密的.
* **android:selectAllOnFocus**：如果文本是可选的,用于控制 TextView 在获得焦点时是否自动选择其所有文本内容。
  默认情况下，此属性设置为 false.
* **android:textAppearance**：设置文字外观.
* **android:textSelectHandleLeft**：设置与用于选择文本的左句柄对应的 Drawable.Api 29弃用
* **android:textSelectHandleRight**：设置与用于选择文本的右句柄对应的 Drawable.Api 29弃用
* textSelectHandle:用于自定义 TextView 中用于选择文本的句柄的外观。 当用户选择 TextView
  中的文本时，会出现这些句柄，允许他们调整选择范围

#### 😜**常用属性**

* **android:gravity**：用于控制 TextView 中文本的对齐方式。它影响文本在 TextView 内部 的位置，而不是
  TextView 本身在布局中的位置.可以组合多个值以实现不同的对齐方式.
  * left：左对齐
  * center_horizontal：水平居中
  * right：右对齐
  * top：顶部对齐
  * center_vertical：垂直居中
  * bottom：底部对齐
* **android:inputType**：用于指定 TextView 接受的文本类型，并控制显示的软键盘类型。
  这对于提供更好的用户体验和防止输入无效数据非常有用.类型有：
  * 文本类型
    * text: 默认值，接受任何文本输入，显示标准的字母键盘。
    * textCapCharacters: 将所有输入的字母转换为大写。
    * textCapWords: 将每个单词的首字母大写。
    * textCapSentences: 将每个句子的首字母大写。
    * textAutoCorrect: 启用自动拼写纠正。
    * textMultiLine: 允许输入多行文本。
    * textPassword: 将输入的字符隐藏为星号或圆点，用于密码输入。
    * textVisiblePassword: 与 textPassword 类似，但输入的字符可见，方便用户确认。
    * textEmailAddress: 显示带有 "@" 符号的键盘，并进行简单的电子邮件格式验证。
    * textUri: 显示带有斜杠和常用 web 地址后缀的键盘，用于输入 URI。
    * textShortMessage: 显示适合输入短消息的键盘。
    * textLongMessage: 显示适合输入长消息的键盘。
    * textPersonName: 显示适合输入人名的键盘。
    * textPostalAddress: 显示适合输入邮政地址的键盘。
  * 数字类型
    * number: 只接受数字输入，显示数字键盘。
    * numberSigned: 接受带符号的数字输入 (正数和负数)。
    * numberDecimal: 接受带小数点的数字输入。
  * 其他类型
    * phone: 显示电话拨号键盘。
    * datetime: 显示日期和时间选择器
* **android:lines**：设置文本多少行,当长度超出该值时,超出文本部分不显示,与 [maxLines] 和 [minLines]
  不同，[lines] 属性会强制 TextView 始终显示指定的行数，无论文本内容的长度如何。
* **android:maxLines**：设置文本最大多少行,当文本达到改行数还没显示完,超出部分不显示,和 android:lines
  属性类似.
* **android:maxLength**：主要用于 EditText 控件，用于限制用户可以输入的最大字符数,比如你设置了
  10,而文本有几十个字,最终效果只能显示
  10 个字.
* **android:drawableBottom**：TextVie 底部出现一个图片.
* **android:drawableEnd**：TextView 右侧出现一个图片.
* **android:drawableRight**：TextView 右侧出现一个图片.
* **android:drawableLeft**：TextView 左侧出现一个图片.
* **android:drawableStart**：TextView 左侧出现一个图片.
* **android:drawableTop**：TextView 上部出现一个图片.
* **android:drawablePadding**：是设置 text 与 drawable(图片等) 的间隔,一般都与
  [drawableLeft],[drawableStart] ,[drawableEnd] ,[drawableRight],[drawableTop],[drawableBottom]
  一起使用.
* **android:minLines**：限制文本最低多少行显示.
* **android:singleLine**：布尔类型,表示是否单行显示,已经废弃,建议用 android:lines.
* **android:text**：设置显示的文本.
* **android:textSize**：设置文本大小.
* **android:textStyle**：设置文本样式.
* **android:typeface**：用于设置 TextView 中显示的字体,monospace：等宽字体,sans：无衬线字体,serif：衬线,normal：普通字体.
* **android:fontFamily**：用于设置 TextView 中显示的字体,里面可以有多个字体,可搭配字体权重,引用的是
  xml
  文件,例如[android:fontFamily="@font/myfont"],文件在[res-font 中].
* **android:textFontWeight**：设置使用的字体的权重,权重高使用谁,和 [android:fontFamily] 一起使用.
* **android:hint**：提示文本在文本空时显示,和 EditText 的 hint 是同等之妙.
* **android:textColorHint**：设置 [android:hint] 属性文本的颜色.
* **android:shadowColor**：设置文本阴影的颜色,[shadowColor],[shadowDx],[shadowDy],[shadowRadius]
  同时使用实现立体文字效果,味道极佳.
* **android:shadowDx**：设置所有文本 x
  轴即水平偏移,右为正,左为负,[shadowColor],[shadowDx],[shadowDy],[shadowRadius]
  同时使用实现立体文字效果,味道极佳.
* **android:shadowDy**：设置所有文本 y
  轴即垂直偏移,下为正,上为负,[shadowColor],[shadowDx],[shadowDy],[shadowRadius]
  同时使用实现立体文字效果,味道极佳.
* **android:shadowRadius**：设置所有文本阴影的半径,[shadowColor],[shadowDx],[shadowDy],[shadowRadius]
  同时使用实现立体文字效果,味道极佳.
* **android:textColor**：设置文本的颜色.
* **android:textColorHighlight**：设置点击后文本的背景色,比如 [android:autoLink="web"]
  为超链接的时候,点击后背景色会展示 [android:textColorHighlight]设置的.
* **android:textColorLink**：设置文本是链接类型的颜色,和 [android:autoLink="web"] 联合使用.
* **android:textCursorDrawable**：设置光标颜色,这个应用在 EditText 输入框 View 中,而且是自定义的
  drawable 文件.
* **android:textScaleX**：设置文本的水平缩放程度,大于 1,x 方向拉长,看起来更胖了,小于 1,x 方向缩短,看起来更瘦了.
* **android:autoLink**：自动识别并链接特定类型的文本，例如网址、 电子邮件地址和电话号码.
  * none: 不自动链接任何文本 (默认)。
  * web: 链接网址。
  * email: 链接电子邮件地址。
  * phone: 链接电话号码。
  * map: 链接地图地址 (需要额外的设置)。
  * all: 链接所有支持的类型
* **android:ellipsize**：用于控制 TextView 中文本的截断方式，并在截断的位置显示省略号 (...)。 当文本内容超出
  TextView 的显示范围时，这个属性非常有用(设置省略号),如果 TextView 的 layout_width 和
  layout_height 是指定了像素的固定宽高,该属性会直接生效,如果宽高是 "wrap_content"
  类型的,则需要同时结合[android:maxLines]和[android:maxEms]属性才可生效.
  * none: 不截断文本 (默认)。
  * start: 在文本开头显示省略号。
  * middle: 在文本中间显示省略号。
  * end: 在文本末尾显示省略号。
  * marquee: 以跑马灯的形式显示文本 (需要额外的设置)
* **android:ems**：用于限制字符的数量.
* **android:maxEms**：最大字符数量.
* **android:minEms**：最小字符数量.
* **android:enabled**：是否可用.

#### 😜**初始 TextView**

看到上面那么多属性是不是懵了,不要慌,真实开发中用到的属性其实连五分之一都到不了.
我们先来创建个基本的文本控件

```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".TextActivity">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:text="爱是一道光,绿到你发慌"
        android:textColor="#00ff00"
        android:textSize="20sp" />
</RelativeLayout>

```

结合上面属性列表,运行效果是这样的：
![](https://i-blog.csdnimg.cn/blog_migrate/295584924cda88348b5eefb9f7c8ebaf.png)

那上面代码写的**对吗**？

**对**,一点都没错,否则怎么能看到效果了.

**那还有更好的方式吗？**

**有**,就是将 text 和 textColor 提出来,放到专门的文件里,text 在[res-values-strings.xml 中],textColor
在[res-values-colors.xml]中.

![](https://i-blog.csdnimg.cn/blog_migrate/db5ad971f9fd1dfbead7edd35b0641e7.png)

那么我们这么做的**好处是什么呢**？

你想象下有这么个场景：不同的页面都有相同的文本,在不同的页面布局有对应的 TextView,这就存在多个
text,当有一天需要修改这个文本的时候,你难道每个文本都改一遍(其实完全可以)？但是如果我们把 text
提出到[strings.xml]中,所有页面都能引用,以后遇见修改只需要修改[strings.xml]中的那一个文本就行了.

这就是文本配置文件,同理 color 是在颜色配置文件中[colors.xml].

**解决国际化**需求也只需要再提供一个英文的[string.xml]即可.

#### 😜**文字对齐**

我们给文字设置了字体,颜色和大小后,再给他增加文字内部居中对齐和相对父类的居中对齐.

```
<TextView
      android:layout_width="match_parent"
      android:layout_height="200dp"
      android:layout_centerInParent="true"
      android:gravity="center"
      android:text="爱是一道光,绿到你发慌"
      android:textColor="#00ff00"
      android:textSize="20sp" />

```

![](https://i-blog.csdnimg.cn/blog_migrate/f7f6f13b0015608a0882eca346121f86.png)除此之外,自身内部的对齐方式还有：

* **android:gravity="left"**：文本相对文本控件左对齐.
* **android:gravity="right"**：文本相对文本控件右对齐.
* **android:gravity="center_horizontal"**：文本相对文本控件水平居中.
* **android:gravity="center_vertical"**：文本相对文本控件垂直居中.
* **android:gravity="bottom"**：文本相对文本控件底部对齐.
  注意任何的对齐方式都可以利用[|]来表示[且]
* **android:gravity="right|bottom"**：代表右对齐以及底部对齐,显示在控件右下角

自身相对父布局对齐方式：如果父布局是 LinearLayout,则使用 android:layout_gravity=" ",双引号内对齐方式和上面的
android:gravity 类似,而父 View 如果是 RelativeLayout,对齐方式如下：

* **android:layout_alignParentStart="true"**：该 View 在父内部左侧对齐
* **android:layout_alignParentLeft="true"**：该 View 在父内部左侧对齐
* **android:layout_alignParentTop="true"**：该 View 在父内部顶部对齐
* **android:layout_alignParentEnd="true"**：该 View 在父内部右对齐
* **android:layout_alignParentRight="true"**：该 View 在父内部右侧对齐
* **android:layout_alignParentBottom="true"**：该 View 在父内部底部对齐
* **android:layout_centerInParent="true"**：该 View 在父内部居中
* **android:layout_centerHorizontal="true"**：该 View 在父内部水平居中
* **android:layout_centerVertical="true"**：该 View 在父内部垂直居中

#### 😜**文字自定义粗细**

产品经理过来一看,- **哎,不行啊**.你这有点小啊,话说这玩意能加图片吗？
小空一听,立马加了两个属性 drawableTop(drawableLeft,drawableRight,drawableBottom)在 TextView 的上(
左,右,下方放置一个 drawable(图片等))和 textStyle

```
<!--转载说明-务必注明来源：https://zhima.blog.csdn.net/-->

<TextView
  android:id="@+id/myTest"
  android:layout_width="match_parent"
  android:layout_height="200dp"
  android:layout_centerInParent="true"
  android:layout_gravity="bottom"
  android:drawableTop="@mipmap/ic_launcher"
  android:gravity="center"
  android:text="@string/test"
  android:textStyle="bold"
  android:textColor="@color/green"
  android:textSize="20sp" />

```

![](https://i-blog.csdnimg.cn/blog_migrate/4c39de49648d881be6d2008b4f029e6c.png)

产品一看,你这也不够粗啊,是男人你**再粗一点**.
![](https://i-blog.csdnimg.cn/blog_migrate/d451b1596c74c18aff85672948ffeb05.png)

WC,这能忍？来

```
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        TextView test = findViewById(R.id.myTest);
        //拿到绘制文本的画笔,设置粗细
        test.getPaint().setStrokeWidth(5f);
        test.getPaint().setStyle(Paint.Style.FILL_AND_STROKE);
    }
}

```

![](https://i-blog.csdnimg.cn/blog_migrate/470633f4fdc8ae231eed7d593eabbaba.png)![](https://i-blog.csdnimg.cn/blog_migrate/ca73ab7988b3f2139e926d4ca4619a52.png)

* **android:textStyle="italic"**：文本斜体
* **android:textStyle="bold"**：文本加粗
* **android:textStyle="normal"**：文本正常

#### 😜**文字阴影效果**

过两天产品经理又来闹腾了,文本强大是强大了,但是,小空你能来点立体感吗？小空不搭理他,直接反手就是代码,必须要用该属性秀他一脸.

* **android:shadowColor**：设置阴影颜色
* **android:shadowRadius**：设置阴影模糊程度,必须要有该属性
* **android:shadowD**：设置阴影在水平方向的偏移,向右为正,向左为负
* **android:shadowDy**：设置阴影在竖直方向的偏移,向下为正,向上为负

```
<!--转载说明-务必注明来源：https://zhima.blog.csdn.net/-->
 <TextView
     android:id="@+id/myTest"
     android:layout_width="match_parent"
     android:layout_height="200dp"
     android:layout_centerInParent="true"
     android:layout_gravity="bottom"
     android:gravity="center"
     android:text="@string/test"
     android:textStyle="normal"
     android:shadowColor="#ff0000"
     android:shadowRadius="10"
     android:shadowDx="20"
     android:shadowDy="20"
     android:textColor="@color/green"
     android:textSize="26sp" />

```

![](https://i-blog.csdnimg.cn/blog_migrate/2a7de5a2d104c260d873e1003b3e8e43.png)

#### 😜**链接形文字**

小空大佬啊,客户又有需求了啊,他们不想只是单纯的文本啊,比如看到一个网址还得自己复制后去浏览器打开,或者看到个电话号码还得自己输入号码,能不能直接点击就完成的啊.
![](https://i-blog.csdnimg.cn/blog_migrate/0ffcde3b67f2404d0cd4f2fba15f8b7c.png)

客户要求了,就想办法加呗.

```
<!--转载说明-务必注明来源：https://zhima.blog.csdn.net/-->
 <TextView
     android:id="@+id/myTest"
     android:layout_width="match_parent"
     android:layout_height="200dp"
     android:layout_centerInParent="true"
     android:layout_gravity="bottom"
     android:gravity="center"
     android:text="https://zhima.blog.csdn.net/"
     android:textStyle="normal"
     android:autoLink="web"
     android:textColor="@color/green"
     android:textSize="26sp" />

```

主要是属性 android:autoLink

* **android:autoLink="web"**：匹配网页模式,点击跳转浏览器应用打开网页.
* **android:autoLink="all"**：匹配所有模式(相当于网络 | 电子邮件 | 电话 | 图).
* **android:autoLink="email"**：匹配电子邮件地址,点击后自动跳转邮箱相关的应用.
* **android:autoLink="phone"**：匹配电子邮件地,点击后自动跳转拨号页面.
* **android:autoLink="none"**：默认值,什么也没有.
