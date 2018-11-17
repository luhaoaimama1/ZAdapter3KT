package com.zone.adapter3kt.utils

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager

/**
 *[2018/11/15] by Zone
 */

fun RecyclerView.getFirstLastPosrecyclerView(): Pair<Int, Int> {
    var firstVisiblePos = -1
    var lastVisiblePos = -1
    val recyclerView = this
    when (recyclerView.layoutManager) {
        is LinearLayoutManager -> {
            firstVisiblePos = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            lastVisiblePos = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }
        is GridLayoutManager -> {
            firstVisiblePos = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
            lastVisiblePos = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
        }
        is StaggeredGridLayoutManager -> {
            var firstVisibleItems: IntArray? = null
            var lastVisibleItems: IntArray? = null
            firstVisibleItems = (recyclerView.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstVisibleItems)
            lastVisibleItems = (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(lastVisibleItems)
            if (firstVisibleItems != null && firstVisibleItems.size > 0) {
                firstVisiblePos = firstVisibleItems[0]
            }
            if (lastVisibleItems != null && lastVisibleItems.size > 0) {
                lastVisiblePos = lastVisibleItems[0]
            }
        }
        else -> throw IllegalStateException("不支持其他布局")
    }
    return Pair(firstVisiblePos, lastVisiblePos)
}