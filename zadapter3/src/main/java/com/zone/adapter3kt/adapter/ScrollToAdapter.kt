package com.zone.adapter3kt.adapter

import android.content.Context
import com.zone.adapter3kt.QuickAdapter
import java.lang.IllegalStateException

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

    fun scrollToFirst() = recyclerView?.scrollToPosition(0)

    fun scrollTo(item: T) {
        val posi = mHFList.indexOfItem(item)
        if (posi != -1) recyclerView?.scrollToPosition(posi)
    }

    fun fastLoadData(item: T, method: QuickAdapter<T>.() -> Unit) {
        add(item)
        scrollTo(item)
        recyclerView?.postDelayed({
            doMethod(method)
        }, 16)
    }

    fun fastLoadData(item: List<T>, method: QuickAdapter<T>.() -> Unit) {
        if (item.size > 0) {
            add(item)
            scrollTo(item[0])
            recyclerView?.postDelayed({
                doMethod(method)
            }, 16)
        }
    }

    private fun doMethod(method: QuickAdapter<T>.() -> Unit) {
        if (this@ScrollToAdapter is QuickAdapter) {
            method.invoke(this@ScrollToAdapter)
        } else throw IllegalStateException("当前adapter 不是QuickAdapter")
    }
}