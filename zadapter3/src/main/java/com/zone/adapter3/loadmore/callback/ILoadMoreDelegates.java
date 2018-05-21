package com.zone.adapter3.loadmore.callback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.loadmore.OnScrollRcvListener;

/**
 * Created by Administrator on 2016/4/1.
 * todo  已经到底了的问题
 */
public abstract class ILoadMoreDelegates extends ViewDelegates {

    protected OnScrollRcvListener mOnScrollRcvListener;

    public OnScrollRcvListener getOnScrollRcvListener() {
        return mOnScrollRcvListener;
    }

    public void setOnScrollRcvListener(OnScrollRcvListener onScrollRcvListener) {
        this.mOnScrollRcvListener = onScrollRcvListener;
    }
    /**
     * loadMore的表现形式 和监听无关
     */
    public abstract void loading();

    /**
     * loadMore的表现形式 和监听无关
     */
    public abstract void complete();

    /**
     * loadMore的表现形式 和监听无关
     */
    public abstract void fail();
    /**
     * loadMore的表现形式  和监听无关
     */
    public abstract void end();


    protected abstract void initData(View convertView, RecyclerView rv);

    @Override
    public Holder reallyCreateView(Context context,  IAdapter adapter) {
        QuickConfig.e("tryEHFCreateView");
        Holder holder=super.reallyCreateView(context, mOnScrollRcvListener.getQuickRcvAdapter());
        initData(holder.itemView, mOnScrollRcvListener.getQuickRcvAdapter().getRecyclerView());
        return holder;
    }

    @Override
    public void fillData(int postion, Object data, Holder holder) {

    }

    public abstract ILoadMoreDelegates clone_();


}
