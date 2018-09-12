package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.ViewGroup
import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.delegate.DelegatesManager
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.delegate.ResDelegate
import com.zone.adapter3kt.delegate.ViewDelegate


/**
 *[2018] by Zone
 */


enum class LoadMode { LOAD, PRE_LOAD; }

abstract class DelegatesAdapter<T>(context: Context) : BaseAdapter<T>(context) {

    val delegatesManager = DelegatesManager(this)
    var loadMode: LoadMode? = null

    fun registerDelegate(viewType: Int, delegate: ViewDelegate<*>) {
        if (viewType < 0 && viewType >= -100) throw IllegalStateException("view type -1 to -100 use by inner！")
        delegatesManager.registerDelegate(viewType, delegate)
    }

    fun registerDelegate(viewType: Int, @LayoutRes layoutId: Int) {
        registerDelegate(viewType, ResDelegate<Any>(layoutId))
    }

    //default
    fun registerDelegate(delegate: ViewDelegate<*>) {
        delegatesManager.registerDelegate(DEFAULT_VALUE, delegate)
    }

    fun registerDelegate(@LayoutRes layoutId: Int) {
        registerDelegate(ResDelegate<Any>(layoutId))
    }

    //empty
    fun registerEmpytDelegate(delegate: ViewDelegate<*>) {
        delegatesManager.registerDelegate(EMPTY_VALUE, delegate)
    }

    fun registerEmpytDelegate(@LayoutRes layoutId: Int) {
        registerEmpytDelegate(ResDelegate<Any>(layoutId))
    }

    //loading
    fun registerLoadingDelegate(loadMode: LoadMode, delegate: LoadMoreViewDelegate) {
        this@DelegatesAdapter.loadMode = loadMode
        delegatesManager.registerDelegate(LOADING_VALUE, delegate)
    }

    // =======================================分割线=====================================

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }
    override fun onBindViewHolder(holder: Holder?, position: Int) {}

    // =======================================分割线=====================================
    override fun onFailedToRecycleView(holder: Holder): Boolean =
        delegatesManager.onFailedToRecycleView(holder)

    override fun onViewDetachedFromWindow(holder: Holder) {
        super.onViewDetachedFromWindow(holder)
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun onViewAttachedToWindow(holder: Holder) {
        super.onViewAttachedToWindow(holder)
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        delegatesManager.onViewRecycled(holder)
    }

}