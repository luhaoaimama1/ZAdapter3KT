package com.zone.adapter3kt.delegate

import android.util.SparseArray
import android.support.annotation.NonNull
import java.util.*
import android.view.ViewGroup
import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.adapter.DelegatesAdapter
import com.zone.adapter3kt.Holder


/**
 *[2018] by Zone
 */
class DelegatesManager(val adapter: DelegatesAdapter<*>) {
    var delegates = SparseArray<ViewDelegate<*>>()
    private val FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST

    fun registerDelegate(viewType: Int, delegate: ViewDelegate<*>) {
        delegates.put(viewType, delegate)
        delegate.adapter = adapter
    }

    // =======================================分割线=====================================

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val delegate = getDelegate(viewType) ?:
                if (viewType == BaseAdapter.LOADING_VALUE) throw NullPointerException("没有注册 加载更多")
                else throw NullPointerException("No AdapterDelegate added for ViewType " + viewType)

        val onCreateViewHolder = delegate.onCreateViewHolder(parent)
        delegate.setListener(onCreateViewHolder)
        return onCreateViewHolder
    }

    fun onBindViewHolder(position: Int, @NonNull item:Any, viewHolder: Holder, payloads: List<Any>?) {
        val delegate = getDelegate(viewHolder.itemViewType) ?: throw NullPointerException("No delegate found for item ")
        delegate.onBindViewHolderInner(position, item  , viewHolder, payloads ?: FULLUPDATE_PAYLOADS)
    }

    // =======================================分割线=====================================

    fun onViewRecycled(@NonNull viewHolder: Holder) {
        val delegate = getDelegate(viewHolder.itemViewType) ?: throw NullPointerException("No delegate found for " + viewHolder)
        delegate.onViewRecycled(viewHolder)
    }

    fun onFailedToRecycleView(@NonNull viewHolder: Holder): Boolean {
        val delegate = getDelegate(viewHolder.itemViewType) ?: throw NullPointerException("No delegate found for " + viewHolder)
        return delegate.onFailedToRecycleView(viewHolder)
    }

    fun onViewAttachedToWindow(viewHolder: Holder) {
        val delegate = getDelegate(viewHolder.itemViewType) ?: throw NullPointerException("No delegate found for " + viewHolder)
        delegate.onViewAttachedToWindow(viewHolder)
    }

    fun onViewDetachedFromWindow(@NonNull viewHolder: Holder) {
        val delegate = getDelegate(viewHolder.itemViewType) ?: throw NullPointerException("No delegate found for " + viewHolder)
        delegate.onViewDetachedFromWindow(viewHolder)
    }

    fun getDelegate(viewType: Int): ViewDelegate<*>? {
        return delegates.get(viewType)
    }
}