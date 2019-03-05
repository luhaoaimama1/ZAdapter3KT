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


# design thought

![](http://assets.processon.com/chart_image/5b755300e4b08d3622b16d4d.png)

https://www.processon.com/view/link/5b755300e4b025cf49492260

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

2.Multiple Layout involves ViewDelegate,CommonAdapter configuration and ViewStyleOBJ configuration extension classes

```
class LeftDelegates : ViewDelegate<String>() {
    override val layoutId: Int= R.layout.item_left

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: Holder, payloads: List<*>) {
        holder.setText(R.id.tv, item.data!!)
        holder.itemView.post { QuickConfig.e("height" + holder.itemView.height) }
        holder.setText(R.id.tv, item.data!!)
                .setOnClickListener(View.OnClickListener { println("holder click test ") })
    }

}
```

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
    }
}

```

Configure extension classes

```
//The data in the actual RV is DataWarp
class DataWarp<T>(var data: T?, var extraConfig: ViewStyleOBJ = ViewStyleOBJ())
// Configure extension classes
class ViewStyleOBJ {
    var viewStyle: Int = -1 //Layout type
    var isFullspan = false //Whether full rows are generally used for grid layouts and waterfall layouts
    //configure tags, Click or browse when the time can be reported through the tags or so. Or find some of the item of a tag in the list in a split second
    val tags: HashSet<String> by lazy { HashSet<String>() }
    //For storing some obj, you can then use the key to use
    val otherMaps:HashMap<String,Any> by lazy { HashMap<String,Any>() }
    // Whether to absorb the top
    var isSticky: Boolean = false
    // Using and quickly updating data in some part of Rv
    var quickUpdateSection: QuickUpdateSection? = null
    //For subdivision multiplexing
    var section: Section? = null
    // Used to distinguish between the head and the bottom or the content
    var part: Part = Part.CONTENT
    // The root of the item FrameLayout is left and right between the top and the left is the divderRect.
    //And when the data is modified, the divder notification of the previous item is changed.
    var divderRect: Rect? = null
    //Control of the previous item divderRect with a bottom force of 0 is generally used for the current item with no up-and-down intervals
    var isHideBeforeDivder = false
    // Internal attribute. Has been generated. If it has been generated, it will not be generated
    internal var isGenerate = false
```



# Update log

>Since there are more updates per version, update logs are posted on each version from now on.


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