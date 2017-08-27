package com.zone.adapter3.bean;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.zone.adapter3.helper.Helper;

/**
 * [2017] by Zone
 */

public abstract class ViewDelegates<T> {

    public static final int ORG_DECTOR=Integer.MAX_VALUE;
    protected Context context;
    private View itemView;
    private String tag;

    public ViewDelegates tryCreateView(Context context, RecyclerView rv) {
        if (itemView == null)
            reallyCreateHFView(context, rv);
        return this;
    }

    public void reallyCreateHFView(Context context, RecyclerView rv) {
        this.context = context;
        itemView = LayoutInflater.from(context).inflate(getLayoutId(), rv, false);
    }

    public void reallyCreateHFView(Context context) {
        this.context = context;
        //null的时候 生成的是FrameLayout的布局
        itemView = LayoutInflater.from(context).inflate(getLayoutId(), null, false);
    }

    @LayoutRes
    public abstract int getLayoutId();

    public abstract void fillData(int postion, T data, Helper<Helper> helper);

    public View getItemView() {
        return itemView;
    }

    public boolean isFullspan() {
        return false;
    }

    public Rect dectorRect() {
        return null;
    }
    public Rect reduceDectorRect() {
        return null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
