package com.zone.adapter3.bean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zone.adapter3.helper.Helper;

public class Holder extends RecyclerView.ViewHolder {

    public Wrapper wrapper;
    public Helper helper;

    public Holder(Context context,Wrapper wrapper) {
        super(wrapper.viewDelegates.getItemView());
        this.wrapper=wrapper;
//        helper=new Helper(context,itemView);
    }

    public Holder(View view) {
        super(view);
    }

    public Helper getHelper() {
        return helper;
    }

    public void setHelper(Helper helper) {
        this.helper = helper;
    }
}