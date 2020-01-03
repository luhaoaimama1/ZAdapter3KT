package zone.com.zadapter3kt

import android.app.Activity
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.appcompat.widget.*
import android.view.View
import android.widget.ImageView
import com.zone.adapter3kt.adapter.StickyAdapter
import kotlinx.android.synthetic.main.a_scroll_recycler.*
import zone.com.zadapter3.R
import java.util.ArrayList
import zone.com.zadapter3kt.common.NestAdapter

class ChangeLayoutNestRecyclerActivity : Activity(), Handler.Callback {

    var isLinear = true
    private val mDatas = ArrayList<Int>()
    private lateinit var muliAdapter: StickyAdapter<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_scroll_recycler_nest)

        rv.addItemDecoration(value1)
        addDatas()
        //base test
//        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        muliAdapter = NestAdapter(this@ChangeLayoutNestRecyclerActivity).apply {
            add(Int.MAX_VALUE)
            add(mDatas)
        }
        changeLayout()
        rv.addOnScrollListener(value)
        rv.adapter = muliAdapter
    }

    fun addDatas() {
        for (i in 0..30) {
            val height = 100 + Math.random() * 300
            mDatas.add(height.toInt())
        }
    }

    val value = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
//            if (recyclerView!!.layoutManager is StaggeredGridLayoutManager)
//                (recyclerView.layoutManager as StaggeredGridLayoutManager).invalidateSpanAssignments()
        }

        override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }
    }
    val value1 = object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            val divider = 30
            if(rv.layoutManager is androidx.recyclerview.widget.StaggeredGridLayoutManager){
                val lp = view.layoutParams as androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams
                outRect.top = divider
                if (lp.spanIndex == 0) {
                    // left
                    outRect.left = divider;
                    outRect.right = divider / 2;
                } else {
                    outRect.right = divider;
                    outRect.left = divider / 2;
                }
            }
            if(rv.layoutManager is androidx.recyclerview.widget.LinearLayoutManager){
                outRect.bottom = divider;
            }
        }
    }


    fun changeLayout() {
//        muliAdapter.clearContentDatas()
        isLinear = !isLinear
        if (isLinear) {
            rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        } else {
            rv.layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(2, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL)
        }
        addDatas()
        muliAdapter.add(mDatas)
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
