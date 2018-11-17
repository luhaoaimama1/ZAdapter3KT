package com.zone.adapter3kt.section

import com.zone.adapter3kt.data.DataWarp

/**
 *[2018/11/15] by Zone
 *
 * 写的目的：细分复用 上传位置
 * section 为一连串的 数据。如果传入不连续的 会出现不可知的问题
 * 使用注意：section作为一个整体。
 * 1.移动的话作为一整块移动
 * 2.要删除的话 ，记得在bindObjList移除obj
 */
class Section(val obj: Any) {
    val bindObjList: ArrayList<DataWarp<Any>> by lazy { ArrayList<DataWarp<Any>>() }
}