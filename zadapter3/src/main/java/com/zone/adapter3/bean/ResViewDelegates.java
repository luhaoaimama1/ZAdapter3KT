package com.zone.adapter3.bean;

import android.support.annotation.LayoutRes;

/**
 * [2017] by Zone
 */

public class ResViewDelegates<T> extends ViewDelegates {
    public int layoutId;

    public ResViewDelegates(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public int getLayoutId() {
        return layoutId;
    }

    @Override
    public void fillData(int postion, Object data, Holder holderolder) {

    }
}
