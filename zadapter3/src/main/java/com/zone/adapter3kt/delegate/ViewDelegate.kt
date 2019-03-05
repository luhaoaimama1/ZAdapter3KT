package com.zone.adapter3kt.delegate

import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter.R

import com.zone.adapter3kt.adapter.BaseAdapter
import com.zone.adapter3kt.adapter.ContentAdapter
import com.zone.adapter3kt.adapter.DelegatesAdapter
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.holder.HolderClickListener

/**
 * [2018] by Zone
 */

abstract class ViewDelegate<T, H : BaseHolder<RecyclerView.ViewHolder>> {
    // =======================================
    // ============ 公开方法  ==============
    // =======================================

    @get:LayoutRes
    protected abstract val layoutId: Int

    open fun initData(convertView: View, parent: ViewGroup) {}
    open fun registerClickListener(): Array<Int>? = null

    open fun setListener(baseHolder: H) {}
    //为什么这么写 而不用lambda方式设置呢？ 因为是set如果不统一处理或者override处理 。别的地方可能会覆盖并难自知
    open fun onClick(v: View?, viewBaseHolder: H, posi: Int, item: DataWarp<T>) {}

    abstract fun onBindViewHolder(position: Int, item: DataWarp<T>, baseHolder: H, payloads: List<*>)

    open fun onViewRecycled(viewBaseHolder: H) {}
    open fun onFailedToRecycleView(baseHolder: H): Boolean = false
    open fun onViewAttachedToWindow(baseHolder: H) {}
    open fun onViewDetachedFromWindow(baseHolder: H) {}

    // =======================================
    // ============ 内部调用的方法  ==============
    // =======================================
    lateinit var adapter: ContentAdapter<*>

    fun onCreateViewHolder(parent: ViewGroup): H {
        val inflate = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val divderFrameLayout = addParentDivderFrameLayout(parent, inflate)
        val holder = createHolder(divderFrameLayout)
        initData(divderFrameLayout, parent)
        val clickIds = registerClickListener()
        setOnClickListener(clickIds, holder )
        setListener(holder as H)
        return holder
    }

    open fun createHolder(view: View): H = Holder(view) as H

    private fun addParentDivderFrameLayout(parent: ViewGroup, inflate: View): FrameLayout {
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

    /**
     * 为了兼容 加载更多那种与数据无关的类型
     */
    fun onBindViewHolderInner(position: Int, item: Any, baseHolder: H, payloads: List<*>) {
        if (payloads.contains(BaseAdapter.PAYLOADS_DIVDER_INVALIDATE)) {
            changeDivder(baseHolder, adapter, position)
        } else {
            changeDivder(baseHolder, adapter, position)
            try {
                onBindViewHolder(position, item as DataWarp<T>, baseHolder, payloads)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun changeDivder(viewBaseHolder: H, adapter: DelegatesAdapter<*>, position: Int) {
        if (adapter is ContentAdapter<*>) {
            val divderFl = viewBaseHolder.getView<FrameLayout>(R.id.fl_divder);
            val divderRect = Rect()
            adapter.divderManager?.getItemOffsets(divderRect, position)
            divderFl.setPadding(divderRect.left, divderRect.top, divderRect.right, divderRect.bottom)
        }
    }

    fun setOnClickListener(clickIds: Array<Int>?, baseHolder: H) {
        clickIds?.forEach {
            baseHolder.setOnHolderClickListener(object : HolderClickListener {
                override fun onClick(v: View?, viewBaseHolder: BaseHolder<RecyclerView.ViewHolder>) {
                    val (pos, item) = getItemByHolder(viewBaseHolder as H)
                    item?.apply { onClick(v, viewBaseHolder, pos, this as DataWarp<T>) }
                }
            }, it)
        }
    }

    fun getItemByHolder(viewBaseHolder: H): Pair<Int, DataWarp<out Any?>?> {
        val pos = viewBaseHolder.layoutPosition
        val item = adapter.mHFList.mListCollection.getItem(pos)
        return Pair(pos, item)
    }

}
