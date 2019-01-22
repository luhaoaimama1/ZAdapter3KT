package zone.com.zadapter3kt

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.adapter.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.LeftOnclickDelegates
import zone.com.zadapter3kt.common.CommonAdapter
import kotlin.collections.ArrayList

class OnclickRecyclerActivity : Activity() {

    private lateinit var rv: RecyclerView
    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: QuickAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_fast_recycler)
        rv = findViewById(R.id.rv)
        for (i in 1..30) {
            mDatas.add("" + i)
        }
        //base test
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.itemAnimator = DefaultItemAnimator()
        muliAdapter = QuickAdapter<String>(this@OnclickRecyclerActivity).apply {
            registerDelegate(LeftOnclickDelegates())
            add(mDatas)
        }
        rv.adapter = muliAdapter
        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.apply {
                    outRect.bottom = 10
                }
            }
        })
    }

}
