package com.zone.adapter3kt.section

import com.zone.adapter3kt.adapter.EHFAdapter


/**
 *[2018] by Zone
 */
class Section(val obj: Any, val start: Int, val end: Int) {

    val sectionMannager = HashMap<String, Section>()

    fun upgradeTo(objList: List<Any>, adapter: EHFAdapter<Any>) {
        upgradeTo(objList, adapter, null)
    }

    fun upgradeTo(objList: List<Any>, adapter: EHFAdapter<Any>, payload: Object?) {
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