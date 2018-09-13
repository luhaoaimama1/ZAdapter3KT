package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.widget.DefaultItemAnimator
import java.util.ArrayList
import butterknife.ButterKnife
import butterknife.OnClick
import com.zone.adapter3kt.StickyAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.data.HFMode
import kotlinx.android.synthetic.main.a_fully_recycler.*
import zone.com.zadapter3.R
import zone.com.zadapter3kt.layout.FullyLinearLayoutManager
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates

class FullyRecyclerKTActivity : Activity(), Handler.Callback {

    private lateinit var muliAdapter: StickyAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_fully_recycler)
        ButterKnife.bind(this)
        //        rv.setLayoutManager(new WrapHeightGridLayoutManager(this, 3));
        //        rv.setLayoutManager(new GridLayoutManager(this, 3));
        rv.layoutManager = FullyLinearLayoutManager(this)
        //        rv.setLayoutManager(new NFullyLinearLayoutManager(this));
        //        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.itemAnimator = DefaultItemAnimator()
        //        rv.addItemDecoration(new RecyclerView.ItemDecoration() {
        //            @Override
        //            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //                super.getItemOffsets(outRect, view, parent, state);
        //                outRect.left = 10;
        //                outRect.bottom = 10;
        //                outRect.top = 10;
        //                outRect.right = 10;
        //
        //            }
        //        });

        val mDatas = ArrayList<String>()
        for (i in 1..5) {
            mDatas.add("" + i)
        }
        muliAdapter = StickyAdapter<String>(this@FullyRecyclerKTActivity).apply {
            registerDelegate(LeftDelegates())
            registerDelegate(0, LeftDelegates())
            registerDelegate(1, RightDelegates())

            registerDelegate(3, R.layout.header_simple)
            registerDelegate(4, R.layout.header_simple2)
            registerDelegate(5, R.layout.header_simple3)

            registerDelegate(6, R.layout.footer_simple)
            registerDelegate(7, R.layout.footer_simple_img2)
            registerDelegate(8, R.layout.footer_simple_img)

            registerEmpytDelegate(R.layout.empty)

            defineHeaderOrder(HFMode.ADD, 3, 4, 5)
            defineFooterOrder(HFMode.ADD, 6, 7, 8)

            setStyleExtra(object : ViewStyleDefault<String>() {
                override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                    var viewStyle = when (item) {
                        "header1" -> 3
                        "header2" -> 4
                        "header3" -> 5
                        "footer1" -> 6
                        "footer2" -> 7
                        "footer3" -> 8
                        else -> -1
                    }
                    if (item.contains("insert one")) {
                        val index = item.substring("insert one item!+".length, item.length).toInt()
                        viewStyle = if (index % 2 == 0) 1 else 0
                    }
                    return ViewStyleOBJ().viewStyle(viewStyle)
                }

                override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
                }
            })
            add(mDatas)
        }
        rv.adapter = muliAdapter
    }

    companion object {
        @JvmStatic
        var addIndex = 0
    }

    @OnClick(R.id.bt_add)
    fun onClick2() {
        val content = "insert one item!+${addIndex++}"
        muliAdapter.add(content)
        muliAdapter.scrollTo(content)
    }

    @OnClick(R.id.bt_addFooter)
    fun onClick3() {
        val mode2 = (Math.random() * 100 % 3).toInt()
        muliAdapter.add("footer${mode2 + 1}")
        muliAdapter.scrollToLast()
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
