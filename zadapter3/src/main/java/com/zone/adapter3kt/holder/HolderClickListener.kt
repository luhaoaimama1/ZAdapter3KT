package com.zone.adapter3kt.holder

import androidx.recyclerview.widget.RecyclerView
import android.view.View

interface HolderClickListener {
    fun onClick(v: View?, viewBaseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>)
}