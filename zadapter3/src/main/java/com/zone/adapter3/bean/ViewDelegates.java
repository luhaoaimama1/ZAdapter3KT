package com.zone.adapter3.bean;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;

import com.zone.adapter3.base.IAdapter;

/**
 * [2017] by Zone
 *
 * 这里的field：ehfHolder 仅仅为了放头和脚的。为什么不写到子类中？因为这样能复用 既头也可以是item item也可以是头
 */
public abstract class ViewDelegates<T> {

    public static final int ORG_DECTOR=Integer.MAX_VALUE;


    // =======================================
    // ============ 用于HF，Empty等 简称EHF ==============
    // =======================================
    //用于HF，Empty等
    private Holder ehfHolder;
    /**
     * 此方法 用于  header footer empty 那种只创建一次的
     * @param context
     * @param adapter
     * @return
     */
    public Holder tryEHFCreateView(Context context,  IAdapter adapter) {
        if (ehfHolder == null)
            ehfHolder =reallyCreateView(context, adapter);
        return ehfHolder;
    }

    public Holder getEhfHolder() {
        return ehfHolder;
    }

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

    public abstract void fillData(int postion, T data, Holder<Holder> holder);

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
