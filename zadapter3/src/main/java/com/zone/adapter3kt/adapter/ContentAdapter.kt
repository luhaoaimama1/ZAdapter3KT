package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.*
import com.zone.adapter3kt.ViewStyle
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.data.HFListHistory
import com.zone.adapter3kt.divder.BaseDivder
import com.zone.adapter3kt.holder.Holder

/**
 *[2018] by Zone
 * 空 头 尾部 处理
 *
 * Tips:绝对以数据操控 而不用占位符！因为动画 等好多都不好处理
 */
open class ContentAdapter<T>(context: Context) : DelegatesAdapter<T>(context) {

    var divderManager: BaseDivder<T>? = null
    protected var recyclerView: RecyclerView? = null
    var dataChangeHasAnimator = true

    internal var mHFList = object : HFListHistory<T>() {
        override fun generateConfig(item: DataWarp<T>): ViewStyleOBJ {
            return super.generateConfig(item)
        }

        override fun checkAddEmptyData() {
            super.checkAddEmptyData()
            this@ContentAdapter.checkAddEmptyData()
        }

        override fun checkRemoveEmptyData() {
            super.checkRemoveEmptyData()
            this@ContentAdapter.checkRemoveEmptyData()
        }

        override fun notifyItemChangedInner(index: Int) {
            super.notifyItemChangedInner(index)
            QuickConfig.d("notifyItemChangedInner：index:$index")
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemChanged(index) else notifyDataSetChanged()
        }

        override fun notifyItemRangeChangedInner(positionStart: Int, itemCount: Int, payload: Any?) {
            super.notifyItemRangeChangedInner(positionStart, itemCount, payload)
            QuickConfig.d("notifyItemRangeChangedInner：pos:$positionStart")
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemRangeChanged(positionStart, itemCount, payload) else notifyDataSetChanged()
        }

        override fun notifyItemMovedInner(fromPosition: Int, toPosition: Int) {
            super.notifyItemMovedInner(fromPosition, toPosition)
            QuickConfig.d("notifyItemMovedInner：fromPosition:$fromPosition , toPosition:$toPosition")
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemMoved(fromPosition, toPosition) else notifyDataSetChanged()
        }

        override fun notifyItemRangeRemovedInner(positionStart: Int, itemCount: Int) {
            super.notifyItemRangeRemovedInner(positionStart, itemCount)
            QuickConfig.d("notifyItemRangeRemovedInner：positionStart:$positionStart , itemCount:$itemCount")
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemRangeRemoved(positionStart, itemCount) else notifyDataSetChanged()
        }

        override fun notifyItemInsertedInner(position: Int) {
            super.notifyItemInsertedInner(position)
            QuickConfig.d("notifyItemInsertedInner：position:" + position)
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemInserted(position) else notifyDataSetChanged()
        }

        override fun notifyItemRangeInsertedInner(positionStart: Int, itemCount: Int) {
            super.notifyItemRangeInsertedInner(positionStart, itemCount)
            QuickConfig.d("notifyItemRangeInsertedInner：positionStart:$positionStart , itemCount:$itemCount")
            dataWithConfigChanged()
            if (dataChangeHasAnimator) notifyItemRangeInserted(positionStart, itemCount) else notifyDataSetChanged()
        }
    }

    init {
        mHFList.addFooterEnable = false
        mHFList.addFooterEnable = false
    }

    override fun getItemCount(): Int = mHFList.mListCollection.count()
    override fun getItemViewType(position: Int): Int = mHFList.getItemViewType(position)
    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>?) {
        //测试 sticky  如果bind的时候 发现 外面的posi与内部的pos一样,那么把占位拿过来  从而不用走BindViewHolder了
        QuickConfig.e("onBindViewHolder ->posi${position}")

        val item = mHFList.mListCollection.getItem(position)
        if (item == null) return
        onBindViewHolderWithData(holder, position, item, payloads)
        if (item.data == null) return
        delegatesManager.onBindViewHolder(position, item, holder, payloads)
    }

    protected open fun onBindViewHolderWithData(holder: Holder, position: Int, item: DataWarp<T>, payloads: MutableList<Any>?) {}
    protected open fun checkAddEmptyData() {}
    protected open fun checkRemoveEmptyData() {}
    protected open fun dataWithConfigChanged() {}

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        checkAddEmptyData()
    }

    // =======================================额外的方法=====================================
    fun getItem(position: Int): T? = mHFList.mListCollection.getItem(position)?.data

    fun getRealItem(position: Int): DataWarp<T>? = mHFList.mListCollection.getItem(position)

    fun getDatas(): List<DataWarp<T>> = mHFList.getDatas()
    fun indexOfItem(item: T): Int = mHFList.indexOfItem(item)
    fun setStyleExtra(viewStyle: ViewStyle<T>) {
        mHFList.styleExtra = viewStyle
    }

    fun getStyleExtra() = mHFList.styleExtra
    fun findViewStyle(viewStyle: Int): Int = mHFList.findFirstViewStyle(viewStyle)

    fun hfDeafultFullSpan(hfIsFullSpan: Boolean) {
        mHFList.hfDeafultFullSpan = hfIsFullSpan
    }

    fun enableHistory(enableHistory: Boolean) {
        mHFList.enableHistory = enableHistory
    }
// =======================================数据操作=====================================

    open fun add(item: T) = mHFList.add(item)
    open fun add(itemList: List<T>?) = mHFList.add(itemList)
    open fun add(pos: Int, item: T) = mHFList.add(pos, item)
    open fun add(pos: Int, moreItems: List<T>?) = mHFList.add(pos, moreItems)

    open fun remove(pos: Int) = remove(pos, 1)
    open fun remove(positionStart: Int, itemCount: Int) = mHFList.remove(positionStart, itemCount)

    open fun clearAll() = mHFList.clearAll()
    open fun clearHeaderDatas() = mHFList.clearHeaderDatas()
    open fun clearContentDatas() = mHFList.clearContentDatas()
    open fun clearFooterDatas() = mHFList.clearFooterDatas()

    open fun changed(item: T, payload: Any? = null) = mHFList.changedOBJ(item, payload)
    open fun changed(itemOld: T, itemNew: T, payload: Any? = null) =
        mHFList.changedOBJ(itemOld, itemNew, payload)

    open fun changed(pos: Int, item: T, payload: Any? = null) = mHFList.changed(pos, item, payload)
    open fun changedRange(pos: Int, objList: List<T>, payload: Any? = null) =
        mHFList.changedRange(pos, objList, payload)

    open fun movedContent(fromPosition: Int, toPosition: Int) =
        mHFList.movedContent(fromPosition, toPosition)
}