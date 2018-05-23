package com.zone.adapter3.bean;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.zone.adapter3.base.IAdapter;

/**
 * [2017] by Zone
 * <p>
 * 这里的field：ehfHolder 仅仅为了放头和脚的。为什么不写到子类中？因为这样能复用 既头也可以是item item也可以是头
 */
public abstract class EHFViewDelegates<T> {

    public static final int ORG_DECTOR = Integer.MAX_VALUE;


    // =======================================
    // ============ 用于HF，Empty等 简称EHF ==============
    // =======================================
    //用于HF，Empty等
    private Holder ehfHolder;

    //todo  放到子类中
    private Holder parentHolder;
    private View placeholderView;

    public View getPlaceholderView() {
        return placeholderView;
    }

    public void setPlaceholderView(View placeholderView) {
        this.placeholderView = placeholderView;
    }

    /**
     * 此方法 用于  header footer empty 那种只创建一次的
     *
     * @param context
     * @param adapter
     * @return
     */
    public Holder tryEHFCreateView(Context context, IAdapter adapter) {
        if (ehfHolder == null)
            ehfHolder = reallyCreateView(context, adapter);
        return ehfHolder;
    }

    public Holder getEHFHolder() {
        return ehfHolder;
    }

    public Holder getParentHolder() {
        return parentHolder;
    }

    public EHFViewDelegates<T> setParentHolder(Holder parentHolder) {
        this.parentHolder = parentHolder;
        return this;
    }

    public abstract Holder reallyCreateView(Context context, IAdapter adapter);

    public abstract void fillData(int postion, T data, Holder holder);

    @Override
    public Object clone() throws CloneNotSupportedException {
        final EHFViewDelegates<T> old = this;
        return new EHFViewDelegates<T>() {
            @Override
            public Holder reallyCreateView(Context context, IAdapter adapter) {
                return old.reallyCreateView(context, adapter);
            }

            @Override
            public void fillData(int postion, T data, Holder holder) {
                old.fillData(postion, data, holder);
            }
        };
    }
}