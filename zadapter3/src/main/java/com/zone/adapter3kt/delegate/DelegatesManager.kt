package com.zone.adapter3kt.delegate

import android.util.SparseArray
import android.support.annotation.NonNull
import java.util.*
import android.view.ViewGroup
import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.adapter.DelegatesAdapter
import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.sticky.StickyChildHolder


/**
 *[2018] by Zone
 */
class DelegatesManager(val adapter: DelegatesAdapter<*>) {
    var delegates = SparseArray<ViewDelegate<*>>()
    internal val FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST
    val oriViewTypeMap: HashMap<Int, Int> = HashMap()

    private fun findOriViewType(itemViewType2: Int, viewHolder: Holder? = null): Int {
        var itemViewType = itemViewType2
        if (viewHolder != null && viewHolder is StickyChildHolder) itemViewType = viewHolder.viewTypeInner
        for (entry in oriViewTypeMap.entries) {
            if (entry.value == itemViewType) {
                return entry.key
            }
        }
        return itemViewType
    }

    fun registerDelegate(viewType: Int, delegate: ViewDelegate<*>) {
        delegates.put(viewType, delegate)
        delegate.adapter = adapter
    }

    // =======================================分割线=====================================

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val delegate = getDelegateNoMap(findOriViewType(viewType))
                ?: if (viewType == BaseAdapter.LOADING_VALUE) throw NullPointerException("没有注册 加载更多")
                else throw NullPointerException("No AdapterDelegate added for ViewType " + viewType)
        val onCreateViewHolder = delegate.onCreateViewHolder(parent)
        delegate.setListener(onCreateViewHolder)
        return onCreateViewHolder
    }

    fun onBindViewHolder(position: Int, @NonNull item: Any, viewHolder: Holder, payloads: List<Any>?) {

        val viewType = findOriViewType(viewHolder.itemViewType, viewHolder)
        val delegate = getDelegateNoMap(viewType) ?: throw NullPointerException("No delegate found for  "+viewType)
        delegate.onBindViewHolderInner(position, item, viewHolder, payloads ?: FULLUPDATE_PAYLOADS)
    }

    // =======================================分割线=====================================

    fun onViewRecycled(@NonNull viewHolder: Holder) {
        val viewType = findOriViewType(viewHolder.itemViewType, viewHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewRecycled(viewHolder)
    }

    fun onFailedToRecycleView(@NonNull viewHolder: Holder): Boolean {
        val viewType = findOriViewType(viewHolder.itemViewType, viewHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        return delegate.onFailedToRecycleView(viewHolder)
    }

    fun onViewAttachedToWindow(viewHolder: Holder) {
        val viewType = findOriViewType(viewHolder.itemViewType, viewHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewAttachedToWindow(viewHolder)
    }

    fun onViewDetachedFromWindow(@NonNull viewHolder: Holder) {
        val viewType = findOriViewType(viewHolder.itemViewType, viewHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewDetachedFromWindow(viewHolder)
    }

    fun getDelegateNoMap(viewType: Int): ViewDelegate<*>? {
        return delegates.get(viewType)
    }
}