package com.zone.adapter3kt.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.Part
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.loadmore.OnScrollRcvListener
import java.util.concurrent.atomic.AtomicBoolean

/**
 *[2018] by Zone
 *
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
        set(value) {
            field = value
            if (value != null) {
                recyclerView?.apply {
                    addLoadMoreScrollListener(this)
                }
            }
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        addLoadMoreScrollListener(recyclerView)
    }

    private fun addLoadMoreScrollListener(recyclerView: RecyclerView) {
        if (loadOnScrollListener != null && delegatesManager.getDelegateNoMap(LOADING_VALUE) != null && !hasLoadScrollListener) {
            loadOnScrollListener?.let {
                it.setting = loadingSetting
                recyclerView.addOnScrollListener(it)
                hasLoadScrollListener = true
            }
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
    internal var isLoading = AtomicBoolean(false)

    internal fun loading() {
        isLoading.set(true)
        addLoadData()
        loadOnScrollListener?.onLoading()
        getLoadDelegate { it.loading() }
    }

    /**
     * 同时也是 reset状态
     */
    fun loadMoreComplete() {
        if (isLoading.get()) {
            removeLoadData()
            loadOnScrollListener?.complete()
            isLoading.set(false)
            getLoadDelegate { it.complete() }
        } else QuickConfig.d("非loading状态")
    }

    fun loadMoreFail() {
        if (isLoading.get()) {
            addLoadData()
            loadOnScrollListener?.fail()
            isLoading.set(false)
            getLoadDelegate { it.fail() }
        } else QuickConfig.d("非loading状态")
    }

    fun loadMoreEnd() {
        if (isLoading.get()) {
            addLoadData()
            loadOnScrollListener?.end()
            isLoading.set(false)
            getLoadDelegate { it.end() }
        } else QuickConfig.d("非loading状态")
    }

}