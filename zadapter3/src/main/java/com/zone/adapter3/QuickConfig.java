package com.zone.adapter3;

import android.util.Log;

import com.zone.adapter3.loadmore.callback.BaseLoadMoreDelegates;
import com.zone.adapter3.loadmore.callback.ILoadMoreDelegates;
import com.zone.adapter3.loadmore.callback.NullLoadMoreDelegates;


/**
 * Created by Administrator on 2016/3/24.
 */
public class QuickConfig {

    public static final String TAG = "ZAdapter3";

    static boolean writeLog = true;

    ILoadMoreDelegates loadMoreDelegates=new BaseLoadMoreDelegates();


    public ILoadMoreDelegates getLoadMoreDelegates() {
        return loadMoreDelegates;
    }

    public void setiLoadMoreDelegates(ILoadMoreDelegates iLoadMoreDelegates) {
        this.loadMoreDelegates = iLoadMoreDelegates;
    }

    public boolean isWriteLog() {
        return writeLog;
    }

    public void setWriteLog(boolean writeLog) {
        this.writeLog = writeLog;
    }

    public static QuickConfig build() {
        return new QuickConfig();
    }

    public void perform() {
        QuickRcvAdapter.quickConfig = this;
    }


    public static void d(String str) {
        if (writeLog)
            Log.d(TAG, str);
    }

    public static void e(String str) {
        if (writeLog)
            Log.e(TAG, str);
    }

    public static void v(String str) {
        if (writeLog)
            Log.v(TAG, str);
    }

    public static void i(String str) {
        if (writeLog)
            Log.i(TAG, str);
    }
}
