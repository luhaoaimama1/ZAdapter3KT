package com.zone.adapter3.base;


import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.util.ListUpdateCallback;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zone.adapter3.diff.DiffCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/3.
 * <p>
 * 仅仅支持 数据的更改 ,对于 头部 底部等 添加移除 需要自己去通知
 *
 * 不支持 映射内容数据 或 断头功能
 */
public abstract class DiffRcvAdapter<T> extends Header2FooterRcvAdapter<T> {

    private List<T> dataBackUp;
    private DiffUtil.DiffResult diffResult;
    private RecyclerView.Adapter innerAdapter = new RecyclerView.Adapter() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    };

    public DiffRcvAdapter(Context context, List<T> data) {
        super(context, data);
    }

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

    ListUpdateCallback callback;

    @Override
    public void diffNotifyDataSetChanged() {
        if (contentDataMapListener != null || stickyOnScrollListener != null)
            throw new IllegalStateException("不支持 映射内容数据 或 断头功能！");

        if (diffResult != null) {
            if (callback == null)
                callback = new ListUpdateCallback() {
                    @Override
                    public void onInserted(int position, int count) {
                        DiffRcvAdapter.this.notifyItemRangeInserted(position + getHeaderViewsCount(), count);
                    }

                    @Override
                    public void onRemoved(int position, int count) {
                        DiffRcvAdapter.this.notifyItemRangeRemoved(position + getHeaderViewsCount(), count);
                    }

                    @Override
                    public void onMoved(int fromPosition, int toPosition) {
                        DiffRcvAdapter.this.notifyItemMoved(fromPosition + getHeaderViewsCount(), toPosition + getHeaderViewsCount());
                    }

                    @Override
                    public void onChanged(int position, int count, Object payload) {
                        //防止闪烁
                        DiffRcvAdapter.this.notifyItemRangeChanged(position + getHeaderViewsCount(), count, payload);
                    }
                };
            diffResult.dispatchUpdatesTo(callback);
        } else
            throw new IllegalStateException("please first use diffCalculate(diffCallBack)!");
    }
}
