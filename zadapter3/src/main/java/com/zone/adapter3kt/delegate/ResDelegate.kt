package com.zone.adapter3kt.delegate

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.data.DataWarp

/**
 * [2018] by Zone
 */

class ResDelegate<T>(@param:LayoutRes private val layoutIdInner: Int) : ViewDelegate<T,BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>() {
    override fun onBindViewHolder(position: Int, item: DataWarp<T>, baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>, payloads: List<*>) {}
    override val layoutId: Int = layoutIdInner
}
