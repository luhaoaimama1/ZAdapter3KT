package com.zone.adapter3kt.divder

import android.graphics.Rect
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.adapter.BaseAdapter

/**
 * Copyright (c) 2018 BiliBili Inc.
 *[2018/9/30] by Zone
 */
abstract class BaseDivder<T>(val adapter: QuickAdapter<T>) {
    companion object {
        @JvmField
        val PLAYLOADS_DIVDER_INVALIDATE = BaseAdapter.PAYLOADS_DIVDER_INVALIDATE
    }
    //根据位置,去设置divder
    abstract fun getItemOffsets(outRect: Rect, itemPosition: Int)

    //根据改变的一些pos, 去通知返回结果的位置 进行更新divder
    abstract fun notifyItemChangedInner(index: Int)
    abstract fun notifyItemRangeChangedInner(positionStart: Int, itemCount: Int)
    abstract fun notifyItemMovedInner(fromPosition: Int, toPosition: Int)
    abstract fun notifyItemRangeRemovedInner(positionStart: Int, itemCount: Int)
    abstract fun notifyItemInsertedInner(position: Int)
    abstract fun notifyItemRangeInsertedInner(positionStart: Int, itemCount: Int)
}