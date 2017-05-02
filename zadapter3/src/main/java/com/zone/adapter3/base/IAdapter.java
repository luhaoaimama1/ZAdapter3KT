package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.loadmore.OnScrollRcvListener;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;

import java.util.List;

/**
 * Generic adapter must implement the interface, as a method of unified standard
 */
public interface IAdapter<T> {

    // =======================================
    // ============baseMethod ==============
    // =======================================
    List<T> getData();

    int getRealItemCount();

    Context getContext();
    // =======================================
    // ============Listener ==============
    // =======================================

    IAdapter setOnItemClickListener(OnItemClickListener listener);

    OnItemClickListener getOnItemClickListener();

    IAdapter setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener);

    OnItemLongClickListener getOnItemLongClickListener();

    interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View view, int position);
    }

    interface OnItemLongClickListener {
        boolean onItemLongClick(ViewGroup parent, View view, int position);
    }

    // =======================================
    // ============RecyclerView方法==============
    // =======================================

    IAdapter addOnScrollListener(OnScrollRcvListener listener);

    OnScrollRcvListener getOnScrollListener();

    // =======================================
    // ============加载更多==============
    // =======================================

    IAdapter loadMoreComplete();

    IAdapter loadMoreFail();

    IAdapter end();

    /**
     * 重footerDelegates清除LoadMoreDelegates
     * 不通知监听
     *
     * @return
     */
    IAdapter clearLoadMoreDelegates();


    // =======================================
    // ============ header/footer ==============
    // =======================================

    IAdapter removeHeaderHolder(ViewDelegates header);

    IAdapter removeFooterHolder(ViewDelegates footer);

    IAdapter clearHeaderHolder();

    IAdapter clearFooterHolder();

    boolean containHeaderHolder(ViewDelegates header);

    boolean containFooterHolder(ViewDelegates footer);

    IAdapter addEmptyHold(@LayoutRes int layout);

    IAdapter addEmptyHold(ViewDelegates footer);

    IAdapter addFooterHolder(@LayoutRes int layout);

    IAdapter addFooterHolder(ViewDelegates footer);

    IAdapter setLoadFooterViewHold(@LayoutRes int layout);

    IAdapter setLoadFooterViewHold(ILoadMoreDelegates loadFooterView);

    IAdapter addHeaderHolder(@LayoutRes int layout);

    IAdapter addHeaderHolder(ViewDelegates header);

    IAdapter addViewHolder(int style, ViewDelegates viewDelegates);

    IAdapter addViewHolder(ViewDelegates viewDelegates);

    IAdapter relatedList(RecyclerView mRecyclerView);

    int getHeaderViewsCount();

    int getFooterViewsCount();

    // =======================================
    // ============ notify ==============
    // =======================================

    void notifyDataSetChanged();

    void notifyItemChanged(int position);

    void notifyItemInserted(int position);

    void notifyItemRemoved(int position);

    void notifyItemRangeChanged(int positionStart, int itemCount);

    void notifyItemRangeInserted(int positionStart, int itemCount);

    void notifyItemRangeRemoved(int positionStart, int itemCount);


    void notifyItemChangedEx(int dataPosition);

    void notifyItemInsertedEx(int dataPosition);

    void notifyItemRemovedEx(int dataPosition);

    void notifyItemRangeChangedEx(int dataPositionStart, int itemCount);

    void notifyItemRangeInsertedEx(int dataPositionStart, int itemCount);

    void notifyItemRangeRemovedEx(int dataPositionStart, int itemCount);


    // =======================================
    // ============adapter原生方法 ==============
    // =======================================
    int getItemCount();

    int getItemViewType(int position);

    void onBindViewHolder(Holder holder, int position, List<Object> payloads);

    void setHasStableIds(boolean hasStableIds);

    long getItemId(int position);

    void onViewRecycled(Holder holder);

    boolean onFailedToRecycleView(Holder holder);

    void onViewAttachedToWindow(Holder holder);

    void onViewDetachedFromWindow(Holder holder);

    void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer);

    void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer);

    void onAttachedToRecyclerView(RecyclerView recyclerView);

    void onDetachedFromRecyclerView(RecyclerView recyclerView);

    Holder onCreateViewHolder(ViewGroup parent, int viewType);

    void onBindViewHolder(Holder holder, int position);

}