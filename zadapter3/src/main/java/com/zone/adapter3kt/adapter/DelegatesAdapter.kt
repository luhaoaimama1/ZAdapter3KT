package com.zone.adapter3kt.adapter

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.delegate.DelegatesManager
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.delegate.ResDelegate
import com.zone.adapter3kt.delegate.ViewDelegate
import com.zone.adapter3kt.loadmore.LoadingSetting


/**
 *[2018] by Zone
 */


abstract class DelegatesAdapter<T>(context: Context) : BaseAdapter<T>(context) {

    val delegatesManager = DelegatesManager(this)

    fun registerDelegate(viewType: Int, delegate: ViewDelegate<*,*>) {
        if (viewType < 0 && viewType >= -100) throw IllegalStateException("view type -1 to -100 use by inner！")
        delegatesManager.registerDelegate(viewType, delegate as ViewDelegate<*, BaseHolder<RecyclerView.ViewHolder>>)
    }

    fun registerDelegate(viewType: Int, @LayoutRes layoutId: Int) {
        registerDelegate(viewType, ResDelegate<Any>(layoutId))
    }

    //default
    fun registerDelegate(delegate: ViewDelegate<*, *>) {
        delegatesManager.registerDelegate(DEFAULT_VALUE, delegate as ViewDelegate<*, BaseHolder<RecyclerView.ViewHolder>>)
    }

    fun registerDelegate(@LayoutRes layoutId: Int) {
        registerDelegate(ResDelegate<Any>(layoutId))
    }

    //empty
    fun registerEmpytDelegate(delegate: ViewDelegate<*,*>) {
        delegatesManager.registerDelegate(EMPTY_VALUE, delegate as ViewDelegate<*, BaseHolder<RecyclerView.ViewHolder>>)
    }

    fun registerEmpytDelegate(@LayoutRes layoutId: Int) {
        registerEmpytDelegate(ResDelegate<Any>(layoutId))
    }

    protected var loadingSetting: LoadingSetting = LoadingSetting()
    //loading
    fun registerLoadingDelegate(delegate: LoadMoreViewDelegate, loadingSetting: LoadingSetting? = null) {
        if (loadingSetting != null) this@DelegatesAdapter.loadingSetting = loadingSetting
        delegatesManager.registerDelegate(LOADING_VALUE, delegate)
    }

    // =======================================分割线=====================================

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder<RecyclerView.ViewHolder> {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(baseHolder: BaseHolder<RecyclerView.ViewHolder>?, position: Int) {}

    // =======================================分割线=====================================
    override fun onFailedToRecycleView(baseHolder: BaseHolder<RecyclerView.ViewHolder>): Boolean =
        delegatesManager.onFailedToRecycleView(baseHolder)

    override fun onViewDetachedFromWindow(baseHolder: BaseHolder<RecyclerView.ViewHolder>) {
        super.onViewDetachedFromWindow(baseHolder)
        delegatesManager.onViewDetachedFromWindow(baseHolder)
    }

    override fun onViewAttachedToWindow(baseHolder: BaseHolder<RecyclerView.ViewHolder>) {
        super.onViewAttachedToWindow(baseHolder)
        delegatesManager.onViewAttachedToWindow(baseHolder)
    }

    override fun onViewRecycled(baseHolder: BaseHolder<RecyclerView.ViewHolder>) {
        super.onViewRecycled(baseHolder)
        delegatesManager.onViewRecycled(baseHolder)
    }

}