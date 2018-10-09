package com.zone.adapter3kt

import android.content.Context
import com.zone.adapter3kt.adapter.StickyAdapter
import com.zone.adapter3kt.data.DataWarp

/**
 * Copyright (c) 2018 BiliBili Inc.
 * [2018/9/30] by Zone
 */
open class QuickAdapter<T>(context: Context, tag: Any? = null) : StickyAdapter<T>(context, tag) {

    // =====================================================
    // =================== 过滤tags的后的位置=================
    // ==Tips:当前的item 即使是过滤的信息也不会被过滤掉这点要注意 ==
    // =====================================================
    fun filterTagsPosi(item: T, vararg tags: String): Int =
        filterTagsCommon(tags, { index, it -> it.data == item })

    fun filterTagsPosi(posi: Int, vararg tags: String): Int =
        filterTagsCommon(tags, { index, it -> index == posi })

    private fun filterTagsCommon(tags: Array<out String>, method: (index: Int, it: DataWarp<T>) -> Boolean): Int {
        var posiResult = -1
        var tagsCount = 0
        mHFList.mListCollection.loop { index, it ->
            if (method(index, it)) {
                posiResult = index
                true
            } else {
                for (tag in tags) {
                    if (it.extraConfig.tags.contains(tag)) {
                        //找到tag +1  并退出循环
                        tagsCount++
                        break
                    }
                }
                false
            }
        }
        posiResult = if (posiResult == -1) posiResult else posiResult - tagsCount
        return posiResult
    }


}
