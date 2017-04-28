package com.zone.adapter3.bean;

import android.support.annotation.LayoutRes;

import com.zone.adapter3.helper.Helper;

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
    public void fillData(int postion, Object data, Helper helper) {

    }
}
