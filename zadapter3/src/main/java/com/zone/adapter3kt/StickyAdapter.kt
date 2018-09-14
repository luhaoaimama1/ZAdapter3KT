package com.zone.adapter3kt

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
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

    internal val mAttachedStickyMuliScrap = SparseArray<ArrayList<RecyclerView.ViewHolder>>()
    internal val mCachedStickyMuliViews = SparseArray<ArrayList<RecyclerView.ViewHolder>>()

    var stickyScroller: StickyOnScrollListener<T>? = null
    fun setStickyShowFrameLayout(vpShow: FrameLayout) {
        stickyScroller = StickyOnScrollListener(vpShow, this)
    }

    /**
     * 创建与复用？  上面可以复用。 下面也可以复用。  上面最多只能存在一个view 而这个view 可能在下面已经被回收了。
     *
     * 要明白一件事： 上面其实什么都没有。缓存 其实不用管。 就是狸猫换太子
     *
     * 上面:StickyView/null
     * scrollerListenter: 可能  创建 StickyView、replace recyclerView中的StickyView
     *
     * onCreate: FrameLayout, 占位View/StickyView : 顺序：StickyView  逆序：nowStickyView?
     * bindView: StickyView 回归。
     */
    fun findStickyPosi(posi: Int, viewGroup: ViewGroup): View {
        val viewType = getItemViewType(posi)
        var delegeteItemView: View? = null
        for (entry in stickyViewTypeMap.entries) {
            if (entry.value == viewType) {
                val delegate = delegatesManager.getDelegate(entry.key) //原始viewtype生成对应的delegate
                if (delegate == null) throw IllegalStateException(" No delegate found for item ")
                val cachedList = mCachedStickyMuliViews.get(entry.key)
                if (cachedList != null && cachedList.size != 0) {
                    delegeteItemView = cachedList[0].itemView
                } else {
                    delegeteItemView = delegate.onCreateViewHolder(viewGroup).itemView
                }
            }
        }
        return delegeteItemView!!
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
    }

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
    }

    override fun dataWithConfigChanged() {
        stickyList.clear()
        mHFList.mListCollection.loop { index, item ->
            if (item.extraConfig.isSticky) {
                if (stickyViewTypeMap.get(item.extraConfig.viewStyle) == null) {
                    stickyViewTypeMap.put(item.extraConfig.viewStyle, nowStickyViewType)
                    if (nowStickyViewType == STICKY_VALUE_END) {
                        throw IllegalStateException("sticky count 超过限定个数")
                    } else nowStickyViewType++
                }
                stickyList.add(StickyEntity(index, item))
            }
            false
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (stickyScroller != null) recyclerView.addOnScrollListener(stickyScroller)
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
        if (viewType <= STICKY_VALUE || viewType >= STICKY_VALUE_END) {
            //stickyViewTypeMap 获得对应的值 然后生成 delegete
            for (entry in stickyViewTypeMap.entries) {
                if (entry.value == viewType) {
                    val delegate = delegatesManager.getDelegate(entry.key)
                    if (delegate == null) throw IllegalStateException(" No delegate found for item ")
                    val viewGroup = LayoutInflater.from(context).inflate(R.layout.base_vp, parent, false) as FrameLayout
                    val stickyChildHolder=StickyChildHolder(delegate.onCreateViewHolder(parent).itemView, entry.key)
                    viewGroup.addView(stickyChildHolder.itemView)
                    return StickyHolder(viewGroup, delegate, stickyChildHolder.itemView,stickyChildHolder)
                }
            }
            throw IllegalStateException(" 未找到 对应的 viewType")
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: Holder, position: Int, payloads: MutableList<Any>?) {
        if (holder is StickyHolder) {
            //承载的FrameLayout  移除里面的view
            (holder.itemView as (ViewGroup)).removeAllViews()
            //找到里面的 view 把他的父亲移除
            val delegateParent = holder.delegateView.parent
            if (delegateParent != null) (delegateParent as ViewGroup).removeAllViews()
            //然后holder承载FrameLayout 安全的添加他
            holder.itemView.addView(holder.delegateView)

            //bind view
            onBindViewHolderSticky(position, holder, payloads)
        } else super.onBindViewHolder(holder, position, payloads)
    }

    fun onBindViewHolderSticky(position: Int, holder: StickyHolder, payloads: MutableList<Any>?) {
        val item = mHFList.mListCollection.getItem(position)
        if (item == null) return
        onBindViewHolderWithData(holder, position, item, payloads)
        if (item.data == null) return
        holder.delegate.onBindViewHolderInner(position, item, holder, payloads
                ?: delegatesManager.FULLUPDATE_PAYLOADS)
    }
}