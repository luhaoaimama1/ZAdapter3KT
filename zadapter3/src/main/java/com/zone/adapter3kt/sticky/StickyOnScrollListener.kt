package com.zone.adapter3kt.sticky

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.StickyAdapter

/**
 * [2018] by Zone
 */
class StickyOnScrollListener<T>(private val vpShow: FrameLayout, val adapter: StickyAdapter<T>) : RecyclerView.OnScrollListener() {
    internal var placeColor = Color.TRANSPARENT
    private var preStickyIndex = -1
    private var placeholderView: View? = null
    internal var mNowStickyViewHolder: StickyChildHolder? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (recyclerView == null) return
    }

    fun setPlaceColor(@ColorInt color: Int) {
        this.placeColor = color
    }

    //todo  如果类型不同咋办？
    private fun restorePreSticky(recyclerView: RecyclerView) {
        QuickConfig.e("吸附-》还原了之前那个位置：" + preStickyIndex)
        val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(preStickyIndex)
        if (shouldViewHolder != null && shouldViewHolder is StickyHolder) {

            if(shouldViewHolder.itemViewType==mNowStickyViewHolder!!.viewTypeInner){
                //找到占位view 移除父亲  甭管他有没有
                removeParent(placeholderView!!)
                //吧sticky设置成当前的
                shouldViewHolder.stickyChildHolder = mNowStickyViewHolder!!
                shouldViewHolder.delegateView = mNowStickyViewHolder!!.itemView

                shouldViewHolder.delegateView.translationY = 0F
                (shouldViewHolder.itemView as FrameLayout).addView(shouldViewHolder.delegateView)
            }
        }
        mNowStickyViewHolder = null
    }


    @Synchronized
    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (recyclerView == null) return
        //为了 变换的时候不影响这次计算 
        val stickyList = adapter.stickyList
        var pos = 0
        try {
            //GridLayoutManager 继承LinearLayoutManager 所以也支持GridLayoutManager
            pos = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            QuickConfig.e("吸附-> findFirstVisibleItemPosition：$pos")
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalStateException("仅仅支持继承LinearLayoutManager与GridLayoutManager的布局!")
        }

        val shouShowStickyPos = findShowPos(pos, recyclerView, stickyList)
        //找到对应的 数组位置  用他去找view

        if (shouShowStickyPos == -1) { //没找到应该显示的话
            if (preStickyIndex != -1) { //之前还有头部显示的话   不显示！
                QuickConfig.e("吸附-》未找到吸附位置：重置占位头")
                restorePreSticky(recyclerView)
                preStickyIndex = -1
            }
        } else {
            if (placeholderView == null) {
                placeholderView = View(recyclerView.context)
                placeholderView!!.setBackgroundColor(placeColor)
            }

            if (preStickyIndex != shouShowStickyPos) {
                //如果当前吸附的与吸附的判断是一样的话 跳过 因为已经设置过了,或者preStickyIndex=-1代表之前没有设置过
                //找到之前那个itemView 还原了
                if (preStickyIndex != -1 && mNowStickyViewHolder != null) restorePreSticky(recyclerView)

                QuickConfig.e("吸附-> 吸附位置:" + shouShowStickyPos + "狸猫换太子")
                //ItemView的狸猫换太子 显示在VP中
                //todo  如果找不到  就自己创建   找到 ,则他创建
                //recyclerView.mRecycler.getViewForPosition(13)
                val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(shouShowStickyPos)
                // 注意  上面 ，下面  ；  上下 可交换
                if (shouldViewHolder == null) { //没有  自己创建
                    val viewType = adapter.getItemViewType(shouShowStickyPos)
                    var delegeteItemView: View?
                    for (entry in adapter.stickyViewTypeMap.entries) {
                        if (entry.value == viewType) {
                            val delegate = adapter.delegatesManager.getDelegate(entry.key) //原始viewtype生成对应的delegate
                            if (delegate == null) throw IllegalStateException(" No delegate found for item ")
                            val viewHolder = StickyChildHolder(delegate.onCreateViewHolder(vpShow).itemView, entry.key)
                            delegeteItemView = viewHolder.itemView
                            vpShow.addView(delegeteItemView)
                            placeholderView!!.layoutParams.height = delegeteItemView.getHeight()
                            mNowStickyViewHolder = viewHolder
                            //viewHolder 是StickyChildHolder  而不是StickHolder所以不会被移除parent 仅仅是进行绑定
                            adapter.onBindViewHolder(viewHolder, shouShowStickyPos, null)
                        }
                    }
                } else {
                    //有  替换
                    if (shouldViewHolder is StickyHolder) {
                        placeholderView!!.layoutParams = shouldViewHolder.delegateView.layoutParams
                        removeParent(placeholderView!!)
                        (shouldViewHolder.itemView as FrameLayout).addView(placeholderView!!)

                        removeParent(shouldViewHolder.delegateView)
                        vpShow.addView(shouldViewHolder.delegateView)
                        mNowStickyViewHolder = shouldViewHolder.stickyChildHolder
                    }
                }
                preStickyIndex = shouShowStickyPos
            }

            // 位移计算!
            if (shouShowStickyPos != stickyList[stickyList.size - 1].posi) {//最后的那个不进行位移
                val next = stickyList[shouShowStickyPos + 1].posi
                val stickyView = mNowStickyViewHolder!!.itemView
                if (stickyView != null) {
                    if (recyclerView.findViewHolderForLayoutPosition(next) == null)
                        stickyView.translationY = 0f
                    else {
                        val targetView = recyclerView.findViewHolderForLayoutPosition(next).itemView
                        if (targetView.top <= stickyView.height)
                            stickyView.translationY = (targetView.top - stickyView.height).toFloat()
                    }
                }

            }

        }
    }

    private fun removeParent(view: View) {
        val vp = view.parent
        if (vp != null) (vp as ViewGroup).removeView(view)
    }

    // 3,6,9为例  [0,3)show 无 ,[3,6)show 3,[6,9)show 6,>=9 show 9
    //等于时候要判断 getTop<=0 是等于的那个 头。不然 是上一个头
    private fun findShowPos(pos: Int, recyclerView: RecyclerView, stickyList: ArrayList<StickyAdapter.StickyEntity<T>>): Int {
        // if (itemViews[i - 1].getTop() <= 0) {//注意:getTop不包括 dector
        for (i in 0 until stickyList.size) {
            if (pos < stickyList[i].posi) {
                return if (i == 0) -1 else stickyList[i - 1].posi
            } else if (pos == stickyList[i].posi) {
                val firstHolder = recyclerView.findViewHolderForLayoutPosition(pos)
                if (firstHolder.itemView.top < 0) return stickyList[i].posi
                else return if (i == 0) -1 else stickyList[i - 1].posi
            }
        }
        // 如果posi 大于 列表中最后一个值
        return stickyList[stickyList.size - 1].posi
    }
}