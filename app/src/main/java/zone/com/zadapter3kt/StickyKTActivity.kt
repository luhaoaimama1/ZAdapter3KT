package zone.com.zadapter3kt

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import com.zone.adapter3kt.adapter.StickyAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import kotlinx.android.synthetic.main.a_recycler_absorb.*
import java.util.ArrayList
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.RightDelegates

/**
 * [2017] by Zone
 */
class StickyKTActivity : Activity() {

    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: StickyAdapter<String>
    private var cb: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler_absorb)
        rv.layoutManager = LinearLayoutManager(this)
        for (i in 1..100) mDatas.add("" + i)

        muliAdapter = StickyAdapter<String>(this).apply {
            enableSticky = true
            registerDelegate(RightDelegates())
            registerDelegate(0, RightDelegates())
            registerDelegate(1, R.layout.item_absorb)
            registerDelegate(2, R.layout.item_absorb2)
            registerDelegate(3, R.layout.item_absorb3)

            registerEmpytDelegate(R.layout.empty)

            setStyleExtra(object : ViewStyleDefault<String>() {
                override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                    val isSticky = when (item) {
                        "13" -> true
                        "15" -> true
                        "40" -> true
                        "80" -> true
                        else -> false
                    }
                    val viewStyle = when (item) {
                        "13" -> 1
                        "15" -> 1
                        "40" -> 2
                        "80" -> 3
                        else -> -1
                    }
                    return ViewStyleOBJ().isSticky(isSticky).viewStyle(viewStyle)
                }
            })

        }
        muliAdapter.add(mDatas)
        rv.adapter = muliAdapter
        rv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect?.left = 10
                outRect?.right = 10
                outRect?.bottom = 10
            }
        })

        cb = findViewById<View>(R.id.cb) as CheckBox
        if ("debug" == intent.getStringExtra("type")) {
//            muliAdapter.setSticky(Color.BLUE, vp, 13, 16, 19)
            muliAdapter.setStickyShowFrameLayout(vp, Color.RED)
            vp!!.layoutParams.width = 700
            cb!!.performClick()
        } else {
            muliAdapter.setStickyShowFrameLayout(vp)
        }
        cb!!.setOnCheckedChangeListener { buttonView, isChecked ->
            val intent = Intent(this@StickyKTActivity, StickyKTActivity::class.java)
            if (isChecked) intent.putExtra("type", "debug")
            startActivity(intent)
            finish()
        }
    }
}
