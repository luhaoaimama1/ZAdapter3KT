package zone.com.zadapter3kt

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.adapter.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.common.CommonAdapter
import kotlin.collections.ArrayList

class FastRecyclerActivity : Activity() {


    private lateinit var rv: androidx.recyclerview.widget.RecyclerView
    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: QuickAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_fast_recycler)
        rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rv)
        for (i in 1..30) {
            mDatas.add("" + i)
        }
        //base test
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rv.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        muliAdapter = QuickAdapter<String>(this@FastRecyclerActivity).apply {
            registerDelegate(LeftDelegates())
            add(mDatas)
        }
        rv.adapter = muliAdapter
        rv.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.bottom = 10
            }
        })
    }

}
