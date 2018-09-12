package com.zone.adapter3kt.delegate.done

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zone.adapter.R
import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.adapter.LoadMoreAdapter
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.LoadMoreViewDelegate
import com.zone.adapter3kt.loadmore.LoadingState

/**
 * [2017] by Zone
 */

class BaseLoadMoreDelegates : LoadMoreViewDelegate() {
    override val layoutId: Int = R.layout.vp
    private var loadingView: View? = null
    private var failView: View? = null
    private var endView: View? = null

    override fun onBindViewHolder(position: Int, item: DataWarp<Any>, holder: Holder, payloads: List<*>) {}


    override fun loading() {
        loadingView?.visibility = View.VISIBLE
        failView?.visibility = View.GONE
        endView?.visibility = View.GONE
    }

    override fun complete() {
        loadingView?.visibility = View.GONE
        failView?.visibility = View.GONE
        endView?.visibility = View.GONE
    }

    override fun fail() {
        loadingView?.visibility = View.GONE
        failView?.visibility = View.VISIBLE
        endView?.visibility = View.GONE
    }

    override fun end() {
        loadingView?.visibility = View.GONE
        failView?.visibility = View.GONE
        endView?.visibility = View.VISIBLE
    }

    override fun initData(convertView: View, rv: ViewGroup) {
        val vp = convertView as ViewGroup
        loadingView = LayoutInflater.from(convertView.getContext())
            .inflate(R.layout.sample_common_list_footer_loading, rv, false)
        failView = LayoutInflater.from(convertView.getContext())
            .inflate(R.layout.sample_common_list_footer_network_error, rv, false)
        failView!!.setOnClickListener {
            if (rv is RecyclerView && rv.adapter is LoadMoreAdapter<*>){
                (rv.adapter as LoadMoreAdapter<*>).loadOnScrollListener?.loadMore(rv)
            }
        }
        endView = LayoutInflater.from(convertView.getContext())
            .inflate(R.layout.sample_common_list_footer_end, rv, false)
        vp.addView(loadingView)
        vp.addView(failView)
        vp.addView(endView)
        if (adapter is LoadMoreAdapter) {
            when ((adapter as LoadMoreAdapter<*>).loadOnScrollListener!!.loadingStateInner) {
                LoadingState.NO_SHOW -> {
                }
                LoadingState.LOADING -> loading()
                LoadingState.END -> end()
                LoadingState.FAIL -> fail()
                LoadingState.COMPLETE -> complete()
            }
        }

    }
}
