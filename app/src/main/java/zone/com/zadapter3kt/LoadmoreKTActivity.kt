package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.LinearLayoutManager
import java.util.ArrayList
import com.zone.adapter3kt.Part
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.loadmore.OnScrollRcvListener
import kotlinx.android.synthetic.main.a_recycler_loadmore.*
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates

class LoadmoreKTActivity : Activity(), Handler.Callback {

    private val mDatas = ArrayList<String>()
    lateinit var mAdapter: QuickAdapter<String>
    private var handler: Handler? = null

    var loadMoreCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handler = Handler(this)
        setContentView(R.layout.a_recycler_loadmore)

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
        rv.layoutManager =LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        rv.layoutManager = GridLayoutManager(this, 3)
        mAdapter = QuickAdapter<String>(this@LoadmoreKTActivity).apply {
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

            loadOnScrollListener = object :OnScrollRcvListener(){

                override fun onLoading() {
                    super.onLoading()
                    loadMoreData()
                }
            }
            add(mDatas)
        }
        rv.adapter = mAdapter
    }
    var newDataCount = 0

    private fun loadMoreData() {
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

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
