package com.zone.adapter3kt

import android.content.Context
import com.zone.adapter3kt.adapter.EventAdapter
import com.zone.adapter3kt.adapter.LoadMoreAdapter
import com.zone.adapter3kt.data.DataWarp

/**
 *[2018] by Zone
 */

//todo 总结成一个 真正的remove方法 这样覆盖好处理
open class StickyAdapter<T>(context: Context) : LoadMoreAdapter<T>(context) {

    lateinit var stickList: List<DataWarp<T>>

    override fun dataWithConfigChanged() {
        stickList = getDatas().filter { if (it.extraConfig.isSticky) true else false }
    }
}