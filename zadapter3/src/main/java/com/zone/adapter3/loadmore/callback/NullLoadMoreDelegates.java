package com.zone.adapter3.loadmore.callback;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2016/4/1.
 */
public  class NullLoadMoreDelegates extends ILoadMoreDelegates {

    public int layoutId;

    public NullLoadMoreDelegates(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }
    @Override
    public void loading() {

    }

    @Override
    public void complete() {

    }

    @Override
    public void fail() {

    }

    @Override
    public void end() {

    }

    @Override
    protected void initData(View convertView, RecyclerView rv) {

    }

    @Override
    public ILoadMoreDelegates clone_() {
        return new NullLoadMoreDelegates(this.layoutId);
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }
}
