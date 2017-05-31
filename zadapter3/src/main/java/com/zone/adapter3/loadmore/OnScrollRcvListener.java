package com.zone.adapter3.loadmore;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.loadmore.callback.BaseLoadMoreDelegates;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;

/**
 * 继承此类关注 这三个方法
 * {@link #loadMoreComplete()}
 * {@link #loadMoreFail()}
 * {@link #loadMore(RecyclerView)}
 * {@link #end()}
 * {@link #isCanLoadMore2isRest(RecyclerView)}
 */
public class OnScrollRcvListener extends RecyclerView.OnScrollListener {

    public enum CheckLoadMoreMode {
        SCROLL_STATE_DRAGGING, SCROLL_STATE_IDLE
    }

    private CheckLoadMoreMode checkLoadMoreMode = CheckLoadMoreMode.SCROLL_STATE_IDLE;

    private ILoadMoreDelegates loadMoreDelegates = new BaseLoadMoreDelegates();

    protected IAdapter mQuickRcvAdapter;

    {
        loadMoreDelegates.setOnScrollRcvListener(this);
    }
    // =======================================
    // ============set/get系列==============
    // =======================================

    public IAdapter getQuickRcvAdapter() {
        return mQuickRcvAdapter;
    }

    public OnScrollRcvListener setQuickRcvAdapter(IAdapter QuickRcvAdapter) {
        this.mQuickRcvAdapter = QuickRcvAdapter;
        return this;
    }

    public ILoadMoreDelegates getLoadFooterViewHold() {
        return loadMoreDelegates;
    }

    public OnScrollRcvListener setLoadFooterViewHold(ILoadMoreDelegates iLoadFooterView) {
        this.loadMoreDelegates = iLoadFooterView;
        return this;
    }

    public CheckLoadMoreMode getCheckLoadMoreMode() {
        return checkLoadMoreMode;
    }

    public OnScrollRcvListener setCheckLoadMoreMode(CheckLoadMoreMode checkLoadMoreMode) {
        this.checkLoadMoreMode = checkLoadMoreMode;
        return this;
    }

    // =======================================
    // ============继承关注的方法==============
    // =======================================
    //加载状态 已经帮你滚动好了
    protected void loadMore(RecyclerView recyclerView) {
        if (loadMoreDelegates != null) {
            loadMoreDelegates.tryCreateView(recyclerView.getContext(), recyclerView);
            if (!mQuickRcvAdapter.containFooterHolder(loadMoreDelegates)) {
                mQuickRcvAdapter.addFooterHolder(loadMoreDelegates,true);
                mQuickRcvAdapter.scrollToHF(loadMoreDelegates);
            }
            loadMoreDelegates.loading();
        }
    }

    //移除
    public void loadMoreComplete() {
        if (mQuickRcvAdapter.containFooterHolder(loadMoreDelegates))
            mQuickRcvAdapter.removeFooterHolder(loadMoreDelegates,true);
        loadMoreDelegates.complete();
    }

    public void clearLoadMoreDelegates() {
        if (mQuickRcvAdapter.containFooterHolder(loadMoreDelegates)) {
            mQuickRcvAdapter.removeFooterHolder(loadMoreDelegates);
            mQuickRcvAdapter.notifyDataSetChanged();
        }
    }

    //数据到底部了
    public void end() {
        if (mQuickRcvAdapter.containFooterHolder(loadMoreDelegates))
            loadMoreDelegates.end();
    }

    //失败
    public void loadMoreFail() {
        if (mQuickRcvAdapter.containFooterHolder(loadMoreDelegates))
            loadMoreDelegates.fail();
    }

    //能加载更多 并且处于rest状态  主要是为了兼容其他的刷新控件
    protected boolean isCanLoadMore2isRest(RecyclerView recyclerView) {
        return true;
    }

    // =======================================
    // ============滚动原生方法==============
    // =======================================
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (checkLoadMoreMode) {
            case SCROLL_STATE_DRAGGING:
                // 只有在闲置状态情况下检查
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                    loadMoreNotify(recyclerView);
                break;
            case SCROLL_STATE_IDLE:
                // 只有在闲置状态情况下检查
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    loadMoreNotify(recyclerView);
                break;
        }
    }

    private void loadMoreNotify(RecyclerView recyclerView) {
        // 如果满足允许上拉加载、非加载状态中、最后一个显示的 item 与数据源的大小一样，则表示滑动入底部
        if (isCanLoadMore2isRest(recyclerView) && !isFirstItemVisible(recyclerView) && isLastItemVisible(recyclerView)) {
            QuickConfig.e("OnScrollRcvListener---->loadMore");
            loadMore(recyclerView);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    // =======================================
    // ============判断加载更多的帮助方法==============
    // =======================================

    /**
     * 判断第一个条目是否完全可见
     *
     * @param recyclerView
     * @return
     */
    private boolean isFirstItemVisible(RecyclerView recyclerView) {
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        // 如果未设置Adapter或者Adapter没有数据可以下拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }
        // 第一个条
        if (getFirstVisiblePosition(recyclerView) == 0) {
            return recyclerView.getChildAt(0).getTop() >= 0;
        }
        return false;
    }

    /**
     * 获取第一个可见子View的位置下标
     *
     * @param recyclerView
     * @return
     */
    private int getFirstVisiblePosition(RecyclerView recyclerView) {
        View firstVisibleChild = recyclerView.getChildAt(0);
        return firstVisibleChild != null ?
                recyclerView.getChildAdapterPosition(firstVisibleChild) : -1;
    }

    /**
     * 判断最后一个条目是否完全可见
     *
     * @param recyclerView
     * @return
     */
    private boolean isLastItemVisible(RecyclerView recyclerView) {
        final RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
        // 如果未设置Adapter或者Adapter没有数据可以上拉刷新
        if (null == adapter || adapter.getItemCount() == 0) {
            return true;
        }
        // 最后一个条目View完全展示,可以刷新
        int lastVisiblePosition = getLastVisiblePosition(recyclerView);
        if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
            return recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom()
                    <= recyclerView.getBottom();
        }
        return false;
    }

    /**
     * 获取最后一个可见子View的位置下标
     *
     * @param recyclerView
     * @return
     */
    private int getLastVisiblePosition(RecyclerView recyclerView) {
        View lastVisibleChild = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        return lastVisibleChild != null ? recyclerView.getChildAdapterPosition(lastVisibleChild) : -1;
    }
}