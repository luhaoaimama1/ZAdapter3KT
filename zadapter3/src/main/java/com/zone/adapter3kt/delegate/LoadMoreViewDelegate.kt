package com.zone.adapter3kt.delegate

import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.holder.BaseHolder


/**
 * [2018] by Zone
 * [.complete]
 * [.loading]
 * [.fail]
 * [.end]
 */
abstract class LoadMoreViewDelegate : ViewDelegate<Any, BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>>() {
    //移除
    open fun complete() {}

    //加载中
    open fun loading() {}

    //失败
    open fun fail() {}

    //数据到底部了
    open fun end() {}


    abstract fun clone_(): LoadMoreViewDelegate
}
