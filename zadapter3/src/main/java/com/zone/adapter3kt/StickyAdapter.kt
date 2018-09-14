package com.zone.adapter3kt

import android.content.Context
import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter.R
import com.zone.adapter3kt.adapter.LoadMoreAdapter
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.sticky.StickyChildHolder
import com.zone.adapter3kt.sticky.StickyHolder
import com.zone.adapter3kt.sticky.StickyOnScrollListener

/**
 *[2018] by Zone
 */
//todo 总结成一个 真正的remove方法 这样覆盖好处理
open class StickyAdapter<T>(context: Context, tag: Any? = null) : LoadMoreAdapter<T>(context, tag) {

    class StickyEntity<T>(val posi: Int, val data: DataWarp<T>)

    val stickyViewTypeMap: HashMap<Int, Int> = HashMap()
    val stickyList: ArrayList<StickyEntity<T>> = ArrayList()
    // 我们用FrameLayout 作为 sticky的布局 可以把它当做整体 回收就不用用管 他被回收也仅仅是里面stickyview可能会被我们持有没事的
    //  用getViewForPosition 去获取  对于scrollto 定位的那种 他可能还没生成。所以用他
//    var stickMap: HashMap<Int, DataWarp<T>> = HashMap()
    var nowStickyViewType = STICKY_VALUE
    var stickyScroller: StickyOnScrollListener<T>? = null

    fun setStickyShowFrameLayout(vpShow: FrameLayout, @ColorInt color: Int = Color.TRANSPARENT) {
        stickyScroller = StickyOnScrollListener(vpShow, this, color)
        addScrollerListener(recyclerView)
    }

    override fun dataWithConfigChanged() {
        stickyList.clear()
        mHFList.mListCollection.loop { index, item ->
            if (item.extraConfig.isSticky) {
                if (stickyViewTypeMap.get(item.extraConfig.viewStyle) == null) {
                    stickyViewTypeMap.put(item.extraConfig.viewStyle, nowStickyViewType)
                    delegatesManager.oriViewTypeMap.put(item.extraConfig.viewStyle, nowStickyViewType)
                    if (nowStickyViewType == STICKY_VALUE_END) {
                        throw IllegalStateException("sticky count 超过限定个数")
                    } else nowStickyViewType--
                }
                stickyList.add(StickyEntity(index, item))
            }
            false
        }
        stickyScroller?.onScrolled(recyclerView, 0, 0)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        addScrollerListener(recyclerView)
    }

    fun addScrollerListener(recyclerView: RecyclerView?) {
        if (stickyScroller != null) { //安全使用
            recyclerView?.removeOnScrollListener(stickyScroller)
            recyclerView?.addOnScrollListener(stickyScroller)
        }
    }

    override fun getItemViewType(position: Int): Int {
        for (stickyEntity in stickyList) {
            if (stickyEntity.posi == position) {
                val key = stickyEntity.data.extraConfig.viewStyle
                return stickyViewTypeMap.get(key)!!
            }
        }
        return super.getItemViewType(position)
    }

    //todo stick 按照满行算吗？
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        if (viewType <= STICKY_VALUE && viewType >= STICKY_VALUE_END) {
            //stickyViewTypeMap 获得对应的值 然后生成 delegete
            for (entry in stickyViewTypeMap.entries) {
                if (entry.value == viewType) {
                    val createHolder = delegatesManager.onCreateViewHolder(parent, viewType)
                    val viewGroup = LayoutInflater.from(context).inflate(R.layout.base_vp, parent, false) as FrameLayout
                    val stickyChildHolder = StickyChildHolder(createHolder.itemView, entry.key)
                    viewGroup.addView(stickyChildHolder.itemView)
                    return StickyHolder(viewGroup, stickyChildHolder.itemView, stickyChildHolder)
                }
            }
            throw IllegalStateException(" 未找到 对应的 viewType")
        }
        return super.onCreateViewHolder(parent, viewType)
    }


    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>?) {
        // 如果bind 的时候 发现 外面的posi与内部的pos一样那么把站位拿过来 不用加载资源！！！
        if (holder is StickyHolder && stickyScroller != null && stickyScroller!!.preStickyIndex == position) {
            stickyScroller!!.adapterAddPlaceHolder(holder)
            return
        }

        if (holder is StickyHolder) { //绝对是先有 才会被站位的可能 那么先有就代表onBind的时候一定 不是站位view
            val childIsPlaceHolder = stickyScroller != null && stickyScroller!!.childIsPlaceholderView(holder.itemView as FrameLayout)
            if (!childIsPlaceHolder) { //bind view
                super.onBindViewHolder(holder.stickyChildHolder, position, payloads)
            }
        } else if (holder is StickyChildHolder) { //scroller 里创建与绑定的
            super.onBindViewHolder(holder, position, payloads)
        } else super.onBindViewHolder(holder, position, payloads)
    }
}