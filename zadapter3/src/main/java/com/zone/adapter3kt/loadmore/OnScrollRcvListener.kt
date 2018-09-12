package com.zone.adapter3kt.loadmore

import android.support.v7.widget.RecyclerView
import com.zone.adapter3.QuickConfig
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.zone.adapter3kt.adapter.LoadMoreAdapter

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
        if (setting.checkLoadMoreMode == CheckLoadMoreMode.SCROLL_STATE_IDLE &&
            newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            loadMoreCheck(recyclerView)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        // 只有在闲置状态情况下检查
        if (setting.checkLoadMoreMode == CheckLoadMoreMode.SCROLL_STATE_DRAGGING)
            loadMoreCheck(recyclerView)
    }


    private fun loadMoreCheck(recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter
        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (adapter == null || adapter.itemCount == 0) return
        getFirstLastPos(recyclerView)
        // isCanLoadMore2isRest 能加载更多 并且 状态是处于休息的时候
        // 如果总共个数-浏览过的数据 小于 阈值的量 。就去加载更多
        //代表adapter.itemCount-1 ：是因为 itemCount从1开始 应该和Pos都从0开始才好
        if (isCanLoadMore2isRest(recyclerView)) {
            //Tips: 为啥要分开写 因为SCROLL_STATE_IDLE 还有非线性 grid那种模式的时候 可能会跨过等于 直接小于的问题
            if (adapter.itemCount - 1 - lastVisiblePos == setting.threshold) { //等于的时候必须贴底
                //这么写  debug好测~
                // 没铺满或者 最后一项可见并不是贴底的话 都不算 贴底代表 itemView.bottom==recyclerView.bottom
                val viewHolder = recyclerView.findViewHolderForLayoutPosition(lastVisiblePos)
                if (viewHolder != null && viewHolder.itemView.bottom == recyclerView.bottom) {
                    QuickConfig.e("OnScrollRcvListener---->loadMore")
                    loadMore(recyclerView)
                }
            } else if (adapter.itemCount - 1 - lastVisiblePos < setting.threshold) {
                //这么写  debug好测~
                val viewHolder = recyclerView.findViewHolderForLayoutPosition(lastVisiblePos)
                if (viewHolder != null && viewHolder.itemView.bottom >= recyclerView.bottom) {
                    QuickConfig.e("OnScrollRcvListener---->loadMore")
                    loadMore(recyclerView)
                }
            }
        }
    }

    private fun getFirstLastPos(recyclerView: RecyclerView) {
        when (recyclerView.layoutManager) {
            is LinearLayoutManager -> {
                firstVisiblePos = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                lastVisiblePos = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            is GridLayoutManager -> {
                firstVisiblePos = (recyclerView.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                lastVisiblePos = (recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                var firstVisibleItems: IntArray? = null
                var lastVisibleItems: IntArray? = null
                firstVisibleItems = (recyclerView.layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstVisibleItems)
                lastVisibleItems = (recyclerView.layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(lastVisibleItems)
                if (firstVisibleItems != null && firstVisibleItems.size > 0) {
                    firstVisiblePos = firstVisibleItems[0]
                }
                if (lastVisibleItems != null && lastVisibleItems.size > 0) {
                    lastVisiblePos = lastVisibleItems[0]
                }
            }
            else -> throw IllegalStateException("不支持其他布局")
        }
    }

    //能加载更多 并且处于rest状态  主要是为了兼容其他的刷新控件
    protected open fun isCanLoadMore2isRest(recyclerView: RecyclerView): Boolean = true

    //加载状态 已经帮你滚动好了
    open fun loadMore(recyclerView: RecyclerView) {
        if (recyclerView.adapter is LoadMoreAdapter<*>) {
            (recyclerView.adapter as LoadMoreAdapter<*>).loading()
        }
    }

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