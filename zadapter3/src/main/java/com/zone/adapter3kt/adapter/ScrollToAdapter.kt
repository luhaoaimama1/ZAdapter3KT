package com.zone.adapter3kt.adapter

import android.content.Context

/**
 *[2018] by Zone
 * 空 头 尾部 处理
 *
 * Tips:绝对以数据操控 而不用占位符！因为动画 等好多都不好处理
 */
abstract class ScrollToAdapter<T>(context: Context) : EventAdapter<T>(context) {
    // =======================================
    // ============scroll 系列 ==============
    // =======================================
    fun scrollToLast() = recyclerView?.scrollToPosition(itemCount - 1)

    fun scrollTo(item: T) {
        val posi = mHFList.indexOfItem(item)
        if (posi != -1) recyclerView?.scrollToPosition(posi)
    }
}