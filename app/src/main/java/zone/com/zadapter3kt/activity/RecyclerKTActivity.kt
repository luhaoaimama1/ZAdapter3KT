package zone.com.zadapter3kt.activity

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.adapter.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.common.CommonAdapter
import kotlin.collections.ArrayList

class RecyclerKTActivity : Activity(), Handler.Callback, View.OnClickListener {
    companion object {
        @JvmStatic
        var addIndex = 0
    }

    private lateinit var rv: androidx.recyclerview.widget.RecyclerView
    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: StickyAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler_kt)
        rv = findViewById(R.id.rv) as androidx.recyclerview.widget.RecyclerView
        //base test
        val type = intent.getStringExtra("type")
        rv.layoutManager = when (type) {
            "Linear" -> androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
            "Grid" -> androidx.recyclerview.widget.GridLayoutManager(this, 3)
            "StaggeredGrid" -> androidx.recyclerview.widget.StaggeredGridLayoutManager(3, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
            else -> androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        }
        rv.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        muliAdapter = CommonAdapter(this@RecyclerKTActivity).apply {
            add(mDatas)
        }
        rv.adapter = muliAdapter
        addListMethod()//init 的时候能看到empty吗
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
