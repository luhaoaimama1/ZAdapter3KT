package zone.com.zadapter3kt

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.QuickAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import kotlin.collections.ArrayList

class FastRecyclerActivity : Activity() {


    private lateinit var rv: RecyclerView
    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: QuickAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_fast_recycler)
        rv = findViewById<RecyclerView>(R.id.rv)
        for (i in 1..30) {
            mDatas.add("" + i)
        }
        //base test
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv.itemAnimator = DefaultItemAnimator()
        muliAdapter = QuickAdapter<String>(this@FastRecyclerActivity).apply {
            registerDelegate(LeftDelegates())
            add(mDatas)
        }
        rv.adapter = muliAdapter
        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
               outRect.bottom = 10
            }

        })
    }
}
