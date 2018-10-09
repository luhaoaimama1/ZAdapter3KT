package com.zone.adapter3kt.data

import com.zone.adapter3kt.Part
import com.zone.adapter3kt.ViewStyle
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.adapter.BaseAdapter

/**
 *[2018/9/10] by Zone
 */
class DataWarp<T>(var data: T?, var extraConfig: ViewStyleOBJ = ViewStyleOBJ())

enum class HFMode { ADD, ADD_OR_CHANGE; }

//为啥用包装类呢？ 因为empty loading这种都可以用添加一个假数据，而用T的话 没办法通过反射 添加实例
//那为什么用包装类 还用 extraConfigMap 呢 是为了防止一个 数据从新生成配置。 其次未添加的也可以配置数据
//为啥不用占位符处理呢？因为占位符 处理 增加删除动画  太麻烦了  通过NineGrid 的时候我才反应过来。用notifyChanged没问题 但是用动画则。。。

//最终都是通过数据进行控制的！

//Config:特殊不走这个config  其次 config只有三个地方可以改。 1：generate 2.生命周期 getPostion 3.bindData
//zone todo   DataWarp与config的绑定。当empty还有其他数据的时候 需要添加 null数据与viewStyle
open class HFList<T>() {

    val headerViewStyleOrder by lazy { ArrayList<Int>() } // 顺序都是往下来的
    val footerViewStyleOrder by lazy { ArrayList<Int>() }
    var addHeaderEnable = true
    var addFooterEnable = true

    var headerMode = HFMode.ADD_OR_CHANGE
    var footerMode = HFMode.ADD_OR_CHANGE
    val headerDatas by lazy { ArrayList<DataWarp<T>>() }
    val footerDatas by lazy { ArrayList<DataWarp<T>>() }
    val contentDatas by lazy { ArrayList<DataWarp<T>>() }
    val otherDatas by lazy { ArrayList<DataWarp<T>>() }
    val mListCollection = ListCollection<DataWarp<T>>()
    var styleExtra: ViewStyle<T> = ViewStyleDefault()
    var hfDeafultFullSpan = true

    init {
        mListCollection.list.add(headerDatas)
        mListCollection.list.add(contentDatas)
        mListCollection.list.add(footerDatas)
        mListCollection.list.add(otherDatas)
    }

    // ======================================= Config系列=====================================

    protected open fun generateConfig(item: DataWarp<T>): ViewStyleOBJ =
        (styleExtra.generateViewStyleOBJ(item.data!!) ?: ViewStyleOBJ()).setNowValue(item.extraConfig)

    fun getItemViewType(position: Int): Int {
        val item = mListCollection.getItem(position)
        if (item == null) BaseAdapter.VIEW_TYPE_NONE_VALUE
        styleExtra.getItemViewType(position, item!!.extraConfig)
        return item.extraConfig.viewStyle
    }

    open fun findFirstViewStyle(viewStyle: Int): Int {
        var resultIndex = -1
        mListCollection.loop { index, item ->
            if (item.extraConfig.viewStyle == viewStyle) {
                resultIndex = index
                true
            } else false
        }
        return resultIndex
    }

    //并不想让他操作数据添加 只能查找
    fun getDatas(): List<DataWarp<T>> {
        val list = ArrayList<DataWarp<T>>()
        mListCollection.loop { _, item ->
            list.add(item)
            false
        }
        return list
    }

    // ======================================= empty系列=====================================
    protected open fun checkAddEmptyData() {}

    protected open fun checkRemoveEmptyData() {}
    // ======================================= 数据操作 =====================================

    fun add(item: T) {
        //因为内容没加进去 所以需要+1
        val beginIndex = mListCollection.beginIndexFromZero(contentDatas)
        add(beginIndex + contentDatas.size, beginIndex, listOf(item))
    }

    fun add(pos: Int, item: T) {
        add(pos, -1, listOf(item))
    }

