package zone.com.zadapter3kt

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
import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.*
import com.zone.adapter3kt.adapter.OnItemClickListener
import com.zone.adapter3kt.data.HFMode
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates
import kotlin.collections.ArrayList

class RecyclerKTActivity : Activity(), Handler.Callback, View.OnClickListener {

    private lateinit var rv: RecyclerView
    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: StickyAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler_kt)
        rv = findViewById(R.id.rv) as RecyclerView
        //base test
        val type = intent.getStringExtra("type")
        rv.layoutManager = when (type) {
            "Linear" -> LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            "Grid" -> GridLayoutManager(this, 3)
            "StaggeredGrid" -> StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            else -> LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }
        rv.itemAnimator = DefaultItemAnimator()
//        return if (dataPosition % 3 == 0) 1 else 0
        muliAdapter = StickyAdapter<String>(this@RecyclerKTActivity)
        muliAdapter.registerDelegate(LeftDelegates())
        muliAdapter.registerDelegate(0, LeftDelegates())
        muliAdapter.registerDelegate(1, RightDelegates())

        muliAdapter.registerDelegate(3, R.layout.header_simple)
        muliAdapter.registerDelegate(4, R.layout.header_simple2)
        muliAdapter.registerDelegate(5, R.layout.header_simple3)

        muliAdapter.registerDelegate(6, R.layout.footer_simple)
        muliAdapter.registerDelegate(7, R.layout.footer_simple_img2)
        muliAdapter.registerDelegate(8, R.layout.footer_simple_img)
        muliAdapter.defineHeaderOrder(HFMode.ADD, 3, 4, 5)
        muliAdapter.defineFooterOrder(HFMode.ADD, 6, 7, 8)

        muliAdapter.registerEmpytDelegate(R.layout.empty)

        muliAdapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                println("被点击->onItemClick" + position)
            }
        }
        muliAdapter.add(mDatas)
        muliAdapter.setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                var viewStyle = when (item) {
                    "header1" -> 3
                    "header2" -> 4
                    "header3" -> 5
                    "footer1" -> 6
                    "footer2" -> 7
                    "footer3" -> 8
                    else -> -1
                }
                if (item.contains("insert one")) {
                    val index = item.substring("insert one item!+".length, item.length).toInt()
                    viewStyle = if (index % 3 == 0) 1 else 0
                }
                return ViewStyleOBJ().viewStyle(viewStyle)
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })
        muliAdapter.enableHistory(true)
        rv.adapter = muliAdapter
        addListMethod()//init 的时候能看到empty吗
    }

    companion object {
        @JvmStatic
        var addIndex = 0
    }

    override fun onClick(v: View) {
        QuickConfig.d("--------------click-----------------")
        when (v.id) {
            R.id.bt_add -> {
                val content = "insert one item!+${addIndex++}"
                muliAdapter.add(content)
            }
            R.id.bt_addList -> {
                addListMethod()
            }
            R.id.bt_remove -> if (muliAdapter.itemCount > 0) muliAdapter.remove(0)
            R.id.bt_clear -> muliAdapter.clearAll()
            R.id.bt_addHeader -> {
                val mode = (Math.random() * 100 % 3).toInt()
                muliAdapter.add("header${mode + 1}")
            }
            R.id.bt_addFooter -> {
                val mode2 = (Math.random() * 100 % 3).toInt()
                muliAdapter.add("footer${mode2 + 1}")
                addIndex = 0
            }
            R.id.bt_clearFooter -> {
                muliAdapter.clearFooterDatas()
            }
            R.id.bt_clearHeader -> {
                muliAdapter.clearHeaderDatas()
            }
        }
    }

    fun addListMethod() {
        val list = ArrayList<String>()
        val mode = (Math.random() * 100 % 3).toInt()
        list.add("header${mode + 1}")
        for (i in 0..4) {
            val content = "insert one item!+${addIndex++}"
            list.add(content)
        }
        muliAdapter.add(list)
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
