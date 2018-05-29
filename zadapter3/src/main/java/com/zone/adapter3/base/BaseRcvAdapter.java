package com.zone.adapter3.base;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.diff.DiffCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public abstract class BaseRcvAdapter<T> implements IAdapter<T>{

    protected RecyclerView.Adapter innerAdapter = new InnerAdapter();

    protected final List<T> data;
    protected final Context context;


    public BaseRcvAdapter(Context context, List<T> data) {
        if (data == null)
            throw new IllegalArgumentException("data must be not null!");
        this.context = context;
        this.data = data;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public List<T> getData() {
        return data;
    }


    @Override
    public void notifyItemChangedEx(int dataPosition) {
        notifyItemChanged(dataPosition + getHeaderViewsCount());
    }

    @Override
    public void notifyItemInsertedEx(int dataPosition) {
        notifyItemInserted(dataPosition + getHeaderViewsCount());
    }

    @Override
    public void notifyItemRemovedEx(int dataPosition) {
        notifyItemRemoved(dataPosition + getHeaderViewsCount());
    }

    @Override
    public void notifyItemRangeChangedEx(int dataPositionStart, int itemCount) {
        checkBound(dataPositionStart, itemCount);
        notifyItemRangeChanged(dataPositionStart + getHeaderViewsCount(), itemCount);
    }

    private void checkBound(int dataPositionStart, int itemCount) {
        if (dataPositionStart + itemCount >= data.size())
            throw new IllegalStateException("数组越界!");
    }

    @Override
    public void notifyItemRangeInsertedEx(int dataPositionStart, int itemCount) {
        checkBound(dataPositionStart, itemCount);
        notifyItemRangeInserted(dataPositionStart + getHeaderViewsCount(), itemCount);
    }

    @Override
    public void notifyItemRangeRemovedEx(int dataPositionStart, int itemCount) {
        checkBound(dataPositionStart, itemCount);
        notifyItemRangeRemoved(dataPositionStart + getHeaderViewsCount(), itemCount);
    }



    // =====================================================================
    // ============代理方法 相关 ============================================
    // =====================================================================

    /**
     * 代理类 :为了 解决 emptyView展示的的时候 notifyinsert数据报错的问题。因为 emptyView getItemCount其实为1
     * 所以notifyinsert 之前需要 判断 如果是空view状态 需要notifyremove(0) 然后notifyinsert
     * 但是呢所有人都喜欢用notifyinsert  而且他是final方法 我没办法覆盖 去检测。所以只能用委托的方式！
     */
    class InnerAdapter extends RecyclerView.Adapter<Holder> implements IAdapterProxy.UnActive<Holder>{


        @Override
        public void onBindViewHolder(Holder holder, int position, List<Object> payloads) {
            ((IAdapter) BaseRcvAdapter.this).onBindViewHolder(holder, position,payloads);
            super.onBindViewHolder(holder,position,payloads);
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return (Holder) ((IAdapter) BaseRcvAdapter.this).onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            ((IAdapter) BaseRcvAdapter.this).onBindViewHolder(holder, position);
        }

        @Override
        public int getItemCount() {
            return  ((IAdapter) BaseRcvAdapter.this).getItemCount();
        }

        @Override
        public int getItemViewType(int position) {
            return ((IAdapter) BaseRcvAdapter.this).getItemViewType(position);
        }


        @Override
        public boolean onFailedToRecycleView(Holder holder) {
            return  ((IAdapter) BaseRcvAdapter.this).onFailedToRecycleView(holder);
        }

        @Override
        public void onViewAttachedToWindow(Holder holder) {
            ((IAdapter) BaseRcvAdapter.this).onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(Holder holder) {
            ((IAdapter) BaseRcvAdapter.this).onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(Holder holder) {
            ((IAdapter) BaseRcvAdapter.this).onViewRecycled(holder);
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            ((IAdapter) BaseRcvAdapter.this).onAttachedToRecyclerView(recyclerView);
        }
        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            ((IAdapter) BaseRcvAdapter.this).onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public long getItemId(int position) {
            return  ((IAdapter) BaseRcvAdapter.this).getItemId(position);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {

    }

    @Override
    public boolean onFailedToRecycleView(Holder holder) {
        return false;
    }

    @Override
    public void onViewAttachedToWindow(Holder holder) {

    }

    @Override
    public void onViewDetachedFromWindow(Holder holder) {

    }

    @Override
    public void onViewRecycled(Holder holder) {

    }

    @Override
    public long getItemId(int position) {
        return   RecyclerView.NO_ID;
    }


    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        innerAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        innerAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        innerAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public boolean hasObservers() {
        return  innerAdapter.hasObservers();
    }

    @Override
    public boolean hasStableIds() {
        return innerAdapter.hasStableIds();
    }

    @Override
    public void notifyDataSetChanged() {
        innerAdapter.notifyDataSetChanged();
    }

    @Override
    public void notifyItemChanged(int position) {
        innerAdapter.notifyItemChanged(position);
    }

    @Override
    public void notifyItemChanged(int position, Object payload) {
        innerAdapter.notifyItemChanged(position,payload);
    }


    @Override
    public void notifyItemMoved(int fromPosition, int toPosition) {
        innerAdapter.notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        innerAdapter.notifyItemRangeChanged(positionStart,itemCount);
    }

    @Override
    public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        innerAdapter.notifyItemRangeChanged(positionStart,itemCount,payload);
    }


    protected boolean isEmpty;

    @Override
    public void notifyItemInserted(int position) {
        if (position == 0 && isEmpty)
            notifyItemRemoved(0);
        innerAdapter.notifyItemInserted(position);
    }

    @Override
    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        if (positionStart == 0 && isEmpty)
            notifyItemRemoved(0);
        innerAdapter.notifyItemRangeInserted(positionStart,itemCount);
    }

    @Override
    public void notifyItemRangeRemoved(int positionStart, int itemCount) {
        innerAdapter.notifyItemRangeRemoved(positionStart,itemCount);
    }

    @Override
    public void notifyItemRemoved(int position) {
        innerAdapter.notifyItemRemoved(position);
    }

    @Override
    public void bindViewHolder(Holder holder, int position) {
        innerAdapter.bindViewHolder(holder,position);
    }

    @Override
    public Holder createViewHolder(ViewGroup parent, int viewType) {
        return (Holder) innerAdapter.createViewHolder(parent,viewType);
    }
}
