package zone.com.zadapter3.helper;

import android.support.v7.widget.RecyclerView;

import com.zone.adapter3.loadmore.OnScrollRcvListener;

import zone.com.zrefreshlayout.AUtils;
import zone.com.zrefreshlayout.ZRefreshLayout;

/**
 * 继承此类关注 这三个方法
 * {@link #loadMoreComplete()}
 * {@link #loadMoreFail()}
 * {@link #loadMore(RecyclerView)}
 * {@link #end()}
 * {@link #isCanLoadMore2isRest(RecyclerView)}
 */
public class OnScrollRcvListenerExZRefresh extends OnScrollRcvListener {

    private ZRefreshLayout zRefreshLayout;

    public OnScrollRcvListenerExZRefresh(ZRefreshLayout zRefreshLayout) {
        this.zRefreshLayout = zRefreshLayout;
    }


    @Override
    public void loadMoreComplete() {
        super.loadMoreComplete();
        zrefreshComplete();
    }

    private void zrefreshComplete() {
        //一般是通知footerView动画去complete
        zRefreshLayout.loadMoreComplete();
        //因为被委托了 只好自己去走footerView里的onComplete该做的事情了  通知footerView动画完成
        AUtils.notifyLoadMoreCompleteListener(zRefreshLayout);
    }

    @Override
    public void loadMoreFail() {
        super.loadMoreFail();
        zrefreshComplete();
    }

    @Override
    public void end() {
        super.end();
        if(zRefreshLayout.isRefresh())
            zRefreshLayout.refreshComplete();
        else
            zrefreshComplete();
    }

    @Override
    public boolean isCanLoadMore2isRest(RecyclerView recyclerView) {
        return zRefreshLayout.isCanLoadMore() && AUtils.isRest(zRefreshLayout);
    }

    @Override
    protected void loadMore(RecyclerView recyclerView) {
        super.loadMore(recyclerView);
        AUtils.notityLoadMoreListener(zRefreshLayout);
    }

}