    fun add(itemList: List<T>?) {
        //因为内容没加进去 所以需要+1
        val beginIndex = mListCollection.beginIndexFromZero(contentDatas)
        add(beginIndex + contentDatas.size, beginIndex, itemList)
    }

    fun add(pos: Int, moreItems: List<T>?) {
        add(pos, -1, moreItems)
    }

    private fun add(pos: Int, beginIndexTemp: Int, moreItems: List<T>?) {
        val beginIndex = if (beginIndexTemp == -1) mListCollection.beginIndexFromZero(contentDatas) else beginIndexTemp

        val posIsIllegal = (pos == -1 || pos < beginIndex || pos > beginIndex + contentDatas.size)
        if (posIsIllegal || moreItems == null || moreItems.isEmpty()) return
        //如果添加的数据不为空, 考虑空数据状态 移除空数据状态
        checkRemoveEmptyData()

        //先记录content 中的位置 不然头部添加后 index 会偏移
        val contentList = ArrayList<DataWarp<T>>()
        moreItems.forEach { it ->
            val dataWarp = DataWarp(it)
            generateConfig(dataWarp)

            if (headerViewStyleOrder.contains(dataWarp.extraConfig.viewStyle)) {
                dataWarp.extraConfig.part = Part.HEADER
                if (hfDeafultFullSpan) dataWarp.extraConfig.isFullspan = true
                if (!addHeaderEnable) throw  IllegalStateException("从映射 不支持 添加头部")
                addOrderList(dataWarp, headerDatas, headerViewStyleOrder)
            } else if (footerViewStyleOrder.contains(dataWarp.extraConfig.viewStyle)) {
                dataWarp.extraConfig.part = Part.FOOTER
                if (hfDeafultFullSpan) dataWarp.extraConfig.isFullspan = true
                if (!addFooterEnable) throw  IllegalStateException("从映射 不支持 添加底部")
                addOrderList(dataWarp, footerDatas, footerViewStyleOrder)
            } else {
                dataWarp.extraConfig.part = Part.CONTENT
                contentList.add(dataWarp)
            }
        }
        if (contentList.size == 0) return
        //头部和底部都完成了
        contentDatas.addAll(pos - beginIndex, contentList)
        notifyItemRangeInsertedInner(pos, contentList.size)
    }


    private fun addOrderList(item: DataWarp<T>, datas: ArrayList<DataWarp<T>>, viewStyleOrder: ArrayList<Int>) {
        val insertOrderIndex = viewStyleOrder.indexOf(item.extraConfig.viewStyle)
        if (insertOrderIndex == -1) throw  IllegalStateException("viewStyleOrder 顺序中没有此 viewStyle")
        datas.forEachIndexed { index, it ->
            val nowOrderIndex = viewStyleOrder.indexOf(it.extraConfig.viewStyle)
            val mode = if (datas == headerDatas) headerMode else footerMode
            if (mode == HFMode.ADD_OR_CHANGE && insertOrderIndex == nowOrderIndex) {
                datas.set(index, item)
                notifyItemChangedInner(index + mListCollection.beginIndexFromZero(datas))
                return
            }
            if (insertOrderIndex < nowOrderIndex) { //  等于的时候是 Mode为add模式
                datas.add(index, item)
                notifyItemInsertedInner(index + mListCollection.beginIndexFromZero(datas))
                return
            }
        }
        //都当头类型现在是1，2 然后添加2 这时候仅仅有头部 并且是add模式。因为没有找到>2的值所以 需要自己去add
        val index = datas.size //add 相当于set 然后把其他的数据往后移动
        datas.add(index, item)
        notifyItemInsertedInner(index + mListCollection.beginIndexFromZero(datas))
    }


    fun remove(positionStart: Int, itemCount: Int) {
        if (mListCollection.remove(positionStart, itemCount) { _ -> })
            notifyItemRangeRemovedInner(positionStart, itemCount)
        checkAddEmptyData()
    }

