package com.zone.adapter3kt.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.holder.Holder

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val createViewHolder = super.onCreateViewHolder(parent, viewType)
        initOnItemClickListener(createViewHolder)
        return createViewHolder
    }

    private fun initOnItemClickListener(holder: Holder) {
        if (onItemClickListener != null)
            holder.itemView.setOnClickListener { v ->
                if (onItemClickListener != null) {
                    QuickConfig.d("OnItemClick: position" + holder.getLayoutPosition())
                    onItemClickListener!!.onItemClick(holder.itemView.parent as ViewGroup, v, holder.getLayoutPosition())
                }
            }
        if (onItemLongClickListener != null)
            holder.itemView.setOnLongClickListener { v ->
                if (onItemLongClickListener != null) {
                    QuickConfig.d("OnItemLongClick: position" + holder.getLayoutPosition())
                    return@setOnLongClickListener onItemLongClickListener!!.onItemLongClick(holder.itemView.parent as ViewGroup, v, holder.getLayoutPosition())
                }
                return@setOnLongClickListener false
            }
    }
}