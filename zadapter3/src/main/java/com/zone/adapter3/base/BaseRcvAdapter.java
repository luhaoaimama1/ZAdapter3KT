package com.zone.adapter3.base;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.diff.DiffCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 */
public abstract class BaseRcvAdapter<T> extends RecyclerView.Adapter<Holder> implements IAdapter<T> {

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

    private List<T> dataBackUp;
    private DiffUtil.DiffResult diffResult;

    @Override
    public void diffSetKeyframe() {
        dataBackUp = new ArrayList<>();
        dataBackUp.addAll(data);
        diffResult = null;
    }

    @Override
    public void diffCalculate(DiffCallBack<T> diffCallBack) {
        if (dataBackUp == null)
            throw new IllegalStateException("please first use diffSetKeyframe()!");
        diffCallBack.setDatas(dataBackUp, data);
        diffResult = DiffUtil.calculateDiff(diffCallBack, true);
    }

    @Override
    public void diffNotifyDataSetChanged() {
        if (diffResult != null)
            diffResult.dispatchUpdatesTo(this);
        else
            throw new IllegalStateException("please first use diffCalculate(diffCallBack)!");
    }
}
