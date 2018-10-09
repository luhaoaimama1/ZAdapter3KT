package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.ViewGroup
import com.zone.adapter3kt.*
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.data.HFMode
import com.zone.adapter3kt.holder.Holder

/**
 *[2018] by Zone
 * 空 头 尾部 load 都是 默认占一行的
 */
open class EHFAdapter<T>(context: Context, tag: Any? = null) : ContentAdapter<T>(context, tag) {

    val emptyData by lazy {
        val dataWarpTemp = DataWarp<T>(null)
        dataWarpTemp.extraConfig.viewStyle = EMPTY_VALUE
        dataWarpTemp.extraConfig.part = Part.OTHER
        dataWarpTemp.extraConfig.isFullspan = true
        dataWarpTemp
    }

    init {
        mHFList.addFooterEnable = true
        mHFList.addFooterEnable = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (viewType == EMPTY_VALUE || viewType == LOADING_VALUE ||
            mHFList.footerViewStyleOrder.indexOf(viewType) != -1 ||
            mHFList.headerViewStyleOrder.indexOf(viewType) != -1) {
            return Holder(super.onCreateViewHolder(parent, viewType).view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolderWithData(holder: Holder, position: Int, item: DataWarp<T>, payloads: MutableList<Any>?) {
        super.onBindViewHolderWithData(holder, position, item, payloads)
        if (recyclerView!!.layoutManager is StaggeredGridLayoutManager) {
            val params = StaggeredGridLayoutManager.LayoutParams(holder.itemView.layoutParams)
            if (params.isFullSpan != item.extraConfig.isFullspan) {
                params.isFullSpan = item.extraConfig.isFullspan
                holder.itemView.layoutParams = params
            }
        }
    }

    override fun checkAddEmptyData() {
        super.checkAddEmptyData()
        if (mHFList.mListCollection.count() == 0) {
            mHFList.otherDatas.add(emptyData)
            dataWithConfigChanged()
            notifyItemInserted(0)
        }
    }

    override fun checkRemoveEmptyData() {
        super.checkRemoveEmptyData()
        if (mHFList.mListCollection.count() == 1 &&
            mHFList.otherDatas.indexOf(emptyData) != -1) {
            mHFList.otherDatas.remove(emptyData)
            dataWithConfigChanged()
            notifyItemRemoved(0)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        val manager = recyclerView.getLayoutManager()
        if (manager is GridLayoutManager) {
            val oldSpanSizeLookup = manager.spanSizeLookup;
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = mHFList.mListCollection.getItem(position)
                    if (item == null) return 0
                    return if (item.extraConfig.isFullspan) manager.spanCount
                    else oldSpanSizeLookup.getSpanSize(position)
                }
            }

        }
        recyclerView.layoutManager = manager
    }

    protected open fun isEmptyDelegates(): Boolean {
        return delegatesManager.getDelegateNoMap(EMPTY_VALUE) != null &&
                mHFList.mListCollection.count() == 1 &&
                mHFList.otherDatas.indexOf(emptyData) != -1
    }

    // =======================================定义头部方法=====================================
    open fun defineHeaderOrder(mode: HFMode, vararg viewTypes: Int) {
        viewTypes.forEach { mHFList.headerViewStyleOrder.add(it) }
        mHFList.headerMode = mode
        checkSafe()
    }

    open fun defineFooterOrder(mode: HFMode, vararg viewTypes: Int) {
        mHFList.footerMode = mode
        viewTypes.forEach { mHFList.footerViewStyleOrder.add(it) }
        checkSafe()
    }

    private fun checkSafe() {
        for (item in mHFList.headerViewStyleOrder) {
            if (mHFList.footerViewStyleOrder.contains(item)) {
                throw IllegalStateException("HeaderOrder can't contains FooterOrder's viewtype!")
            }
        }
    }
}