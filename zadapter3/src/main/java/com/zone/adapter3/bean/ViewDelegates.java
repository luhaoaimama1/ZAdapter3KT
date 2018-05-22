package com.zone.adapter3.bean;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;

import com.zone.adapter3.base.IAdapter;

/**
 * [2017] by Zone
 *
 */
public abstract class ViewDelegates<T> extends EHFViewDelegates<T> {

    public static final int ORG_DECTOR=Integer.MAX_VALUE;


    // =======================================
    // ============ 用于content ==============
    // =======================================

    private String tag;
    protected Context context;
    protected IAdapter adapter;


    public Holder reallyCreateView(Context context, IAdapter adapter) {
        this.context = context;
        this.adapter= adapter;
        return getLayoutHolder();
    }

    @LayoutRes
    public abstract int getLayoutId();

    /**
     * super.getLayoutView() 就是通过id创建布局了。
     * 想要通过view的方式前后插入 东西的话用这种方式
     * @return
     */
    public  Holder getLayoutHolder(){
        return  new Holder(LayoutInflater.from(context).inflate(getLayoutId(), adapter.getRecyclerView()
                , false));
    };


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
