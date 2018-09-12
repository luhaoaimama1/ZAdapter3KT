//package com.zone.adapter3kt.adapter
//
//import android.content.Context
//import com.zone.adapter3kt.delegate.ViewDelegate
//import com.zone.adapter3.QuickConfig
//import com.zone.adapter3kt.*
//import java.util.*
//import kotlin.collections.ArrayList
//
///**
// *[2018] by Zone
// */
//class HF2DataAdapter<T>(context: Context) : DelegatesAdapter<T>(context) {
//    val headerViewStyleOrder by lazy {
//        ArrayList<Int>()
//    }
//    val footerViewStyleOrder by lazy {
//        val arrayList = ArrayList<Int>()
//        arrayList.add(LOADING_VALUE) //设置loading为最底部
//        arrayList
//    }
//
//    //loading
//    fun registerLoadingDelegate(delegate: ViewDelegate<Any>) {
//        delegatesManager.registerDelegate(LOADING_VALUE, delegate)
//    }
//
//    fun checkSafe() {
//        for (item in headerViewStyleOrder) {
//            if (footerViewStyleOrder.contains(item)) {
//                throw IllegalStateException("HeaderOrder can't contains FooterOrder's viewtype!")
//            }
//        }
//    }
//
//    fun defineHeaderOrder(mode: Mode, vararg viewTypes: Int) {
//        viewTypes.forEach { headerViewStyleOrder.add(it) }
//        checkSafe();
//    }
//
//    fun defineFooterOrder(mode: Mode, vararg viewTypes: Int) {
//        viewTypes.forEach { footerViewStyleOrder.add(it) }
//        checkSafe();
//    }
//
//    enum class Mode {
//        ADD, ADD_OR_CHANGE;
//    }
//
//    class DataWarper<T>(var data: T, var adapter: Adapter<T>, var styleExtra: ViewStyle<T>) {
//        var viewStyleOBJ: ViewStyleOBJ? = null
//
//        init {
//            viewStyleOBJ = styleExtra.generateViewStyleOBJ(adapter, data)
//        }
//    }
//
//    // =======================================数据操作=====================================
//
//    var headerMode = Mode.ADD_OR_CHANGE
//    var footerMode = Mode.ADD_OR_CHANGE
//    val headerDatas by lazy { ArrayList<DataWarper<T>>() }
//    val footerDatas by lazy { ArrayList<DataWarper<T>>() }
//    val contentDatas by lazy { ArrayList<DataWarper<T>>() }
//
//    private fun isEmptyData(): Boolean {
//        return delegatesManager.getDelegate(EMPTY_VALUE) == null
//                && headerDatas.size == 0
//                && footerDatas.size == 0
//                && contentDatas.size == 0
//    }
//
//    fun getCHFItemCount(): Int {
//        return contentDatas.size + headerDatas.size + footerDatas.size
//    }
//
//    fun getOriItem(position: Int): DataWarper<T>? {
//        if (isEmptyData()) return null
//        else {
//            if (position >= headerDatas.size && position < headerDatas.size + contentDatas.size) {
//                return headerDatas.get(position - headerDatas.size)
//            } else if (position < headerDatas.size) {
//                QuickConfig.e("bind header position:" + position)
//                return headerDatas.get(position)
//            } else {
//                QuickConfig.e("bind footer position:" + position)
//                return headerDatas.get(position - headerDatas.size - contentDatas.size)
//            }
//        }
//    }
//
//    fun getItem(position: Int): T? = getOriItem(position)?.data
//
//    override fun getItemCount(): Int {
//        return getCHFItemCount() + if (isEmptyData()) 1 else 0
//    }
//
//    override fun getItemViewType(position: Int): Int = getOriItem(position)?.viewStyleOBJ?.viewStyle ?: defaultViewStyleOBJ.viewStyle
//
//    var styleExtra: ViewStyle<T> = ViewStyleDefault()
//    var defaultViewStyleOBJ: ViewStyleOBJ = ViewStyleOBJ.Builder().build()
//
//
//    // =======================================数据操作=====================================
//    //
//    //如果存在就 change 没有就add  一般给头部与底部用的
////    fun addOrChange(item: T?) {
////        if (item == null) return
////        val index = contentDatas.indexOf(item)
////        if (index == -1) add(item)
////        else notifyItemChanged(index)
////    }
//
//    fun add(item: T?) {
//        add(contentDatas.size, item)
//    }
//
//    fun add(itemList: List<T>?) {
//        add(contentDatas.size, itemList)
//    }
//
//    fun add(pos: Int, item: T?) {
//        if (item == null) return
//        contentDatas.add(pos, DataWarper(item, this, styleExtra))
//        notifyItemInserted(pos)
//    }
//
//    fun add(pos: Int, moreItems: List<T>?) {
//        if (pos < 0 || moreItems == null || moreItems.isEmpty()) return
//        val dataWarperList = ArrayList<DataWarper<T>>()
//        moreItems.forEach {
//            dataWarperList.add(DataWarper(it, this, styleExtra))
//        }
//        contentDatas.addAll(pos, dataWarperList)
//        notifyItemRangeInserted(pos, moreItems.size)
//    }
//
//    fun remove(pos: Int) {
//        if (pos < 0 || pos > contentDatas.size - 1) return
//        contentDatas.removeAt(pos)
//        notifyItemRemoved(pos)
//    }
//
//    fun removed(positionStart: Int, itemCount: Int) {
//        if (positionStart < 0 || (positionStart + itemCount > contentDatas.size - 1)) return
//        for (i in itemCount..0) {
//            contentDatas.removeAt(i + positionStart)
//        }
//        notifyItemRangeRemoved(positionStart, itemCount)
//    }
//
//    fun removeAll() {
//        val count = contentDatas.size
//        contentDatas.clear()
//        notifyItemRangeRemoved(0, count)
//    }
//
//
//    fun moved(fromPosition: Int, toPosition: Int) {
//        if (fromPosition < 0 || (fromPosition > contentDatas.size - 1)) return
//        if (toPosition < 0 || (toPosition > contentDatas.size - 1)) return
//
//        val tempObj = contentDatas.get(fromPosition)
//        contentDatas.set(fromPosition, contentDatas.get(toPosition))
//        contentDatas.set(toPosition, tempObj)
//        notifyItemMoved(fromPosition, toPosition)
//    }
//
//}