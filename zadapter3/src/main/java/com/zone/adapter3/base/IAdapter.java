package com.zone.adapter3.base;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.diff.DiffCallBack;
import com.zone.adapter3.loadmore.OnScrollRcvListener;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;
import com.zone.adapter3.decoration.MarginItemDecoration;

import java.util.List;

/**
 * Generic adapter must implement the interface, as a method of unified standard
 */
public interface IAdapter<T> extends  IAdapterProxy.UnActive<Holder>
        ,IAdapterProxy.Active<Holder> {

    // =======================================
    // ============baseMethod ==============
    // =======================================
    List<T> getData();


    //获取ContentCount+headerCount+FooterCount
    int getCHFItemCount();

    int getContentCount();

    Context getContext();
    // =======================================
    // ============Listener ==============
    // =======================================

    IAdapter<T> setOnItemClickListener(OnItemClickListener listener);

    OnItemClickListener getOnItemClickListener();

    IAdapter<T> setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener);

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

    IAdapter<T> addOnScrollListener(OnScrollRcvListener listener);

    OnScrollRcvListener getOnScrollListener();

    IAdapter<T> addItemDecoration(int space);

    IAdapter<T> addItemDecoration(MarginItemDecoration itemDecoration);

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

    IAdapter<T> loadMoreComplete();

    IAdapter<T> loadMoreFail();

    IAdapter<T> end();

    /**
     * 重footerDelegates清除LoadMoreDelegates
     * 不通知监听
     *
     * @return
     */
    IAdapter<T> removeLoadMoreDelegates();


    // =======================================
    // ============ header/footer ==============
    // =======================================

    IAdapter<T> addSticky(FrameLayout vpShow, int... stickyPostions);

    IAdapter<T> addSticky(@ColorInt int color,FrameLayout vpShow, int... stickyPostions);

    IAdapter<T> setContentDataMapListener(Header2FooterRcvAdapter.ContentDataMapListener contentDataMapListener);

    IAdapter<T> removeHeaderHolder(ViewDelegates header);

    IAdapter<T> removeHeaderHolder(ViewDelegates header,boolean notify);

    IAdapter<T> removeFooterHolder(ViewDelegates footer);

    IAdapter<T> removeFooterHolder(ViewDelegates footer,boolean notify);

    IAdapter<T> clearHeaderHolder();

    IAdapter<T> clearFooterHolder();

    boolean containHeaderHolder(ViewDelegates header);

    boolean containFooterHolder(ViewDelegates footer);

    IAdapter<T> addEmptyHold(@LayoutRes int layout);

    IAdapter<T> addEmptyHold(ViewDelegates footer);

    IAdapter<T> addFooterHolder(@LayoutRes int layout);

    IAdapter<T> addFooterHolder(@LayoutRes int layout,boolean notify);

    IAdapter<T> addFooterHolder(ViewDelegates footer);

    IAdapter<T> addFooterHolder(ViewDelegates footer,boolean notify);


    IAdapter<T> addHeaderHolder(@LayoutRes int layout);

    IAdapter<T> addHeaderHolder(@LayoutRes int layout,boolean notify);

    IAdapter<T> addHeaderHolder(ViewDelegates header);

    IAdapter<T> addHeaderHolder(ViewDelegates header,boolean notify);


//        mHeaderViews.add(0,header);  //即可！
    IAdapter<T> addHeaderHolder(int index,@LayoutRes int layout);

    IAdapter<T> addHeaderHolder(int index,@LayoutRes int layout,boolean notify);

    IAdapter<T> addHeaderHolder(int index,ViewDelegates header);

    IAdapter<T> addHeaderHolder(int index,ViewDelegates header,boolean notify);


    IAdapter<T> addFooterHolder(int index,@LayoutRes int layout);

    IAdapter<T> addFooterHolder(int index,@LayoutRes int layout,boolean notify);

    IAdapter<T> addFooterHolder(int index,ViewDelegates footer);

    IAdapter<T> addFooterHolder(int index,ViewDelegates footer,boolean notify);


    IAdapter<T> setLoadFooterViewHold(@LayoutRes int layout);

    IAdapter<T> setLoadFooterViewHold(ILoadMoreDelegates loadFooterView);


    //允许style重复 重复的话 就覆盖之前的
    IAdapter<T> addViewHolder(int style, ViewDelegates viewDelegates);

    IAdapter<T> addViewHolder(ViewDelegates viewDelegates);

    IAdapter<T> relatedList(RecyclerView mRecyclerView);

    int getHeaderViewsCount();

    int getFooterViewsCount();

    //用于动画定位 不包括空view;
    int indexOfHF(ViewDelegates item);


    // =======================================
    // ============ notify ==============
    // =======================================

//    void notifyDataSetChanged();
//
//    void notifyItemChanged(int position);
//
//    void notifyItemInserted(int position);
//
//    void notifyItemRemoved(int position);
//
//    void notifyItemRangeChanged(int positionStart, int itemCount);
//
//    void notifyItemRangeInserted(int positionStart, int itemCount);
//
//    void notifyItemRangeRemoved(int positionStart, int itemCount);
//


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
//    int getItemCount();
//
//    int getItemViewType(int position);
//
//    void setHasStableIds(boolean hasStableIds);
//
//    long getItemId(int position);
//
//    void onViewRecycled(Holder holder);
//
//    boolean onFailedToRecycleView(Holder holder);
//
//    void onViewAttachedToWindow(Holder holder);
//
//    void onViewDetachedFromWindow(Holder holder);
//
//    void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
//
//    void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
//
//    void onAttachedToRecyclerView(RecyclerView recyclerView);
//
//    void onDetachedFromRecyclerView(RecyclerView recyclerView);
//
//    Holder onCreateViewHolder(ViewGroup parent, int viewType);
//
//    void onBindViewHolder(Holder holder, int position);
//
//    void onBindViewHolder(Holder holder, int position, List<Object> payloads);

}