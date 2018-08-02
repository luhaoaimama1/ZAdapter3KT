//package zone.com.zadapter3
//
//import android.app.Activity
//import android.os.Bundle
//import android.os.Handler
//import android.os.Message
//import android.support.v7.widget.GridLayoutManager
//import android.support.v7.widget.LinearLayoutManager
//import android.support.v7.widget.LinearSmoothScroller
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import android.widget.CompoundButton
//import com.zone.adapter3.QuickRcvAdapter
//import com.zone.adapter3.base.IAdapter
//import kotlinx.android.synthetic.main.a_scroll_recycler.*
//import java.util.ArrayList
//import zone.com.zadapter3.adapter.LeftDelegates
//import zone.com.zadapter3.adapter.RightDelegates
//import zone.com.zadapter3.animator.DefaultItemAnimator
//
//class RecyclerActivityScroll2 : Activity(), Handler.Callback, CompoundButton.OnCheckedChangeListener {
//    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//
//        if (isChecked) {
//            when (buttonView) {
//                scrollTo -> {
//                    scrollState = SCROLL_TO
//                }
//                smoothScroll -> {
//                    scrollState = SMOOTH_SCROLL
//                }
//                scrollBy -> {
//                    scrollState = SCROLL_BY
//                }
//                smoothScrollBy -> {
//                    scrollState = SMOOTH_SCROLL_BY
//                }
//                smoothScrollerScroll -> {
//                    scrollState = SMOOTH_SCROLLER_SCROLL
//                }
//            }
//        }
//    }
//
//    final var SCROLL_TO = 0
//    final var SMOOTH_SCROLL = 1
//    final var SCROLL_BY = 2
//    final var SMOOTH_SCROLL_BY = 3
//    final var SMOOTH_SCROLLER_SCROLL = 4
//    var scrollState = 0
//
//    private var rv: RecyclerView? = null
//    private val mDatas = ArrayList<String>()
//    private var muliAdapter: IAdapter<String>? = null
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.a_scroll_recycler)
//
//        scrollTo.setOnCheckedChangeListener(this)
//        smoothScroll.setOnCheckedChangeListener(this)
//        scrollBy.setOnCheckedChangeListener(this)
//        smoothScrollBy.setOnCheckedChangeListener(this)
//        smoothScrollerScroll.setOnCheckedChangeListener(this)
//
//        go.setOnClickListener({
//            try {
//                var scollValue = editValue.text.toString().toInt()
//                when (scrollState) {
//                    SCROLL_TO -> {
//                        rv!!.scrollToPosition(scollValue)
//                    }
//                    SMOOTH_SCROLL -> {
//                        rv!!.smoothScrollToPosition(scollValue)
//                    }
//                    SCROLL_BY -> {
//                        rv!!.scrollBy(0, scollValue)
//                    }
//                    SMOOTH_SCROLL_BY -> {
//                        rv!!.smoothScrollBy(0, scollValue)
//                    }
//                    SMOOTH_SCROLLER_SCROLL -> {
//                        var smoothScroller = object : LinearSmoothScroller(this@RecyclerActivityScroll) {
//                            override fun getVerticalSnapPreference(): Int = LinearSmoothScroller.SNAP_TO_START
//                        }
//                        smoothScroller.setTargetPosition(scollValue)
//                        rv!!.getLayoutManager().startSmoothScroll(smoothScroller);
//                    }
//                }
//            } catch(e: Exception) {
//            }
//        })
//
//        rv = findViewById(R.id.rv) as RecyclerView
//        rv!!.layoutManager = GridLayoutManager(this, 3)
//        for (i in 0..50) {
//            mDatas.add("" + i);
//        }
//        //base test
//        rv!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        muliAdapter = object : QuickRcvAdapter<String>(this, mDatas) {
//            override fun getItemViewType2(dataPosition: Int): Int {
//                return if (dataPosition % 3 == 0) 1 else 0
//            }
//        }
//        muliAdapter!!.addViewHolder(LeftDelegates())//默认
//                .addViewHolder(0, LeftDelegates()) //多部剧 注释开启即可
//                .addViewHolder(1, RightDelegates())//多部剧 注释开启即可
//                //                .addHeaderHolder(R.layout.header_simple)
//                //                .addFooterHolder(R.layout.footer_simple)
//                .addEmptyHold(R.layout.empty)
//                .relatedList(rv)
//                .addItemDecoration(10)
//                .setOnItemClickListener { parent, view, position -> println("被点击->onItemClick" + position) }
//                .setOnItemLongClickListener { parent, view, position ->
//                    println("被点击->onItemLongClick:" + position)
//                    false
//                }
//        muliAdapter!!.notifyDataSetChanged()
//    }
//
//
//    override fun handleMessage(msg: Message): Boolean {
//        return false
//    }
//}
