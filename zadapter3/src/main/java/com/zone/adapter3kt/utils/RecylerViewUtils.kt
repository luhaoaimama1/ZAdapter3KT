package com.zone.adapter3kt.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *[2018/11/15] by Zone
 */

fun androidx.recyclerview.widget.RecyclerView.getFirstLastPosrecyclerView(): Pair<Int, Int> {
    var firstVisiblePos = -1
    var lastVisiblePos = -1
    val recyclerView = this
    when (recyclerView.layoutManager) {
        is androidx.recyclerview.widget.LinearLayoutManager -> {
            firstVisiblePos = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
            lastVisiblePos = (recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
        }
        is androidx.recyclerview.widget.GridLayoutManager -> {
            firstVisiblePos = (recyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager).findFirstVisibleItemPosition()
            lastVisiblePos = (recyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager).findLastVisibleItemPosition()
        }
        is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
            var firstVisibleItems: IntArray? = null
            var lastVisibleItems: IntArray? = null
            firstVisibleItems = (recyclerView.layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstVisibleItems)
            lastVisibleItems = (recyclerView.layoutManager as androidx.recyclerview.widget.StaggeredGridLayoutManager).findLastVisibleItemPositions(lastVisibleItems)
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