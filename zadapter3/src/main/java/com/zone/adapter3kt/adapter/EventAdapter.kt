package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.holder.BaseHolder

/**
 *[2018] by Zone
 */

interface OnItemClickListener {
    fun onItemClick(parent: ViewGroup, view: View, position: Int)
}

interface OnItemLongClickListener {
    fun onItemLongClick(parent: ViewGroup, view: View, position: Int): Boolean
}

open class EventAdapter<T>(context: Context) : EHFAdapter<T>(context) {

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnItemLongClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<RecyclerView.ViewHolder> {
        val createViewHolder = super.onCreateViewHolder(parent, viewType)
        initOnItemClickListener(createViewHolder)
        return createViewHolder
    }

    private fun initOnItemClickListener(baseHolder: BaseHolder<RecyclerView.ViewHolder>) {
        if (onItemClickListener != null)
            baseHolder.itemView.setOnClickListener { v ->
                if (onItemClickListener != null) {
                    QuickConfig.d("OnItemClick: position" + baseHolder.layoutPosition)
                    onItemClickListener!!.onItemClick(baseHolder.itemView.parent as ViewGroup, v, baseHolder.layoutPosition)
                }
            }
        if (onItemLongClickListener != null)
            baseHolder.itemView.setOnLongClickListener { v ->
                if (onItemLongClickListener != null) {
                    QuickConfig.d("OnItemLongClick: position" + baseHolder.layoutPosition)
                    return@setOnLongClickListener onItemLongClickListener!!.onItemLongClick(baseHolder.itemView.parent as ViewGroup, v, baseHolder.layoutPosition)
                }
                return@setOnLongClickListener false
            }
    }
}