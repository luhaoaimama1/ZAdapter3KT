# ZAdapter3KT

# 优点

- [x]  实体类 配置额外功能

- [x]  支持断头吸附

- [x]  Holder类的链式调用

- [x]  支持空数据布局 头部 尾部

- [x]  支持加载更多

- [x]  支持 非官方的divder(根据实体类自动更新)

- [x]  支持与ZRefresh联动

- [x]  支持细分复用 ,整体可见与不可以见的监听

- [x] Holder类的 可扩展 参考zone.com.zadapter3kt.adapterimpl文件夹下

# Usage

### JicPack
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency
> implementation 'com.github.luhaoaimama1:ZAdapter3KT:[Latest release](https://github.com/luhaoaimama1/ZAdapter3KT/releases)'
    
# demo解释：

* 简单实用参考FastRecyclerActivity
* 多布局参考  RecyclerKTActivity（这里写的很多 其实没那么麻烦,但是为了验证很多功能所以写的...）
* NineRecyclerKTActivity 类似最多添加9张发布图片
* ViewStyleOBJ扩展类中的divderRect、isHideBeforeDivder 参考 DivderKTActivity
* ViewStyleOBJ扩展类中的section 参考 PartitionActivity
* ViewStyleOBJ扩展类中的isSticky 参考 StickyKTActivity
* 类似最多添加9张发布图片  参考 NineRecyclerKTActivity
* ZRefresh库上拉下拉的联动  参考 ZRefreshKTActivity
    > ps:QuickConfig可以全局更改 加载布局的样式,还有加载更多的方式
* ViewDelegate里使用onclick 参考 OnclickRecyclerActivity
    > ps:这样做的好处可以override 而世界用view.setonclick的方式不推荐因为那种 继承后,在set容易覆盖掉

# 简要概述:

1.简单使用

```
    for (i in 1..30) {
       mDatas.add("" + i)
    }
    //base test
    rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    rv.itemAnimator = DefaultItemAnimator()
    muliAdapter = QuickAdapter<String>(this@FastRecyclerActivity).apply {
       registerDelegate(LeftDelegates())
       add(mDatas)
    }
    rv.adapter = muliAdapter
```

2.多布局  涉及 ViewDelegate,CommonAdapter配置与ViewStyleOBJ配置扩展类

```
class LeftDelegates : ViewDelegate<String>() {
    override val layoutId: Int= R.layout.item_left

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: Holder, payloads: List<*>) {
        holder.setText(R.id.tv, item.data!!)
        holder.itemView.post { QuickConfig.e("height" + holder.itemView.height) }
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        holder.setText(R.id.tv, item.data!!)
                .setOnClickListener(View.OnClickListener { println("holder click测试 ") })
    }

}
```

```
class CommonAdapter(context: Context) : QuickAdapter<String>(context) {
    init {
        enableHistory(true) //添加删除可以看到操作的历史

        registerDelegate(LeftDelegates()) //默认注册的是-1
        registerDelegate(0, RightDelegates()) //也可以注册委托处理

        registerDelegate(1, R.layout.header_simple) //可以直接注册布局资源
        registerDelegate(2, R.layout.header_simple2) //可以直接注册布局资源

        registerDelegate(3, R.layout.footer_simple)
        registerDelegate(4, R.layout.footer_simple)

        // 注册完毕后 定义那些布局类型是 头部和底部
        // HFMode.ADD,HFMode.REPLACE 就是当添加这个布局是 找到后继续添加还是替换
        defineHeaderOrder(HFMode.ADD, 1,2)
        defineFooterOrder(HFMode.ADD, 3,4)

        // 最后注册空数据的 显示
        registerEmpytDelegate(R.layout.empty)

        //item注册点击事件  一般没人用~
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                println("被点击->onItemClick" + position)
            }
        }

        //这里通过获取的数据 修改他的配置扩展类
        setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                var viewStyle = when (item) {
                    "header1" -> 1
                    "header2" -> 2
                    "footer1" -> 3
                    "footer2" -> 4
                    else -> -1
                }
                //通过数据修改 多部剧类型
                return ViewStyleOBJ().viewStyle(viewStyle)
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })
    }
}

```

配置扩展类

```
//实际RV中的数据是DataWarp
class DataWarp<T>(var data: T?, var extraConfig: ViewStyleOBJ = ViewStyleOBJ())
// 配置扩展类
class ViewStyleOBJ {
    var viewStyle: Int = -1 //布局类型
    var isFullspan = false //是不是满行 一般用于grid布局和瀑布流布局
    //配置tags 到时候点击或者浏览的时候可以通过tags是否上报之类的。或者在列表中瞬间找到一中tag的一些item
    val tags: HashSet<String> by lazy { HashSet<String>() }
    // 用于存一些obj的,可以到时候通过key取出来用
    val otherMaps:HashMap<String,Any> by lazy { HashMap<String,Any>() }
    // 是否吸顶
    var isSticky: Boolean = false
    // 用与快速更新Rv某部分的数据
    var quickUpdateSection: QuickUpdateSection? = null
    // 用于细分复用
    var section: Section? = null
    // 用于区分 是头部 还是底部 还是内容的
    var part: Part = Part.CONTENT
    // item 的 根部局FrameLayout 的上下左右间隔  就是divderRect。
    //并且修改数据的时候都会把上一个item的divder通知变更下
    var divderRect: Rect? = null
    //控制前一个item divderRect的bottom强制为0 一般用于当前item上下没有间隔
    var isHideBeforeDivder = false
    // 内部属性。 是否生成过。如果生成过 则不生成了
    internal var isGenerate = false
```



# Update log

>由于每个版本更新的东西较多，所以从现在开始每个版本都会贴上更新日志.


## 1.0.01

  * 实体类 配置额外功能
  * 支持断头吸附
  * Holder类的链式调用
  * 支持空数据布局 头部 尾部
  * 支持加载更多
  * 支持 非官方的divder(根据实体类自动更新)
  * 支持与ZRefresh联动
  * 支持细分复用 ,整体可见与不可以见的监听


# Reference&Thanks：
https://github.com/JoanZapata/base-adapter-helper

https://github.com/cundong/HeaderAndFooterRecyclerView

https://github.com/GcsSloop/diycode