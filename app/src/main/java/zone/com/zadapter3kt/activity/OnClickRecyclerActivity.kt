package zone.com.zadapter3kt.activity

import android.app.Activity
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zone.adapter3kt.QuickAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftOnclickDelegates
import kotlin.collections.ArrayList

class OnClickRecyclerActivity : Activity() {

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
        muliAdapter = QuickAdapter<String>(this@OnClickRecyclerActivity).apply {
            registerDelegate(LeftOnclickDelegates())
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
