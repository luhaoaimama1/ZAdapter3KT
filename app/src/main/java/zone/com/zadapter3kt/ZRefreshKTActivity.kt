package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import java.util.ArrayList
import com.zone.adapter3kt.Part
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import kotlinx.android.synthetic.main.a_recycler_zrefresh.*
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates
import zone.com.zadapter3kt.helper.OnScrollRcvListenerExZRefresh
import zone.com.zrefreshlayout.ZRefreshLayout

class ZRefreshKTActivity : Activity(), Handler.Callback {

    private val mDatas = ArrayList<String>()
    lateinit var mAdapter: QuickAdapter<String>
    private var handler: Handler? = null

    var loadMoreCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(this)
        setContentView(R.layout.a_recycler_zrefresh)

        //没铺满 测试成功不触发loading
//        for (i in 0..5) {
//            mDatas.add("" + i)
//        }

        for (i in 0..25) {
            mDatas.add("" + i)
        }
//        for (i in 0..50) {
//            mDatas.add("" + i)
//        }

        //base test
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
//        rv.layoutManager = GridLayoutManager(this, 3)
        mAdapter = QuickAdapter<String>(this@ZRefreshKTActivity).apply {
            enableLoadMore = true
            setStyleExtra(object : ViewStyleDefault<String>() {
                override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
                    super.getItemViewType(position, itemConfig)
                    if (itemConfig.part != Part.OTHER) itemConfig.viewStyle = position % 2
                }
            })
            registerDelegate(LeftDelegates())
            registerDelegate(0, LeftDelegates())
            registerDelegate(1, RightDelegates())
            registerEmpytDelegate(R.layout.empty)
//            registerLoadingDelegate(BaseLoadMoreDelegates(), LoadingSetting().apply {
//                threshold = 0
//                isScrollToLoadData = true
//            })

            loadOnScrollListener = OnScrollRcvListenerExZRefresh(refresh)
            add(mDatas)
        }
        rv.adapter = mAdapter
        initRefresh()
    }

    var newDataCount = 0
    private fun initRefresh() {
        //更改第一个参数,可以看到 是否委托给外面的监听
        refresh.setLoadMoreListener(true, object : ZRefreshLayout.LoadMoreListener {
            override fun loadMore(zRefreshLayout: ZRefreshLayout) {
                handler!!.postDelayed({
                    when (loadMoreCount) {
                        1 -> {
                            mAdapter.loadMoreFail()
                        }
                        2 -> {
                            //预加载的方式 ，这里可以不用滚动
                            val items = ArrayList<String>()
                            for (i in 0..2) {
                                items.add("new Data${newDataCount++}")
                            }
                            mAdapter.add(items)
                            mAdapter.scrollTo(items[0])
                            mAdapter.loadMoreComplete()
                        }
                        else -> {
                            mAdapter.loadMoreEnd()
                        }
                    }
                    loadMoreCount++
                }, 2000)
            }

        })
        refresh.pullListener = object : ZRefreshLayout.PullListener {
            override fun refresh(zRefreshLayout: ZRefreshLayout) {
                refresh.isCanLoadMore = true
                loadMoreCount = 1
                handler!!.postDelayed({
                    mAdapter.add("Refresh Complete!")
                    refresh!!.refreshComplete()
                }, 2000)
            }

        }
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
