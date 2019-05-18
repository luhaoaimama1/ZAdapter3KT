package com.zone.adapter3kt

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.adapter.StickyAdapter
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.section.Section
import com.zone.adapter3kt.utils.getFirstLastPosrecyclerView

/**
 * [2018/9/30] by Zone
 * 过滤  查找等 方法
 */
open class QuickAdapter<T>(context: Context) : StickyAdapter<T>(context) {
    companion object {
        internal var quickConfig: QuickConfig? = null
    }

    init {
        //加载默认头部 ps:回来注册的会被他顶掉
        initQuickConfig()
    }

    private fun initQuickConfig() {
        quickConfig?.apply {
            registerLoadingDelegate(this.loadMoreDelegates.clone_(), this.loadingSetting)
        }
    }

    // =====================================================
    // =================== 过滤tags的后的位置=================
    // ==Tips:当前的item 即使是过滤的信息也不会被过滤掉这点要注意 ==
    // =====================================================
    /**
     * 返回过滤tags后的位置 如果是tags点击的话会返回-1
     */
    fun filterTagsPosi(item: T, vararg tags: String): Int =
        filterTagsCommon(tags) { _, it -> it.data == item }

    /**
     * 返回过滤tags后的位置 如果是tags点击的话会返回-1
     */
    fun filterTagsPosi(posi: Int, vararg tags: String): Int =
        filterTagsCommon(tags) { index, _ -> index == posi }

    private fun filterTagsCommon(tags: Array<out String>, method: (index: Int, it: DataWarp<T>) -> Boolean): Int {
        var posiResult = -1
        var tagsCount = 0
        mHFList.mListCollection.loop { index, it ->
            var isContainTags = false
            for (tag in tags) {
                if (it.extraConfig.tags.contains(tag)) {
                    //找到tag +1  并退出循环
                    tagsCount++
                    isContainTags = true
                    break
                }
            }
            if (method(index, it)) {
                if (!isContainTags) posiResult = index
                true
            } else false
        }
        posiResult = if (posiResult == -1) posiResult else posiResult - tagsCount
        return posiResult
    }

    /**
     *  ex:找到第一个不是头部的位置
     */
    fun findFirstPositionByExculdeCardType(vararg exculdeTypes: Int): Int {
        var firstPositionByExculdeCardType = -1
        mHFList.mListCollection.loop { index, item ->
            val itemType = item.extraConfig.viewStyle
            var isContainsExculdeType = false
            for (exculdeTypeItem in exculdeTypes) {
                if (exculdeTypeItem == itemType) {
                    isContainsExculdeType = true
                    break
                }
            }
            if (!isContainsExculdeType) {
                firstPositionByExculdeCardType = index
                true //退出循环
            } else false
        }
        return firstPositionByExculdeCardType
    }

    // =====================================================
    // =================== Section 系列=================
    // ==Tips:细分复用  上报埋点 的section ==
    // =====================================================

    var isMonitorSection = false
    var nowAddSection: Section? = null
    // 并不接受在里面进行延迟造作
    fun addSection(section: Section, method: () -> Unit) {
        isMonitorSection = true
        nowAddSection = section
        method()
        //notifyItemRangeInsertedInnerMonitor 执行添加section操作后 nowAddSection制空
        nowAddSection = null
    }

    @Suppress("UNCHECKED_CAST")
    override fun notifyItemRangeInsertedInnerMonitor(positionStart: Int, itemCount: Int) {
        super.notifyItemRangeInsertedInnerMonitor(positionStart, itemCount)
        if (!isMonitorSection) return
        nowAddSection?.apply {
            //..包括左右边界 是闭区间
            for (i in positionStart..(positionStart + itemCount - 1)) {
                val realItem = getRealItem(i)
                realItem!!.extraConfig.section = this
                this.bindObjList.add(realItem as DataWarp<Any>)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun notifyItemRangeRemovedInnerMonitor(positionStart: Int, itemCount: Int) {
        super.notifyItemRangeRemovedInnerMonitor(positionStart, itemCount)
        if (!isMonitorSection) return
        for (i in positionStart..(positionStart + itemCount - 1)) {
            val realItem = getRealItem(i)
            realItem?.extraConfig?.section?.bindObjList?.remove(realItem as DataWarp<Any>)
            realItem?.extraConfig?.apply {
                this.section=null
            }
        }
    }

    val visiSectionList: ArrayList<Section> by lazy { ArrayList<Section>() }
    /**
     * 什么叫做显示？
     * 当前显示的sectionList没有。既为 显示
     */
    override fun onViewAttachedToWindow(baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        super.onViewAttachedToWindow(baseHolder)
        if (!isMonitorSection) return
        val section = getRealItem(baseHolder.layoutPosition)?.extraConfig?.section
        //section可空 只要和后面不等  因为有些卡片我不想赋section
        section?.apply {
            if (visiSectionList.indexOf(section) == -1) {
                visiSectionList.add(section)
                findSection(section) {
                    onViewAttachedToWindowForSection(baseHolder, section, it)
                }
            }
        }
    }

    /**
     * 什么叫做隐藏？
     * 通过holder找到section  通过section找到内部第一个和最后一个在列表的位置 然后找到列表中第一个可见和最后一个可见 作对比
     */
    @Suppress("UNCHECKED_CAST")
    override fun onViewDetachedFromWindow(baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>) {
        super.onViewDetachedFromWindow(baseHolder)
        if (!isMonitorSection) return
        val detachSectionPosition = baseHolder.layoutPosition
        val section = getRealItem(detachSectionPosition)?.extraConfig?.section
        section?.apply {
            val pair = recyclerView!!.getFirstLastPosrecyclerView()
            if (pair.first < 0) return
            //如果等于当前 要跑出 可见的 holder 则first+1/second-1
            val first = if (detachSectionPosition == pair.first) pair.first + 1 else pair.first
            val second = if (detachSectionPosition == pair.second) pair.second - 1 else pair.second

            var thisSectionFirstPosi = -1
            var thisSectionLastPosi = -1
            this.bindObjList.firstOrNull()?.apply {
                thisSectionFirstPosi = mHFList.indexOfRealItem(this as DataWarp<T>)
            }
            this.bindObjList.lastOrNull()?.apply {
                thisSectionLastPosi = mHFList.indexOfRealItem(this as DataWarp<T>)
            }
            //列表中没找到数据 ，返回！
            if(thisSectionLastPosi==-1||thisSectionLastPosi==-1) return
            //如果section最后数据那个位置小于 section中第一个数据的位置 表明数据不正确 ，返回！
            if(thisSectionFirstPosi>thisSectionLastPosi)return
            //找到后 如果不在可见范围内
            if (thisSectionLastPosi < first || thisSectionFirstPosi > second) {
                visiSectionList.remove(section)
                findSection(section) {
                    onViewDetachedFromWindowForSection(baseHolder, section, it)
                }
            }
        }
    }

    open fun findSection(section: Section, method: (sectionPosi: Int) -> Unit) {
        var sectionPosi = 0
        mHFList.mListCollection.loop { _ , item: DataWarp<T> ->
            item.extraConfig.section?.apply {
                if (this == section) {
                    method(sectionPosi)
                    return@loop true
                }
                sectionPosi++
            }
            false
        }
    }

    open fun onViewAttachedToWindowForSection(baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>, section: Section, sectionPosi: Int) {
    }

    open fun onViewDetachedFromWindowForSection(baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>, section: Section, sectionPosi: Int) {
    }

}
