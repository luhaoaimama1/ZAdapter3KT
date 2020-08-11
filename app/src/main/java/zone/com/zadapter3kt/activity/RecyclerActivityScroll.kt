package zone.com.zadapter3kt.activity

import android.app.Activity
import android.graphics.PointF
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.LinearSmoothScroller
import com.zone.adapter3kt.adapter.StickyAdapter
import kotlinx.android.synthetic.main.a_scroll_recycler.*
import zone.com.zadapter3.R
import java.util.ArrayList
import zone.com.zadapter3kt.common.CommonAdapter

class RecyclerActivityScroll : Activity(), Handler.Callback {

    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: StickyAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_scroll_recycler)

        go.setOnClickListener {
            try {
                val scollValue = editValue.text.toString().toInt()
                when (rg.checkedRadioButtonId) {
                    R.id.scrollTo -> {
                        println("scollType:scrollTo")
                        rv.scrollToPosition(scollValue)
                    }
                    R.id.smoothScroll -> {
                        println("scollType:smoothScroll")
                        rv.smoothScrollToPosition(scollValue)
                    }
                    R.id.scrollBy -> {
                        println("scollType:scrollBy")
                        rv.scrollBy(0, scollValue)
                    }
                    R.id.smoothScrollBy -> {
                        println("scollType:smoothScrollBy")
                        rv.smoothScrollBy(0, scollValue)
                    }
                    R.id.smoothScrollerScroll -> {
                        println("scollType:smoothScrollerScroll")
                        val smoothScroller = object : LinearSmoothScroller(this@RecyclerActivityScroll) {
                            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                                if (snapPreference == SNAP_TO_START) return boxStart - (viewStart - 100)
                                return super.calculateDtToFit(viewStart, viewEnd, boxStart, boxEnd, snapPreference)
                            }

                            override fun getVerticalSnapPreference(): Int = LinearSmoothScroller.SNAP_TO_START
                        }
                        smoothScroller.setTargetPosition(scollValue)
                        rv.getLayoutManager()?.startSmoothScroll(smoothScroller);
                    }
                    R.id.scrollToPositionWithOffset -> {
                        println("scollType:scrollToPositionWithOffset")
                        (rv.getLayoutManager() as androidx.recyclerview.widget.LinearLayoutManager)
                                .scrollToPositionWithOffset(scollValue, 50)
                    }
                    R.id.scrollCustomMethodFirst -> {
                        println("scollType:scrollCustomMethodFirst")
                        moveToPosition(scollValue)
                    }
                }

            } catch (e: Exception) {
            }
        }

        rv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        for (i in 0..50) {
            mDatas.add("" + i);
        }
        //base test
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        muliAdapter = CommonAdapter(this@RecyclerActivityScroll).apply {
            add(mDatas)
        }
        rv.adapter = muliAdapter
    }

    private fun moveToPosition(n: Int) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        val firstItem = (rv.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
        val lastItem = (rv.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findLastVisibleItemPosition()
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            rv.scrollToPosition(n)
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            val top = rv.getChildAt(n - firstItem).getTop()
            rv.scrollBy(0, top)
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            rv.scrollToPosition(n)
            //这里这个变量是用在RecyclerView滚动监听里面的
        }

    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
