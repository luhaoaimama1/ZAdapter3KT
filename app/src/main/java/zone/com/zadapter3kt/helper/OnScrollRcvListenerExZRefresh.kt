package zone.com.zadapter3kt.helper

import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.loadmore.OnScrollRcvListener
import zone.com.zrefreshlayout.AUtils
import zone.com.zrefreshlayout.ZRefreshLayout

/**
 * 继承此类关注 这三个方法
 * [.loadMoreComplete]
 * [.loadMoreFail]
 * [.loadMore]
 * [.end]
 * [.isCanLoadMore2isRest]
 */
class OnScrollRcvListenerExZRefresh(val zRefreshLayout: ZRefreshLayout) : OnScrollRcvListener() {

    private fun zrefreshLoadComplete() {
        if (zRefreshLayout.isLoadMore) {
            //一般是通知footerView动画去complete
            zRefreshLayout.loadMoreComplete()
            //因为被委托了 只好自己去走footerView里的onComplete该做的事情了  通知footerView动画完成
            AUtils.notifyLoadMoreCompleteListener(zRefreshLayout)
        }
    }

    override fun isCanLoadMore2isRest(recyclerView: androidx.recyclerview.widget.RecyclerView): Boolean =
        zRefreshLayout.isCanLoadMore && AUtils.isRest(zRefreshLayout)

    override fun loadMore(recyclerView:RecyclerView) {
        super.loadMore(recyclerView)
        AUtils.notityLoadMoreListener(zRefreshLayout)
    }

    override fun onLoading() {
        super.onLoading()
        zrefreshLoadComplete()
    }

    override fun end() {
        super.end()
        zrefreshLoadComplete()
        zRefreshLayout.isCanLoadMore = false
    }

    override fun fail() {
        super.fail()
        zrefreshLoadComplete()
    }

    override fun complete() {
        super.complete()
        zrefreshLoadComplete()
    }
}