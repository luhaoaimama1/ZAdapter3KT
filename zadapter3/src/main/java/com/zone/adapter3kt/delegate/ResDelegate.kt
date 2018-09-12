package com.zone.adapter3kt.delegate

import android.support.annotation.LayoutRes

import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.data.DataWarp

/**
 * [2018] by Zone
 */

class ResDelegate<T>(@param:LayoutRes private val layoutIdInner: Int) : ViewDelegate<T>() {
    override fun onBindViewHolder(position: Int, item: DataWarp<T>, holder: Holder, payloads: List<*>) {}
    override val layoutId: Int = layoutIdInner
}
