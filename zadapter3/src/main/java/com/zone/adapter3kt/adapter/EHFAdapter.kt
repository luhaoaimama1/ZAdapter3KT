package com.zone.adapter3kt.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.zone.adapter3kt.*
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.data.HFMode
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.holder.Holder

/**
 *[2018] by Zone
 * 空 头 尾部 load 都是 默认占一行的
 */
open class EHFAdapter<T>(context: Context) : ContentAdapter<T>(context) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<RecyclerView.ViewHolder> {
        if (viewType == EMPTY_VALUE || viewType == LOADING_VALUE ||
            mHFList.footerViewStyleOrder.indexOf(viewType) != -1 ||
            mHFList.headerViewStyleOrder.indexOf(viewType) != -1) {
            return Holder(super.onCreateViewHolder(parent, viewType).itemView)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolderWithData(baseHolder: BaseHolder<RecyclerView.ViewHolder>, position: Int, item: DataWarp<T>, payloads: MutableList<Any>) {
        super.onBindViewHolderWithData(baseHolder, position, item, payloads)
        if (recyclerView!!.layoutManager is StaggeredGridLayoutManager) {
            val params = StaggeredGridLayoutManager.LayoutParams(baseHolder.itemView.layoutParams)
            if (params.isFullSpan != item.extraConfig.isFullspan) {
                params.isFullSpan = item.extraConfig.isFullspan
                baseHolder.itemView.layoutParams = params
            }
        }
    }

    override fun checkAddEmptyData() {
        super.checkAddEmptyData()

        if (delegatesManager.getDelegateNoMap(EMPTY_VALUE) != null &&
                mHFList.hcfDataIsEmpty() &&
                !mHFList.otherDatas.contains(emptyData)) {
            QuickConfig.d("add empty data")
            mHFList.otherDatas.add(emptyData)
            dataWithConfigChanged()
            notifyItemInserted(0)
        }
    }

    override fun checkRemoveEmptyData() {
        super.checkRemoveEmptyData()
        if (delegatesManager.getDelegateNoMap(EMPTY_VALUE) != null && mHFList.otherDatas.contains(emptyData)) {
            QuickConfig.d("remove empty data")
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