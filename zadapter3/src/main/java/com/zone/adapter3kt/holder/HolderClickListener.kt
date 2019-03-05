package com.zone.adapter3kt.holder

import android.support.v7.widget.RecyclerView
import android.view.View

interface HolderClickListener {
    fun onClick(v: View?, viewBaseHolder: BaseHolder<RecyclerView.ViewHolder>)
}