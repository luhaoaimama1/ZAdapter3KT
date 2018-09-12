package com.zone.adapter3kt.delegate

/**
 * [2018] by Zone
 * [.complete]
 * [.loading]
 * [.fail]
 * [.end]
 */
abstract class LoadMoreViewDelegate : ViewDelegate<Any>() {
    //移除
    open fun complete() {}

    //加载中
    open fun loading() {}

    //失败
    open fun fail() {}

    //数据到底部了
    open fun end() {}
}
