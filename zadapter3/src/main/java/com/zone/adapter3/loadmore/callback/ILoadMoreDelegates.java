package com.zone.adapter3.loadmore.callback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.helper.Helper;
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

    public abstract void loading();

    public abstract void complete();

    public abstract void fail();

    public abstract void end();


    protected abstract void initData(View convertView, RecyclerView rv);

    @Override
    public void reallyCreateView(Context context, RecyclerView rv) {
        QuickConfig.e("tryCreateView");
        super.reallyCreateView(context, rv);
        initData(getItemView(), rv);
    }

    @Override
    public void fillData(int postion, Object data, Helper helper) {

    }

    public abstract ILoadMoreDelegates clone_();


}
