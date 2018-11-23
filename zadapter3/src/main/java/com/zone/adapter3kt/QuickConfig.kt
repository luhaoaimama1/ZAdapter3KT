package com.zone.adapter3kt

import android.util.Log
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.delegate.done.BaseLoadMoreDelegates

import com.zone.adapter3kt.loadmore.LoadingSetting

/**
 * Created by Administrator on 2016/3/24.
 */
class QuickConfig {

    var loadMoreDelegates: LoadMoreViewDelegate = BaseLoadMoreDelegates()
    var loadingSetting: LoadingSetting = LoadingSetting()

    var isWriteLog: Boolean
        get() = writeLog
        set(writeLog) {
            isWriteLog = writeLog
        }

    fun perform() {
        QuickAdapter.quickConfig = this
    }

    companion object {

        val TAG = "ZAdapter3KT"

        internal var writeLog = true
        @JvmStatic
        fun build(): QuickConfig {
            return QuickConfig()
        }
        @JvmStatic
        fun d(str: String) {
            if (writeLog)
                Log.d(TAG, str)
        }

        @JvmStatic
        fun e(str: String) {
            if (writeLog)
                Log.e(TAG, str)
        }
        @JvmStatic
        fun v(str: String) {
            if (writeLog)
                Log.v(TAG, str)
        }
        @JvmStatic
        fun i(str: String) {
            if (writeLog)
                Log.i(TAG, str)
        }
    }
}
