package com.zone.adapter3kt.delegate

import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter.R

import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.adapter.ContentAdapter
import com.zone.adapter3kt.adapter.DelegatesAdapter
import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.data.DataWarp

/**
 * [2018] by Zone
 */

abstract class ViewDelegate<T> {

    lateinit var adapter: ContentAdapter<*>

    fun onCreateViewHolder(parent: ViewGroup): Holder {
        val inflate = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val divderFrameLayout = addParentDivderFrameLayout(parent, inflate)
        val holder = Holder(divderFrameLayout)
        initData(divderFrameLayout, parent)
        setListener(holder)
        return holder
    }

    fun addParentDivderFrameLayout(parent: ViewGroup, inflate: View): FrameLayout {
        val divderFrameLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.divder_vp, parent, false)
            as FrameLayout

        val onCreateViewLPwidth = inflate.layoutParams.width
        val onCreateViewLPheight = inflate.layoutParams.height
        val warpType = ViewGroup.LayoutParams.WRAP_CONTENT
        val matchType = ViewGroup.LayoutParams.MATCH_PARENT

        val divderLPwidth = if (onCreateViewLPwidth == warpType || onCreateViewLPwidth == matchType) {
            onCreateViewLPwidth
        } else warpType

        val divderLPheight = if (onCreateViewLPheight == warpType || onCreateViewLPheight == matchType) {
            onCreateViewLPheight
        } else warpType

        divderFrameLayout.layoutParams = FrameLayout.LayoutParams(divderLPwidth, divderLPheight)
        divderFrameLayout.addView(inflate)
        return divderFrameLayout
    }

    @get:LayoutRes
    protected abstract val layoutId: Int

    /**
     * 为了兼容 加载更多那种与数据无关的类型
     */
    fun onBindViewHolderInner(position: Int, item: Any, holder: Holder, payloads: List<*>) {
        if (payloads.contains(BaseAdapter.PAYLOADS_DIVDER_INVALIDATE)) {
            changeDivder(holder, adapter, position)
        } else {
            changeDivder(holder, adapter, position)
            onBindViewHolder(position, item as DataWarp<T>, holder, payloads)
        }
    }

    fun changeDivder(viewHolder: Holder, adapter: DelegatesAdapter<*>, position: Int) {
        if (adapter is ContentAdapter<*>) {
            val divderFl = viewHolder.getView<FrameLayout>(R.id.fl_divder);
            val divderRect = Rect()
            adapter.divderManager?.getItemOffsets(divderRect, position)
            divderFl.setPadding(divderRect.left, divderRect.top, divderRect.right, divderRect.bottom)
        }
    }

    abstract fun onBindViewHolder(position: Int, item: DataWarp<T>, holder: Holder, payloads: List<*>)

    fun onViewRecycled(viewHolder: Holder) {}

    fun onFailedToRecycleView(holder: Holder): Boolean = false

    fun onViewAttachedToWindow(holder: Holder) {}

    fun onViewDetachedFromWindow(holder: Holder) {}

    fun setListener(holder: Holder) {}

    open fun initData(convertView: View, parent: ViewGroup) {}
}
