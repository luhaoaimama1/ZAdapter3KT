package com.zone.adapter3kt.section

import com.zone.adapter3kt.adapter.EHFAdapter


/**
 *[2018] by Zone
 *
 * 为了通过obj设置对应的快捷 区域更新
 * 为什么这么设计 因为obj 的位置可能会变更 。给obj找到位置会更准
 * @param obj 当前item
 * @param start 以obj为参考点 设置的相对值
 * @param end 以obj为参考点 设置的相对值
 *
 * 列表 通过  item找到对应的 section 。
 */
class QuickUpdateSection(val obj: Any, val start: Int, val end: Int) {

    fun updateSecionDatas(objList: List<Any>, adapter: EHFAdapter<Any>, payload: Any? = null) {
        val index = adapter.mHFList.indexOfItem(obj)
        if (index == -1) throw IllegalStateException("列表中 已经不存在item了")
        else {
            //0-3 是4个
            val num = end - start + 1
            val beginIndex = num + start
            val endIndex = num + end
            if (safeCheck(beginIndex, adapter) || safeCheck(endIndex, adapter)) throw IllegalStateException("超出列表 ")
            else adapter.mHFList.changedRange(beginIndex, objList, payload)
        }
    }

    private fun safeCheck(beginIndex: Int, adapter: EHFAdapter<*>) = beginIndex < 0 || beginIndex > adapter.mHFList.mListCollection.count()

}