package com.zone.adapter3.loadmore;

import android.support.v7.widget.RecyclerView;

/**
 * [2017] by Zone
 */

public class ScrollerUtils {

    public static void loadMore(OnScrollRcvListener onScrollRcvListener,RecyclerView recyclerView){
        onScrollRcvListener.loadMore(recyclerView);
    }
}
