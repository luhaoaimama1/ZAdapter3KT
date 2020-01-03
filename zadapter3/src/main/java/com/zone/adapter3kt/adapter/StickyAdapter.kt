package com.zone.adapter3kt.adapter

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter.R
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.sticky.StickyChildHolder
import com.zone.adapter3kt.sticky.StickyHolder
import com.zone.adapter3kt.sticky.StickyOnScrollListener

/**
 *[2018] by Zone
 * Tips:为什么不是 通过setStickyShowFrameLayout()去设置enableSticky=true呢？
 * 因为vpShow是view 可能会遇到view没生成就添加数据的情况吧 , 而且 添加数据前开启enableSticky 才会准确！
 *
 * 默认关闭是因为他 进行了大量了 关于 sticky相关的操作。
 */
open class StickyAdapter<T>(context: Context) : LoadMoreAdapter<T>(context) {

    class StickyEntity<T>(val posi: Int, val data: DataWarp<T>)

    val stickyViewTypeMap: HashMap<Int, Int> = HashMap()
    val stickyList: ArrayList<StickyEntity<T>> = ArrayList()
    var nowStickyViewType = STICKY_VALUE
    var stickyScroller: StickyOnScrollListener<T>? = null
    var enableSticky = false

    fun setStickyShowFrameLayout(vpShow: FrameLayout, @ColorInt color: Int = Color.TRANSPARENT) {
        if (!enableSticky) throw IllegalStateException("需要开启 enableSticky")
        stickyScroller = StickyOnScrollListener(vpShow, this, color)
        addScrollerListener(recyclerView)
    }

    override fun dataWithConfigChanged() {
        if (!enableSticky) return
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
        recyclerView?.let {
            stickyScroller?.onScrolled(it, 0, 0)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (!enableSticky) return
        addScrollerListener(recyclerView)
    }

    fun addScrollerListener(recyclerView: RecyclerView?) {
        stickyScroller?.let {
            recyclerView?.removeOnScrollListener(it)
            recyclerView?.addOnScrollListener(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (enableSticky) {
            for (stickyEntity in stickyList) {
                if (stickyEntity.posi == position) {
                    val key = stickyEntity.data.extraConfig.viewStyle
                    return stickyViewTypeMap.get(key)!!
                }
            }
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<RecyclerView.ViewHolder> {
        if (enableSticky && viewType <= STICKY_VALUE && viewType >= STICKY_VALUE_END) {
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

    override fun onBindViewHolder(baseHolder: BaseHolder<RecyclerView.ViewHolder>, position: Int, payloads: MutableList<Any>) {
        if (enableSticky) {
            // 如果bind的时候 发现 外面的posi与内部的pos一样,那么把占位拿过来  从而不用走BindViewHolder了
            if (baseHolder is StickyHolder && stickyScroller != null && stickyScroller!!.preShowStickyPosi == position) {
                stickyScroller!!.adapterAddPlaceHolder(baseHolder)
                return
            }
            if (baseHolder is StickyHolder) { //绝对是先有 才会被站位的可能 那么先有就代表onBind的时候一定 不是站位view
                val childIsPlaceHolder = stickyScroller != null && stickyScroller!!.childIsPlaceholderView(baseHolder.itemView as FrameLayout)
                //当内部不是占位的时候  进行bindViewHolder
                if (!childIsPlaceHolder) super.onBindViewHolder(baseHolder.stickyChildHolder, position, payloads)
            } else if (baseHolder is StickyChildHolder) { //scroller 里创建与绑定的
                super.onBindViewHolder(baseHolder, position, payloads)
            } else super.onBindViewHolder(baseHolder, position, payloads)
        } else super.onBindViewHolder(baseHolder, position, payloads)
    }
}