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
 *
 * todo 永久储藏？
 */
class StickyOnScrollListener<T>(private val vpShow: FrameLayout, val adapter: StickyAdapter<T>,@ColorInt val placeColor: Int = Color.TRANSPARENT) : RecyclerView.OnScrollListener() {
    internal var preStickyIndex = -1
    private var placeholderView: View? = null
    internal var mNowStickyViewHolder: StickyChildHolder? = null

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (recyclerView == null) return
    }

    private fun releaseNowShowSticky(recyclerView: RecyclerView) {
        //release释放的时候 如果在 对应position位置找到的话 ,替换 . 找不到 制空不管即可
        QuickConfig.e("释放之前位置的view：" + preStickyIndex)
        val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(preStickyIndex)
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
        preStickyIndex = -1
    }

    fun childIsPlaceholderView(vp: FrameLayout): Boolean {
        return if (placeholderView != null && placeholderView!!.parent != null && placeholderView!!.parent == vp) true else false
    }

    fun adapterAddPlaceHolder(holder:StickyHolder){
        if(mNowStickyViewHolder!=null){
            removeParent(placeholderView!!)
            placeholderView!!.layoutParams = mNowStickyViewHolder!!.itemView.layoutParams
            placeholderView!!.layoutParams.height = mNowStickyViewHolder!!.itemView.height
            (holder.itemView as FrameLayout).addView(placeholderView!!)
        }
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
            if (preStickyIndex != -1) { //之前还有头部显示的话   不显示！
                QuickConfig.e("释放掉 外层显示的view")
                releaseNowShowSticky(recyclerView)
            }
        } else {
            if (placeholderView == null) {
                placeholderView = View(recyclerView.context)
                placeholderView!!.setBackgroundColor(placeColor)
            }

            if (preStickyIndex != shouldShowStickyPos) { //如果之前外层显示的与当前显示的pos不一样就更换

                //如果之前外层没有显示 就是-1的话就不用释放掉了  因为-1的时候已经释放过了
                if (preStickyIndex != -1 && mNowStickyViewHolder != null) releaseNowShowSticky(recyclerView)

                QuickConfig.e("吸附-> 吸附位置:" + shouldShowStickyPos + "狸猫换太子")
                //ItemView的狸猫换太子 显示在VP中
                val shouldViewHolder = recyclerView.findViewHolderForLayoutPosition(shouldShowStickyPos)
                if (shouldViewHolder == null) { //没有  自己创建 ,而不用空白占位
                    val viewType = adapter.getItemViewType(shouldShowStickyPos)
                    var delegeteItemView: View?
                    for (entry in adapter.stickyViewTypeMap.entries) {
                        if (entry.value == viewType) {
                            val delegate = adapter.delegatesManager.getDelegateNoMap(entry.key) //原始viewtype生成对应的delegate
                            if (delegate == null) throw IllegalStateException(" No delegate found for item ")
                            val viewHolder = StickyChildHolder(delegate.onCreateViewHolder(vpShow).itemView, entry.key)
                            delegeteItemView = viewHolder.itemView
                            vpShow.addView(delegeteItemView)
                            placeholderView!!.layoutParams = delegeteItemView.layoutParams
                            mNowStickyViewHolder = viewHolder
                            //viewHolder 是StickyChildHolder  而不是StickHolder所以不会被移除parent 仅仅是进行绑定
                            adapter.onBindViewHolder(viewHolder, shouldShowStickyPos, null)
                        }
                    }
                } else { //有的话 用空白  替换
                    if (shouldViewHolder is StickyHolder) {
                        //安全使用 空白站位View
                        removeParent(placeholderView!!)
                        placeholderView!!.layoutParams = shouldViewHolder.stickyChildHolderItemView.layoutParams
                        placeholderView!!.layoutParams.height = shouldViewHolder.stickyChildHolderItemView.height
                        (shouldViewHolder.itemView as FrameLayout).addView(placeholderView!!)

                        removeParent(shouldViewHolder.stickyChildHolderItemView)
                        vpShow.addView(shouldViewHolder.stickyChildHolderItemView)
                        mNowStickyViewHolder = shouldViewHolder.stickyChildHolder
                    }
                }
                preStickyIndex = shouldShowStickyPos
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
                        else print("what?")
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
    private fun findShowPos(pos: Int, recyclerView: RecyclerView, stickyList: ArrayList<StickyAdapter.StickyEntity<T>>): ShowPos {
        // if (itemViews[i - 1].getTop() <= 0) {//注意:getTop不包括 dector
        for (i in 0 until stickyList.size) {
            if (pos < stickyList[i].posi) {
                return if (i == 0) ShowPos(-1, 0) else ShowPos(stickyList[i - 1].posi, i)
            }
//            else if (pos == stickyList[i].posi) {
//                val firstHolder = recyclerView.findViewHolderForLayoutPosition(pos)
//                if (firstHolder.itemView.top < 0) return stickyList[i].posi
//                else return if (i == 0) -1 else stickyList[i - 1].posi
//            }
        }
        // 如果posi 大于 列表中最后一个值
        return ShowPos(stickyList[stickyList.size - 1].posi, stickyList.size - 1)
    }

    data class ShowPos(val shouldPos: Int, val nextStickyIndex: Int)
}