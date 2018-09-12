package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.*
import com.zone.adapter3kt.ViewStyle
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.data.HFListHistory

/**
 *[2018] by Zone
 * 空 头 尾部 处理
 *
 * Tips:绝对以数据操控 而不用占位符！因为动画 等好多都不好处理
 */
abstract class ScrollToAdapter<T>(context: Context) : ContentAdapter<T>(context) {
    fun scrollToLast() = recyclerView?.scrollToPosition(itemCount - 1)
}