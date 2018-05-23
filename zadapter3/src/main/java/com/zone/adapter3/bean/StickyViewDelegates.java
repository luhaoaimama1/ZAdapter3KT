package com.zone.adapter3.bean;

import android.content.Context;
import android.view.View;

import com.zone.adapter3.base.IAdapter;

/**
 * [2017] by Zone
 * <p>
 * 这里的field：ehfHolder 仅仅为了放头和脚的。为什么不写到子类中？因为这样能复用 既头也可以是item item也可以是头
 */
public abstract class StickyViewDelegates<T> extends EHFViewDelegates<T>{


    //todo  放到子类中
    private Holder parentHolder;
    private View placeholderView;

    public Holder getParentHolder() {
        return parentHolder;
    }
    public EHFViewDelegates<T> setParentHolder(Holder parentHolder) {
        this.parentHolder = parentHolder;
        return this;
    }


    public View getPlaceholderView() {
        return placeholderView;
    }

    public void setPlaceholderView(View placeholderView) {
        this.placeholderView = placeholderView;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        final StickyViewDelegates<T> old = this;
        return new StickyViewDelegates<T>() {
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