    fun changedOBJ(item: T, payload: Any? = null) {
        val posi = indexOfItem(item)
        if (posi != -1) notifyItemRangeChangedInner(posi, 1, payload)
    }

    fun changed(pos: Int, item: T, payload: Any? = null) {
        changedRange(pos, listOf(item), payload)
    }

    fun changedRange(pos: Int, objList: List<T>, payload: Any? = null) {
        val beginIndex = mListCollection.beginIndexFromZero(contentDatas)
        if (pos < beginIndex || pos >= beginIndex + contentDatas.size) return
        objList.forEachIndexed { index, t ->
            val dataWarpNew = DataWarp(t)
            generateConfig(dataWarpNew)
            mListCollection.setItem(pos + index, dataWarpNew) {
                if (it == headerDatas || it == footerDatas)
                    hfFullSpan(dataWarpNew)
            }
        }
        notifyItemRangeChangedInner(pos, objList.size, payload)
    }

    private fun hfFullSpan(dataWarpNew: DataWarp<T>) {
        if (hfDeafultFullSpan) dataWarpNew.extraConfig.isFullspan = true
    }

    fun changedOBJ(itemOld: T, itemNew: T, payload: Any? = null) {
        val dataWarpNew = DataWarp(itemNew)
        generateConfig(dataWarpNew)

        val posi = indexOfItem(itemOld)
        if (posi != -1) {
            mListCollection.setItem(posi, dataWarpNew) { if (it == headerDatas || it == footerDatas) hfFullSpan(dataWarpNew) }
            notifyItemRangeChangedInner(posi, 1, payload)
        }
    }

    fun indexOfItem(item: T): Int {
        var posi = -1
        mListCollection.loop { index, it ->
            if (it.data == item) {
                posi = index
                true
            } else false
        }
        return posi
    }

    //头部尾部都是有顺序的所以不能移动
    fun movedContent(fromPosition: Int, toPosition: Int) {
        val beginIndex = mListCollection.beginIndexWithNoHas(contentDatas)
        val endIndex = beginIndex + contentDatas.count() - 1
        //仅仅支持 content
        if (beginIndex == -1 ||
            fromPosition < beginIndex || fromPosition > endIndex ||
            endIndex < beginIndex || endIndex > endIndex
        ) return

        val tempObj = mListCollection.getItem(fromPosition)
        mListCollection.remove(fromPosition, 1) { _ -> }
        if (tempObj == null) return
        mListCollection.setItem(toPosition, tempObj) { if (it == headerDatas || it == footerDatas) hfFullSpan(tempObj) }
        notifyItemMovedInner(fromPosition, toPosition)
    }

    open fun clearAll() {
        clearHeaderDatas()
        clearContentDatas()
        clearFooterDatas()
    }

    open fun clearHeaderDatas() = clear(headerDatas)
    open fun clearContentDatas() = clear(contentDatas)
    open fun clearFooterDatas() = clear(footerDatas)

    private fun clear(item: ArrayList<DataWarp<T>>) {
        val beginIndex = mListCollection.beginIndexWithNoHas(item)
        val itemCount = item.size
        if (beginIndex == -1 || itemCount == 0) return
        item.clear()
        notifyItemRangeRemovedInner(beginIndex, itemCount)
        checkAddEmptyData()
    }

    // ======================================= notify系列=====================================
    // 调用内部通知  这样覆盖方法即可
    protected open fun notifyItemChangedInner(index: Int) {}

    protected open fun notifyItemRangeChangedInner(positionStart: Int, itemCount: Int, payload: Any?) {}
    protected open fun notifyItemMovedInner(fromPosition: Int, toPosition: Int) {}
    protected open fun notifyItemRangeRemovedInner(positionStart: Int, itemCount: Int) {}
    protected open fun notifyItemInsertedInner(position: Int) {}
    protected open fun notifyItemRangeInsertedInner(positionStart: Int, itemCount: Int) {}
}
