package zone.com.zadapter3

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zone.adapter3.QuickRcvAdapter
import com.zone.adapter3.base.IAdapter
import java.util.ArrayList
import zone.com.zadapter3.adapter.LeftDelegates
import zone.com.zadapter3.adapter.RightDelegates
import zone.com.zadapter3.animator.DefaultItemAnimator

class RecyclerActivityAnimator : Activity(), Handler.Callback, View.OnClickListener {

    private var rv: RecyclerView? = null
    private val mDatas = ArrayList<String>()
    private var muliAdapter: IAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler)
        rv = findViewById(R.id.rv) as RecyclerView
        rv!!.layoutManager = GridLayoutManager(this, 3)
        for (i in 0..15) {
            mDatas.add("" + i);
        }
        //base test
        val type = intent.getStringExtra("type")
        rv!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val animator = DefaultItemAnimator()
        animator.addDuration = 2000
        animator.changeDuration = 2000
        animator.removeDuration = 2000
        animator.moveDuration = 2000
        rv!!.itemAnimator = animator
        muliAdapter = object : QuickRcvAdapter<String>(this, mDatas) {
            override fun getItemViewType2(dataPosition: Int): Int{
                return if (dataPosition % 3 == 0) 1 else 0
            }
        }
        muliAdapter!!.addViewHolder(LeftDelegates())//默认
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
                val str = "insert one  ani"
                mDatas.add(str)
                muliAdapter!!.notifyItemInsertedEx(mDatas.size - 1)
                muliAdapter!!.scrollToLast()
            }
            R.id.bt_remove -> {
                mDatas.add("insert one with noScroll")
                muliAdapter!!.notifyItemInsertedEx(mDatas.size - 1)
            }
            R.id.bt_clear -> {
                mDatas.add("insert one with notifyDataSetChanged?")
                muliAdapter!!.notifyDataSetChanged()
                muliAdapter!!.scrollToPosition(mDatas.size - 1)
//                muliAdapter!!.scrollToLast()
            }
            R.id.bt_addHeader -> {
                mDatas.add("insert one scollby -10")
                muliAdapter!!.notifyItemInsertedEx(mDatas.size - 1)
                rv!!.scrollBy(0, -40)
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
