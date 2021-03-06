package com.zone.adapter3kt.loadmore

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.adapter.LoadMoreAdapter
import com.zone.adapter3kt.utils.getFirstLastPosrecyclerView

enum class CheckLoadMoreMode { SCROLL_STATE_DRAGGING, SCROLL_STATE_IDLE }

enum class LoadingState { NO_SHOW, LOADING, END, COMPLETE, FAIL }

class LoadingSetting {
    //从 0 开始  所以他 4的时候代表的是倒数第五项,非0 则代表预加载
    var threshold = 4
    //    var threshold = 0
//    var checkLoadMoreMode = CheckLoadMoreMode.SCROLL_STATE_IDLE
    var checkLoadMoreMode = CheckLoadMoreMode.SCROLL_STATE_DRAGGING
    var isScrollToLoadData = false
}

interface OnLoadingListener {
    fun onLoading()
    fun end()
    fun fail()
    fun complete()
}

/**
 * 继承此类关注 这个方法
 * [.isCanLoadMore2isRest]
 */
open class OnScrollRcvListener() : RecyclerView.OnScrollListener(), OnLoadingListener {

    internal var loadingStateInner = LoadingState.NO_SHOW
    var setting: LoadingSetting = LoadingSetting()
    var firstVisiblePos = -1
    var lastVisiblePos = -1
    var recyclerView: RecyclerView? = null

    // ===========================滚动原生方法========================================

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        this@OnScrollRcvListener.recyclerView = recyclerView
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            loadMoreCheck(recyclerView, 0)
        }

//        if (setting.checkLoadMoreMode == CheckLoadMoreMode.SCROLL_STATE_IDLE &&
//            newState == RecyclerView.SCROLL_STATE_IDLE
//        ) {
//            loadMoreCheck(recyclerView,0)
//        }
//        有bug:下拉到底 在刷新  结果到底部了！
//        if (setting.checkLoadMoreMode == CheckLoadMoreMode.SCROLL_STATE_DRAGGING&&
//            newState == RecyclerView.SCROLL_STATE_IDLE)
//            loadMoreCheck(recyclerView,0)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 只有在闲置状态情况下检查
        if (setting.checkLoadMoreMode == CheckLoadMoreMode.SCROLL_STATE_DRAGGING)
            loadMoreCheck(recyclerView, dy)
    }

    private val outRect = Rect()
    private fun loadMoreCheck(recyclerView: RecyclerView, dy: Int) {
        //<0 刷新动作  0是onScrollStateChanged 传过来所以包括0
        if (dy <= 0) return
        val adapter = if (recyclerView.adapter is LoadMoreAdapter<*>) {
            recyclerView.adapter as LoadMoreAdapter<*>
        } else null

        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (adapter == null || adapter.itemCount == 0) return
//        getFirstLastPos(recyclerView)
        val pair = recyclerView.getFirstLastPosrecyclerView()
        firstVisiblePos = pair.first
        lastVisiblePos = pair.second
        // isCanLoadMore2isRest 能加载更多 并且 状态是处于休息的时候
        // 如果总共个数-浏览过的数据 小于 阈值的量 。就去加载更多
        //代表adapter.itemCount-1 ：是因为 itemCount从1开始 应该和Pos都从0开始才好
        if (adapter.loadOnScrollListener != null && isCanLoadMore2isRest(recyclerView)) {
            //Tips: 为啥要分开写 因为SCROLL_STATE_IDLE 还有非线性 grid那种模式的时候 可能会跨过等于 直接小于的问题
            if (adapter.itemCount - 1 - lastVisiblePos == setting.threshold) { //等于的时候必须贴底
                //这么写  debug好测~
                // 没铺满或者 最后一项可见并不是贴底的话 都不算 贴底代表 itemView.bottom==recyclerView.bottom
                val viewHolder = viewHolder2ItemDecoration(recyclerView)
                if (viewHolder != null && viewHolder.itemView.bottom == recyclerView.bottom - outRect.bottom) {
                    QuickConfig.e("OnScrollRcvListener---->loadMore")
                    loadMore(recyclerView)
                }
            } else if (adapter.itemCount - 1 - lastVisiblePos < setting.threshold) {
                //这么写  debug好测~
                val viewHolder = viewHolder2ItemDecoration(recyclerView)
                if (viewHolder != null && viewHolder.itemView.bottom >= recyclerView.bottom - outRect.bottom) {
                    QuickConfig.e("OnScrollRcvListener---->loadMore")
                    loadMore(recyclerView)
                }
            }
        }
    }

    private fun viewHolder2ItemDecoration(recyclerView: RecyclerView): RecyclerView.ViewHolder? {
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(lastVisiblePos)
        outRect.set(0, 0, 0, 0)
        if (viewHolder != null) {
            recyclerView.layoutManager?.calculateItemDecorationsForChild(viewHolder.itemView, outRect)
        }
        return viewHolder
    }

    //能加载更多 并且处于rest状态  主要是为了兼容其他的刷新控件
    protected open fun isCanLoadMore2isRest(recyclerView: RecyclerView): Boolean = loadingStateInner == LoadingState.NO_SHOW ||
            loadingStateInner == LoadingState.COMPLETE ||
            loadingStateInner == LoadingState.FAIL

    //加载状态 已经帮你滚动好了
    open fun loadMore(recyclerView: RecyclerView) {
        if (recyclerView.adapter is LoadMoreAdapter<*>) {
            (recyclerView.adapter as LoadMoreAdapter<*>).loading()
        }
    }

    fun getLoadingState() = loadingStateInner

    override fun onLoading() {
        loadingStateInner = LoadingState.LOADING
    }

    override fun end() {
        loadingStateInner = LoadingState.END
    }

    override fun fail() {
        loadingStateInner = LoadingState.FAIL
    }

    override fun complete() {
        loadingStateInner = LoadingState.COMPLETE
    }
}