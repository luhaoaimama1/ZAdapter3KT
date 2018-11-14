package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.Part
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.loadmore.OnLoadingListener
import com.zone.adapter3kt.loadmore.OnScrollRcvListener

/**
 *[2018] by Zone
 */
abstract class OnLoadingAdapterListener : OnLoadingListener {
    override fun end() {}
    override fun fail() {}
    override fun complete() {}
}

/**
 * 这个与数据 独立出来。 这里的加载更多 与 empty一样用占位控制
 */
open class LoadMoreAdapter<T>(context: Context) : ScrollToAdapter<T>(context) {


    val loadData by lazy {
        val dataWarpTemp = DataWarp<T>(null)
        dataWarpTemp.extraConfig.viewStyle = LOADING_VALUE
        dataWarpTemp.extraConfig.part = Part.OTHER
        dataWarpTemp.extraConfig.isFullspan = true
        dataWarpTemp
    }

    private var hasLoadScrollListener: Boolean = false
    var loadDelegate: LoadMoreViewDelegate? = null

    internal var onLoadingListener: OnLoadingAdapterListener? = null

    private fun getLoadDelegate(method: (LoadMoreViewDelegate) -> Unit) {
        if (loadDelegate != null) method(loadDelegate!!)
        else {
            val delegate2 = delegatesManager.getDelegateNoMap(LOADING_VALUE)
            if (delegate2 != null && delegate2 is LoadMoreViewDelegate) {
                loadDelegate = delegate2
                method(delegate2)
            }
        }
    }

    var loadOnScrollListener: OnScrollRcvListener? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (delegatesManager.getDelegateNoMap(LOADING_VALUE) != null && !hasLoadScrollListener) {
            if (loadOnScrollListener == null) loadOnScrollListener = OnScrollRcvListener()
            loadOnScrollListener!!.setting = loadingSetting
            recyclerView.addOnScrollListener(loadOnScrollListener)
            hasLoadScrollListener = true
        }
    }

    internal fun addLoadData() {
        if (mHFList.otherDatas.indexOf(loadData) == -1) {
            mHFList.otherDatas.add(loadData)
            notifyItemInserted(itemCount)
            if (loadingSetting.isScrollToLoadData) scrollToLast()
        }
    }

    internal fun removeLoadData() {
        if (mHFList.otherDatas.indexOf(loadData) != -1) {
            mHFList.otherDatas.remove(loadData)
            notifyItemRemoved(itemCount)
        }
    }

    //加载更多是 触发的 所以是滚动来触发  剩下的都是主动通知的
    var isLoading = false

    internal fun loading() {
        addLoadData()
        loadOnScrollListener?.onLoading()
        onLoadingListener?.onLoading()
        isLoading = true
        getLoadDelegate { it.loading() }
    }

    fun loadMoreComplete() {
        if (isLoading) {
            removeLoadData()
            isLoading = false
            loadOnScrollListener?.complete()
            onLoadingListener?.complete()
            getLoadDelegate { it.complete() }
        } else QuickConfig.d("非loading状态")
    }

    fun loadMoreFail() {
        if (isLoading) {
            addLoadData()
            isLoading = false
            loadOnScrollListener?.fail()
            onLoadingListener?.fail()
            getLoadDelegate { it.fail() }
        } else QuickConfig.d("非loading状态")
    }

    fun loadMoreEnd() {
        if (isLoading) {
            addLoadData()
            isLoading = false
            loadOnScrollListener?.end()
            onLoadingListener?.end()
            getLoadDelegate { it.end() }
        } else QuickConfig.d("非loading状态")
    }

}