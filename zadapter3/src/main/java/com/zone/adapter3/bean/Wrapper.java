package com.zone.adapter3.bean;


import com.zone.adapter3.base.Header2FooterRcvAdapter;

public class Wrapper {

    int style = Header2FooterRcvAdapter.DEFAULT_VALUE;
    ViewDelegates viewDelegates;

    public Wrapper(int style, ViewDelegates viewDelegates) {
        this.style = style;
        this.viewDelegates = viewDelegates;
    }

    public int getStyle() {
        return style;
    }

    public ViewDelegates getViewDelegates() {
        return viewDelegates;
    }
}