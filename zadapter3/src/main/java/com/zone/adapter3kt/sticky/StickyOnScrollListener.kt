package com.zone.adapter3kt.sticky

import android.graphics.Color
import android.support.annotation.ColorInt
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.adapter.StickyAdapter

/**
 * [2018] by Zone
 */
class StickyOnScrollListener<T>(private val vpShow: FrameLayout, val adapter: StickyAdapter<T>, @ColorInt val placeColor: Int = Color.TRANSPARENT) : RecyclerView.OnScrollListener() {
    internal var preShowStickyPosi = -1
    private var placeholderView: View? = null
    internal var mNowStickyViewHolder: StickyChildHolder? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (recyclerView == null) return
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

        //找到应该显示的pos
        val (shouldShowStickyPos, nextStickyIndex) = findShowPos(pos, recyclerView, stickyList)
        if (shouldShowStickyPos == -1) { //没找到应该显示的话
            if (preShowStickyPosi != -1) { //之前还有头部显示的话   不显示！
                QuickConfig.e("释放掉 外层显示的view")
                releaseNowShowSticky(recyclerView)
            }
        } else {
            if (placeholderView == null) {
                placeholderView = View(recyclerView.context)
                placeholderView!!.setBackgroundColor(placeColor)
            }

            //如果之前外层显示的与当前显示的pos不一样就更换
            if (preShowStickyPosi != shouldShowStickyPos) {

                //如果之前外层没有显示 就是-1的话就不用释放掉了  因为-1的时候已经释放过了
                if (preShowStickyPosi != -1 && mNowStickyViewHolder != null) releaseNowShowSticky(recyclerView)

                //ItemView的狸猫换太子 显示在VP中
                val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(shouldShowStickyPos)
                if (shouldViewHolder == null) { //没有  自己创建 ,而不用空白占位
                    val viewType = adapter.getItemViewType(shouldShowStickyPos)
                    val stickyChildHolderItemView: View?
                    for (entry in adapter.stickyViewTypeMap.entries) {
                        if (entry.value == viewType) {
                            val delegate = adapter.delegatesManager.getDelegateNoMap(entry.key) //原始viewtype生成对应的delegate
                            if (delegate == null) throw IllegalStateException(" No delegate found for item ")
                            val innerHolder=adapter.delegatesManager.onCreateViewHolder(vpShow, entry.value)
                            val viewHolder = StickyChildHolder(innerHolder.itemView, entry.key)
//                            val viewHolder = StickyChildHolder(delegate.onCreateViewHolder(vpShow).itemView, entry.key)
                            stickyChildHolderItemView = viewHolder.itemView
                            vpShow.addView(stickyChildHolderItemView)
                            placeholderView!!.layoutParams = stickyChildHolderItemView.layoutParams
                            mNowStickyViewHolder = viewHolder
                            //viewHolder 是StickyChildHolder  而不是StickHolder所以不会被移除parent 仅仅是进行绑定
                            adapter.onBindViewHolder(viewHolder, shouldShowStickyPos, null)
                            break
                        }
                    }
                } else { //有的话 用空白  替换
                    if (shouldViewHolder is StickyHolder) {
                        //安全使用 空白站位View
                        removeParent(placeholderView!!)
                        placeholderView!!.layoutParams = shouldViewHolder.stickyChildHolderItemView.layoutParams
                        placeholderView!!.layoutParams.height = shouldViewHolder.stickyChildHolderItemView.height
                        (shouldViewHolder.itemView as FrameLayout).addView(placeholderView!!)

                        //外层显示 从里层拿出来的viewholder
                        removeParent(shouldViewHolder.stickyChildHolderItemView)
                        vpShow.addView(shouldViewHolder.stickyChildHolderItemView)
                        mNowStickyViewHolder = shouldViewHolder.stickyChildHolder
                    }
                }
                preShowStickyPosi = shouldShowStickyPos
            }

            // 位移计算!
            if (shouldShowStickyPos != stickyList[stickyList.size - 1].posi) {//最后的那个不进行位移
                //下一个的top 和 当前show的高度进行对比
                val nextStickyView = stickyList[nextStickyIndex].posi
                val stickyView = mNowStickyViewHolder!!.itemView
                if (stickyView != null) {
                    if (recyclerView.findViewHolderForLayoutPosition(nextStickyView) == null)
                        stickyView.translationY = 0f
                    else {
                        val targetView = recyclerView.findViewHolderForLayoutPosition(nextStickyView).itemView
                        if (targetView.top <= stickyView.height) stickyView.translationY = (targetView.top - stickyView.height).toFloat()
                        else stickyView.translationY = 0f
                    }
                }
            }
        }
    }

    private fun releaseNowShowSticky(recyclerView: RecyclerView) {
        //release释放的时候 如果在 对应position位置找到的话 ,替换 . 找不到 制空不管即可

        val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(preShowStickyPosi)
        removeParent(mNowStickyViewHolder!!.itemView)

        if (shouldViewHolder != null && shouldViewHolder is StickyHolder) {
            //找到占位view 移除父亲  甭管他有没有
            removeParent(placeholderView!!)
            //因为有可能 该位置的站位 不是空白  因为如果是scrollto定位的话 往回滚。那么我仅仅在外面生成了view ,但没有用空白去替换啊
            (shouldViewHolder.itemView as ViewGroup).removeAllViews()

            //把sticky设置成当前的
            shouldViewHolder.stickyChildHolder = mNowStickyViewHolder!!
            shouldViewHolder.stickyChildHolderItemView = mNowStickyViewHolder!!.itemView
            //移除外面的view的parent
            removeParent(shouldViewHolder.stickyChildHolderItemView)

            //把外城view 设置到
            shouldViewHolder.stickyChildHolderItemView.translationY = 0F
            (shouldViewHolder.itemView as FrameLayout).addView(shouldViewHolder.stickyChildHolderItemView)
        }
        mNowStickyViewHolder = null
        preShowStickyPosi = -1
    }

    fun childIsPlaceholderView(vp: FrameLayout): Boolean {
        return if (placeholderView != null && placeholderView!!.parent != null && placeholderView!!.parent == vp) true else false
    }

    //当现在在外面显示的posi与adapter bind的posi相同的时候 adapter用占位符占位
    fun adapterAddPlaceHolder(holder:StickyHolder){
        if(mNowStickyViewHolder!=null){
            removeParent(placeholderView!!)
            placeholderView!!.layoutParams = mNowStickyViewHolder!!.itemView.layoutParams
            placeholderView!!.layoutParams.height = mNowStickyViewHolder!!.itemView.height
            (holder.itemView as FrameLayout).addView(placeholderView!!)
        }
    }

    private fun removeParent(view: View) {
        val vp = view.parent
        if (vp != null) (vp as ViewGroup).removeView(view)
    }

    // 3,6,9为例  [0,3)show 无 ,[3,6)show 3,[6,9)show 6,>=9 show 9
    private fun findShowPos(pos: Int, recyclerView: RecyclerView, stickyList: ArrayList<StickyAdapter.StickyEntity<T>>): ShowPos {
        // if (itemViews[i - 1].getTop() <= 0) {//注意:getTop不包括 dector
        for (i in 0 until stickyList.size) {
            if (pos < stickyList[i].posi) {
                return if (i == 0) ShowPos(-1, 0) else ShowPos(stickyList[i - 1].posi, i)
            }
        }
        // 如果posi 大于 列表中最后一个值
        return ShowPos(stickyList[stickyList.size - 1].posi, stickyList.size - 1)
    }

    data class ShowPos(val shouldPos: Int, val nextStickyIndex: Int)
}