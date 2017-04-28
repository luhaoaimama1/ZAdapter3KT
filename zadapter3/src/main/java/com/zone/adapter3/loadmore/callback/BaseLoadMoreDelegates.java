package com.zone.adapter3.loadmore.callback;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter.R;
import com.zone.adapter3.loadmore.ScrollerUtils;

/**
 * [2017] by Zone
 */

public class BaseLoadMoreDelegates extends ILoadMoreDelegates {

    private View loadingView, failView, endView;

    @Override
    public void loading() {
        loadingView.setVisibility(View.VISIBLE);
        failView.setVisibility(View.GONE);
        endView.setVisibility(View.GONE);
    }

    @Override
    public void complete() {
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.GONE);
        endView.setVisibility(View.GONE);
    }

    @Override
    public void fail() {
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.VISIBLE);
        endView.setVisibility(View.GONE);
    }

    @Override
    public void end() {
        loadingView.setVisibility(View.GONE);
        failView.setVisibility(View.GONE);
        endView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData(View convertView, final RecyclerView rv) {
        ViewGroup vp = (ViewGroup) convertView;
        loadingView = LayoutInflater.from(convertView.getContext())
                .inflate(R.layout.sample_common_list_footer_loading, rv, false);
        failView = LayoutInflater.from(convertView.getContext())
                .inflate(R.layout.sample_common_list_footer_network_error, rv, false);
        failView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading();
                ScrollerUtils.loadMore(mOnScrollRcvListener,rv);
            }
        });
        endView = LayoutInflater.from(convertView.getContext())
                .inflate(R.layout.sample_common_list_footer_end, rv, false);
        //todo  endView逻辑  最后测试
        vp.addView(loadingView);
        vp.addView(failView);
        vp.addView(endView);
    }

    @Override
    public ILoadMoreDelegates clone_() {
        return new BaseLoadMoreDelegates();
    }

    @Override
    public int getLayoutId() {
        return R.layout.vp;
    }
}
