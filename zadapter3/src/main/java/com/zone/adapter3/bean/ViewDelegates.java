package com.zone.adapter3.bean;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.zone.adapter3.helper.Helper;

/**
 * [2017] by Zone
 */

public abstract class ViewDelegates<T> {

    protected Context context;
    private View itemView;

    public ViewDelegates tryCreateView(Context context, RecyclerView rv) {
        if (itemView == null)
            reallyCreateView(context, rv);
        return this;
    }

    public void reallyCreateView(Context context, RecyclerView rv) {
        this.context = context;
        itemView = LayoutInflater.from(context).inflate(getLayoutId(), rv, false);
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void fillData(int postion, T data, Helper helper);

    public View getItemView() {
        return itemView;
    }
}
