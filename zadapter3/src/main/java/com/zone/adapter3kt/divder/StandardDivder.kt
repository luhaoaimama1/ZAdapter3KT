package com.zone.adapter3kt.divder

import android.graphics.Rect
import com.zone.adapter3kt.QuickAdapter

/**
 * Copyright (c) 2018 BiliBili Inc.
 *[2018/9/30] by Zone
 * 某个改变会通知上一个
 *
 * todo ing 仅仅考虑线性布局了。其他的没考虑
 */
class StandardDivder<T>(adapter: QuickAdapter<T>) : BaseDivder<T>(adapter) {

    //    当前根据  下一个改变
    override fun getItemOffsets(outRect: Rect, itemPosition: Int) {
        val nowRealItem = adapter.getRealItem(itemPosition)
        val nextRealItem = adapter.getRealItem(itemPosition + 1)
        if (nowRealItem == null) return
        if (nowRealItem.extraConfig.divderRect == null) return
        outRect.set(nowRealItem.extraConfig.divderRect)
        // 头部
        if (itemPosition == 0) outRect.top = 0
        //尾部
        if (adapter.itemCount == itemPosition + 1) outRect.bottom = 0

        //如果下一个会强制隐藏 当前的则当前bottom会为0
        if (nextRealItem != null && nextRealItem.extraConfig.isHideBeforeDivder)
            outRect.bottom = 0
    }

    fun notifyBeforeDivder(index: Int) {
        if (index > 0) adapter.notifyItemChanged(index - 1, PLAYLOADS_DIVDER_INVALIDATE)
    }

    // 某个改变会通知上一个
    override fun notifyItemChangedInner(index: Int) = notifyBeforeDivder(index)

    override fun notifyItemRangeRemovedInner(positionStart: Int, itemCount: Int) =
        notifyBeforeDivder(positionStart)

    override fun notifyItemInsertedInner(position: Int) = notifyBeforeDivder(position)
    override fun notifyItemRangeInsertedInner(positionStart: Int, itemCount: Int) =
        notifyBeforeDivder(positionStart)

    override fun notifyItemRangeChangedInner(positionStart: Int, itemCount: Int) =
        notifyBeforeDivder(positionStart)

    override fun notifyItemMovedInner(fromPosition: Int, toPosition: Int) {
        notifyBeforeDivder(fromPosition)
        notifyBeforeDivder(toPosition)
    }

}