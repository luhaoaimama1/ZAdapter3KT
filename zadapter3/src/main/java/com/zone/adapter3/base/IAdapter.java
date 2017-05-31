package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.diff.DiffCallBack;
import com.zone.adapter3.loadmore.OnScrollRcvListener;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;
import com.zone.adapter3.manager.MarginItemDecoration;

import java.util.List;

/**
 * Generic adapter must implement the interface, as a method of unified standard
 */
public interface IAdapter<T> {

    // =======================================
    // ============baseMethod ==============
    // =======================================
    List<T> getData();

    //当时空页面的时候应该是0 所以排除空页面的
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

    RecyclerView getRecyclerView();

    IAdapter addOnScrollListener(OnScrollRcvListener listener);

    OnScrollRcvListener getOnScrollListener();

    IAdapter addItemDecoration(int space);

    IAdapter addItemDecoration(MarginItemDecoration itemDecoration);

    void scrollToData(T o);

    void scrollToPosition(int position);

    void scrollToLast();

    void scrollToHF(ViewDelegates hf);

    void smoothScrollToData(T o);

    void smoothScrollToPosition(int position);

    void smoothScrollToHF(ViewDelegates hf);

    void smoothScrollToLast();


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
    IAdapter removeLoadMoreDelegates();


    // =======================================
    // ============ header/footer ==============
    // =======================================

    IAdapter removeHeaderHolder(ViewDelegates header);

    IAdapter removeHeaderHolder(ViewDelegates header,boolean notify);

    IAdapter removeFooterHolder(ViewDelegates footer);

    IAdapter removeFooterHolder(ViewDelegates footer,boolean notify);

    IAdapter clearHeaderHolder();

    IAdapter clearFooterHolder();

    boolean containHeaderHolder(ViewDelegates header);

    boolean containFooterHolder(ViewDelegates footer);

    IAdapter addEmptyHold(@LayoutRes int layout);

    IAdapter addEmptyHold(ViewDelegates footer);

    IAdapter addFooterHolder(@LayoutRes int layout);

    IAdapter addFooterHolder(@LayoutRes int layout,boolean notify);

    IAdapter addFooterHolder(ViewDelegates footer);

    IAdapter addFooterHolder(ViewDelegates footer,boolean notify);

    IAdapter setLoadFooterViewHold(@LayoutRes int layout);

    IAdapter setLoadFooterViewHold(ILoadMoreDelegates loadFooterView);

    IAdapter addHeaderHolder(@LayoutRes int layout);

    IAdapter addHeaderHolder(@LayoutRes int layout,boolean notify);

    IAdapter addHeaderHolder(ViewDelegates header);

    IAdapter addHeaderHolder(ViewDelegates header,boolean notify);

    //允许style重复 重复的话 就覆盖之前的
    IAdapter addViewHolder(int style, ViewDelegates viewDelegates);

    IAdapter addViewHolder(ViewDelegates viewDelegates);

    IAdapter relatedList(RecyclerView mRecyclerView);

    int getHeaderViewsCount();

    int getFooterViewsCount();

    //用于动画定位 不包括空view;
    int indexOfHF(ViewDelegates item);


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

    void diffSetKeyframe();

    //后台使用
    void diffCalculate(DiffCallBack<T> diffCallBack);

    void diffNotifyDataSetChanged();

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