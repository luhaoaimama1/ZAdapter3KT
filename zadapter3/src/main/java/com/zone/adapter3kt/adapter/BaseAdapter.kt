package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.zone.adapter3kt.holder.BaseHolder

/**
 *[2018] by Zone
 */
abstract class BaseAdapter<T>(protected var context: Context)
    : RecyclerView.Adapter<BaseHolder<RecyclerView.ViewHolder>>() {

    companion object {
        @JvmField
        val DEFAULT_VALUE = -1
        @JvmField
        val EMPTY_VALUE = -2
        @JvmField
        val LOADING_VALUE = -3
        @JvmField
        val STICKY_VALUE = -4
        @JvmField
        val STICKY_VALUE_END = -30
        @JvmField
        val VIEW_TYPE_NONE_VALUE = -100

        @JvmField
        val PAYLOADS_DIVDER_INVALIDATE = -101
    }
}