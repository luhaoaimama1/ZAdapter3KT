# ZAdapter3KT

[中文版文档](README-cn.md)

# Merit

- [x]   Configuring additional functionality for entity classes

- [x]   support of decapitation adsorption

- [x]   Chain call of the Holder class

- [x]   Support for empty data layout header tail

- [x]   Support for loading more

- [x]   Support for unofficial divder (automatically updated by entity class)

- [x]   Support and ZRefresh linkage

- [x]   Support for subdivision reuse, overall visible and invisible listening

- [x] Extensibility of Holder classes,please read zone.com.zadapter3kt.adapterimpl's files

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

> not anroidX version implementation 'com.github.luhaoaimama1:ZAdapter3KT:1.0.03' branch:notAndroidx

Step 3: you need provide

because compileOnly 'androidx.recyclerview:recyclerview:1.0.0'
# design thought

https://www.processon.com/view/link/5b755300e4b025cf49492260
![](https://tva1.sinaimg.cn/large/006tNbRwgy1gakk8cb7w3j31cx0u0h65.jpg)
# demo explain：

* easy use 's ,  Example  -> FastRecyclerActivity
* Multiple Layout , Example  ->  RecyclerKTActivity（Much of the writing here is actually not that troublesome, but to verify a lot of functionality, so write...）
* Similar to add up to 9 published pictures  , Example  -> NineRecyclerKTActivity
* ViewStyleOBJ extend class 's divderRect、isHideBeforeDivder ,  Example -> DivderKTActivity
* ViewStyleOBJ extend class 's section ,  Example -> PartitionActivity
* ViewStyleOBJ extend class 's isSticky ,  Example -> StickyKTActivity
* Pull-down linkage on ZRefresh library ,  Example -> ZRefreshKTActivity
    > ps:QuickConfig can change the style of the load layout globally, and there are more ways to load it
* Using onclick in ViewDelegate ,  Example -> OnclickRecyclerActivity
    > ps:The benefits of doing so can be override and the world doesn't recommend view.setonclick because after that inheritance, it's easy to overwrite in set.

# Easy Summary:

1.Easy use

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

#### Multiple Layout:

1.Declare a layout pattern

```
class LeftOnclickDelegates : ViewDelegatesDemo<String>() {
    override val layoutId: Int = R.layout.item_left_onclick

    override fun registerClickListener(): Array<Int>? = arrayOf(R.id.ll_main, R.id.tv)

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        item.data?.let {
            baseHolder.itemView.post { QuickConfig.e("height" + baseHolder.itemView.height) }
            baseHolder.setText(R.id.tv, it)
        }
    }

    override fun onClick(v: View?, viewBaseHolder: HolderExDemoImpl, posi: Int, item: DataWarp<String>) {
        super.onClick(v, viewBaseHolder, posi, item)
        ToastUtils.showShort(viewBaseHolder.itemView.context, "click 位置：$posi, 点击 ${if (v!!.id == R.id.ll_main) "非文字" else "文字"}")
    }

}
```

2.Configuration in adapter Multi-layout mode And listening

```
class CommonAdapter(context: Context) : QuickAdapter<String>(context) {
    init {
        enableHistory(true) //Add and delete the history of the action that you can see

        registerDelegate(LeftDelegates()) //The default registration is -1
        registerDelegate(0, RightDelegates()) //You may also register for delegate processing

        registerDelegate(1, R.layout.header_simple) //You can register layout resources directly
        registerDelegate(2, R.layout.header_simple2) //You can register layout resources directly

        registerDelegate(3, R.layout.footer_simple)
        registerDelegate(4, R.layout.footer_simple)

        // After registration, define which layout types are the header and bottom
        // HFMode.ADD,HFMode.REPLACE is to continue to add or replace when the layout is found
        defineHeaderOrder(HFMode.ADD, 1,2)
        defineFooterOrder(HFMode.ADD, 3,4)

        // Display of last registered null data
        registerEmpytDelegate(R.layout.empty)

        //Item registration click events are generally not used ~
        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                println("onItemClick" + position)
            }
        }

        //Here he modifies his configuration extension class by getting the data
        setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                var viewStyle = when (item) {
                    "header1" -> 1
                    "header2" -> 2
                    "footer1" -> 3
                    "footer2" -> 4
                    else -> -1
                }
                //Modifying multiple play types through data
                return ViewStyleOBJ().viewStyle(viewStyle)
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })


        //loadmore Function reference class: LoadmoreKTActivity requires QuickConfig to set global loading Settings
        loadOnScrollListener = object : OnScrollRcvListener() {

                override fun onLoading() {
                    super.onLoading()
                    //加载数据
                }
            }
    }
}
```

# Update log

>Since there are more updates per version, update logs are posted on each version from now on.

## 2.0.2
  * androidX

##  1.0.01

  * Configuring additional functionality for entity classes
  * support of decapitation adsorption
  * Chain call of the Holder class
  * Support for empty data layout header tail
  * Support for loading more
  * Support for unofficial divder (automatically updated by entity class)
  * Support and ZRefresh linkage
  * Support for subdivision reuse, overall visible and invisible listening


# Reference&Thanks：
https://github.com/JoanZapata/base-adapter-helper

https://github.com/cundong/HeaderAndFooterRecyclerView

https://github.com/GcsSloop/diycode