package com.zone.adapter3kt.delegate

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.data.DataWarp

/**
 * [2018] by Zone
 */

abstract class ViewDelegate<T> {
    lateinit var adapter: BaseAdapter<*>

    fun onCreateViewHolder(parent: ViewGroup): Holder {
        val inflate = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        initData(inflate, parent)
        val holder = Holder(inflate)
        setListener(holder)
        return holder
    }

    @get:LayoutRes
    protected abstract val layoutId: Int

    /**
     * 为了兼容 加载更多那种与数据无关的类型
     */
    fun onBindViewHolderInner(position: Int, item: Any, holder: Holder, payloads: List<*>) {
        onBindViewHolder(position, item as DataWarp<T>, holder, payloads)
    }

    abstract fun onBindViewHolder(position: Int, item: DataWarp<T>, holder: Holder, payloads: List<*>)

    fun onViewRecycled(viewHolder: Holder) {}

    fun onFailedToRecycleView(holder: Holder): Boolean {
        return false
    }

    fun onViewAttachedToWindow(holder: Holder) {}

    fun onViewDetachedFromWindow(holder: Holder) {}

    fun setListener(holder: Holder) {}

    open fun initData(convertView: View, parent: ViewGroup) {}
}
