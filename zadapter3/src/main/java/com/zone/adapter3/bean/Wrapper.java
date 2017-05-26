package com.zone.adapter3.bean;


public class Wrapper {
    public static final int DEFAULT_VALUE = -1;
    public static final int EMPTY_VALUE = -2;
    int style = DEFAULT_VALUE;
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