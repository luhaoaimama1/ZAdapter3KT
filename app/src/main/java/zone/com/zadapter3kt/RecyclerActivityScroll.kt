package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import com.zone.adapter3kt.StickyAdapter
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

        go.setOnClickListener({
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
                        var smoothScroller = object : LinearSmoothScroller(this@RecyclerActivityScroll) {
                            override fun getVerticalSnapPreference(): Int = LinearSmoothScroller.SNAP_TO_START
                        }
                        smoothScroller.setTargetPosition(scollValue)
                        rv.getLayoutManager().startSmoothScroll(smoothScroller);
                    }
                    R.id.scrollToPositionWithOffset -> {
                        println("scollType:scrollToPositionWithOffset")
                        (rv.getLayoutManager() as LinearLayoutManager)
                                .scrollToPositionWithOffset(scollValue, 0)
                    }
                    R.id.scrollCustomMethodFirst -> {
                        println("scollType:scrollCustomMethodFirst")
                        moveToPosition(scollValue)
                    }
                }

            } catch(e: Exception) {
            }
        })

        rv.layoutManager = GridLayoutManager(this, 3)
        for (i in 0..50) {
            mDatas.add("" + i);
        }
        //base test
        rv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        muliAdapter = CommonAdapter(this@RecyclerActivityScroll).apply {
            add(mDatas)
        }
        rv.adapter = muliAdapter
    }

    private fun moveToPosition(n: Int) {
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        val firstItem = (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastItem =(rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
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
