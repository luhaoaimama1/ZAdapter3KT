package com.zone.adapter3.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zone.adapter3.bean.Holder;

import java.util.List;

/**
 * [2018] by Zone
 *
 * 确保不会造成循环
 */
public interface IAdapterProxy {
    /**
     *  回调方法  。回调给委托的信息。  自己使用是不不会委托adapter处理
     * @return
     */
    public interface UnActive<VH extends RecyclerView.ViewHolder>{

        abstract int getItemCount();

        abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

        abstract void onBindViewHolder(VH holder, int position);


        int getItemViewType(int position);

        void onAttachedToRecyclerView(RecyclerView recyclerView);

        void onBindViewHolder(VH holder, int position, List<Object> payloads);

        void onDetachedFromRecyclerView(RecyclerView recyclerView);

        boolean onFailedToRecycleView(VH holder);

        void onViewAttachedToWindow(VH holder);

        void onViewDetachedFromWindow(VH holder);

        void onViewRecycled(VH holder);

        long getItemId(int position);

    }

    /**
     *  主动使用的。
     */

    public interface Active <VH extends RecyclerView.ViewHolder>{

        void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer);

        void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer);

        void setHasStableIds(boolean hasStableIds);

        boolean hasObservers();
        boolean hasStableIds();
        void notifyDataSetChanged();
        void notifyItemChanged(int position);
        void notifyItemChanged(int position, Object payload);
        void notifyItemInserted(int position);
        void notifyItemMoved(int fromPosition, int toPosition);
        void notifyItemRangeChanged(int positionStart, int itemCount);
        void notifyItemRangeChanged(int positionStart, int itemCount, Object payload);
        void notifyItemRangeInserted(int positionStart, int itemCount);
        void notifyItemRangeRemoved(int positionStart, int itemCount);
        void notifyItemRemoved(int position);


        /**
         * 并不知道这两个有啥软用
         * @param holder
         * @param position
         */
        void bindViewHolder(VH holder, int position);
        VH createViewHolder(ViewGroup parent, int viewType);

    }

}
