package com.zone.adapter3kt.delegate

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView

import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.data.DataWarp

/**
 * [2018] by Zone
 */

class ResDelegate<T>(@param:LayoutRes private val layoutIdInner: Int) : ViewDelegate<T,BaseHolder<RecyclerView.ViewHolder>>() {
    override fun onBindViewHolder(position: Int, item: DataWarp<T>, baseHolder: BaseHolder<RecyclerView.ViewHolder>, payloads: List<*>) {}
    override val layoutId: Int = layoutIdInner
}
