package com.zone.adapter3kt.delegate

import android.util.SparseArray
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import android.view.ViewGroup
import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.adapter.ContentAdapter
import com.zone.adapter3kt.adapter.DelegatesAdapter
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.sticky.StickyChildHolder


/**
 *[2018] by Zone
 */
class DelegatesManager(val adapter: DelegatesAdapter<*>) {
    var delegates = SparseArray<ViewDelegate<*,BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>>()
    internal val FULLUPDATE_PAYLOADS = Collections.EMPTY_LIST
    val oriViewTypeMap: HashMap<Int, Int> = HashMap()

    private fun findOriViewType(itemViewType2: Int, viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>? = null): Int {
        var itemViewType = itemViewType2
        if (viewBaseHolder != null && viewBaseHolder is StickyChildHolder) itemViewType = viewBaseHolder.viewTypeInner
        for (entry in oriViewTypeMap.entries) {
            if (entry.value == itemViewType) {
                return entry.key
            }
        }
        return itemViewType
    }

    fun registerDelegate(viewType: Int, delegate: ViewDelegate<*,BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>) {
        delegates.put(viewType, delegate)
        if (adapter is ContentAdapter<*>) delegate.adapter = adapter
    }

    // =======================================分割线=====================================

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder> {
        val delegate = getDelegateNoMap(findOriViewType(viewType))
                ?: if (viewType == BaseAdapter.LOADING_VALUE) throw NullPointerException("没有注册 加载更多")
                else throw NullPointerException("No AdapterDelegate added for ViewType " + viewType)
        val onCreateViewHolder = delegate.onCreateViewHolder(parent)
        delegate.setListener(onCreateViewHolder)
        return onCreateViewHolder
    }

    fun onBindViewHolder(position: Int, @NonNull item: Any, viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>, payloads: List<Any>?) {
        val viewType = findOriViewType(viewBaseHolder.itemViewType, viewBaseHolder)
        val delegate = getDelegateNoMap(viewType) ?: throw NullPointerException("No delegate found for  " + viewType)
        delegate.onBindViewHolderInner(position, item, viewBaseHolder, payloads ?: FULLUPDATE_PAYLOADS)
    }



    // =======================================分割线=====================================

    fun onViewRecycled(@NonNull viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        val viewType = findOriViewType(viewBaseHolder.itemViewType, viewBaseHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewRecycled(viewBaseHolder)
    }

    fun onFailedToRecycleView(@NonNull viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>): Boolean {
        val viewType = findOriViewType(viewBaseHolder.itemViewType, viewBaseHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        return delegate.onFailedToRecycleView(viewBaseHolder)
    }

    fun onViewAttachedToWindow(viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        val viewType = findOriViewType(viewBaseHolder.itemViewType, viewBaseHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewAttachedToWindow(viewBaseHolder)
    }

    fun onViewDetachedFromWindow(@NonNull viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        val viewType = findOriViewType(viewBaseHolder.itemViewType, viewBaseHolder)
        val delegate = getDelegateNoMap(viewType)
                ?: throw NullPointerException("No delegate found for " + viewType)
        delegate.onViewDetachedFromWindow(viewBaseHolder)
    }

    fun getDelegateNoMap(viewType: Int): ViewDelegate<*,BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>? {
        return delegates.get(viewType)
    }

    fun getDelegateMap(itemViewType: Int, viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>? = null): ViewDelegate<*,BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>? {
        return delegates.get(findOriViewType(itemViewType, viewBaseHolder))
    }
}