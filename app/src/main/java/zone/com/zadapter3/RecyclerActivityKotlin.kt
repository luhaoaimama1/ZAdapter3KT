package zone.com.zadapter3

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.view.ViewGroup

import com.zone.adapter3.QuickRcvAdapter
import com.zone.adapter3.base.IAdapter

import java.util.ArrayList

import zone.com.zadapter3.adapter.LeftDelegates
import zone.com.zadapter3.adapter.RightDelegates

class RecyclerActivityKotlin : Activity(), Handler.Callback, View.OnClickListener {

    private var rv: RecyclerView? = null
    private val mDatas = ArrayList<String>()
    private var muliAdapter: IAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler)
        rv = findViewById(R.id.rv) as RecyclerView
        rv!!.layoutManager = GridLayoutManager(this, 3)
        //        for (int i = 1; i <= 100; i++) {
        //            mDatas.add("" +  i);
        //        }

        //base test
        val type = intent.getStringExtra("type")
        if ("Linear" == type) {
            rv!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            //            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        if ("Grid" == type) {
            rv!!.layoutManager = GridLayoutManager(this, 3)
        }
        if ("StaggeredGrid" == type) {
            rv!!.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        }
        rv!!.itemAnimator = DefaultItemAnimator()
        muliAdapter = QuickRcvAdapter<String>(this, mDatas)
                .addViewHolder(LeftDelegates())//默认
                .addViewHolder(0, LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, RightDelegates())//多部剧 注释开启即可
                //                .addHeaderHolder(R.layout.header_simple)
                //                .addFooterHolder(R.layout.footer_simple)
                .addEmptyHold(R.layout.empty)
                .relatedList(rv)
                .addItemDecoration(10)
                .setOnItemClickListener { parent, view, position -> println("被点击->onItemClick" + position) }
                .setOnItemLongClickListener { parent, view, position ->
                    println("被点击->onItemLongClick:" + position)
                    false
                }

        muliAdapter!!.notifyDataSetChanged()

    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_add -> {
                val str = "insert one no ani"
                mDatas.add(str)
                muliAdapter!!.notifyItemInsertedEx(mDatas.size - 1)
            }
            R.id.bt_aniAdd -> {
                mDatas.add(1, "insert one with ani")
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_remove -> if (mDatas.size > 1) {
                mDatas.removeAt(1)
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_removeAni -> if (mDatas.size > 1) {
                mDatas.removeAt(1)
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_clear -> {
                mDatas.clear()
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_addHeader -> {
                val mode = (Math.random() * 100 % 3).toInt()
                when (mode) {
                    0 -> muliAdapter!!.addHeaderHolder(R.layout.header_simple)
                    1 -> muliAdapter!!.addHeaderHolder(R.layout.header_simple2)
                    2 -> muliAdapter!!.addHeaderHolder(R.layout.header_simple3)
                }
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_addFooter -> {
                val mode2 = (Math.random() * 100 % 3).toInt()
                when (mode2) {
                    0 -> muliAdapter!!.addFooterHolder(R.layout.footer_simple)
                    1 -> muliAdapter!!.addFooterHolder(R.layout.footer_simple_img2)
                    2 -> muliAdapter!!.addFooterHolder(R.layout.footer_simple_img)
                }
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_clearFooter -> {
                muliAdapter!!.clearFooterHolder()
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_clearHeader -> {
                muliAdapter!!.clearHeaderHolder()
                muliAdapter!!.notifyDataSetChanged()
            }
            R.id.bt_addHeaderFirst -> {
                //todo   addHeaderHolder(0,id,true) 的话就有问题  如果没有数据就没有问题
                //                muliAdapter.addHeaderHolder(0,R.layout.header_simple3);
                muliAdapter!!.addHeaderHolder(0, R.layout.header_simple3, true)
                muliAdapter!!.scrollToPosition(0)
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
