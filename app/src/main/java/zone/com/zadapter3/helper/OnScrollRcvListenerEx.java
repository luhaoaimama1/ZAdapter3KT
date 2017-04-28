package zone.com.zadapter3.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.loadmore.OnScrollRcvListener;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;

/**
 * 继承此类关注 这三个方法
 * {@link #loadMoreComplete()}
 * {@link #loadMoreFail()}
 * {@link #loadMore(RecyclerView)}
 * {@link #end()}
 * {@link #isCanLoadMore2isRest(RecyclerView)}
 */
public class OnScrollRcvListenerEx extends OnScrollRcvListener {

    private LoadMoreCallback loadMoreCallback;
    private boolean end;

    public OnScrollRcvListenerEx(LoadMoreCallback loadMoreCallback) {
        this.loadMoreCallback = loadMoreCallback;
    }

    @Override
    public void loadMoreComplete() {
        super.loadMoreComplete();
    }

    @Override
    public void loadMoreFail() {
        super.loadMoreFail();
    }

    @Override
    public void end() {
        super.end();
        end=true;
    }

    @Override
    public boolean isCanLoadMore2isRest(RecyclerView recyclerView) {
        return end?false:super.isCanLoadMore2isRest(recyclerView);
    }

    @Override
    protected void loadMore(RecyclerView recyclerView) {
        super.loadMore(recyclerView);
        loadMoreCallback.loadMore();
    }

    public interface LoadMoreCallback {
        void loadMore();
    }
}