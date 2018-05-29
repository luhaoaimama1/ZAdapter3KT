package com.zone.adapter3.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zone.adapter3.bean.Holder;

import java.util.List;

/**
 * [2018] by Zone
 */

public interface IAdapterProxy <VH extends RecyclerView.ViewHolder> {


    public abstract int getItemCount();
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);
    public abstract void onBindViewHolder(VH holder, int position);
    public long getItemId(int position);
    public int getItemViewType(int position);
    public void onAttachedToRecyclerView(RecyclerView recyclerView);
    public void onBindViewHolder(VH holder, int position, List<Object> payloads);
    public void onDetachedFromRecyclerView(RecyclerView recyclerView);
    public boolean onFailedToRecycleView(VH holder);
    public void onViewAttachedToWindow(VH holder);
    public void onViewDetachedFromWindow(VH holder);
    public void onViewRecycled(VH holder);
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer);
    public void setHasStableIds(boolean hasStableIds);
    /**
     * final
     */

}
