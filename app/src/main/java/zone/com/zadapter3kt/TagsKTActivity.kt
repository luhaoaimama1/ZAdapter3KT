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
import zone.com.zadapter3kt.common.TagsAdapter

/**
 * [2017] by Zone
 */
class TagsKTActivity : Activity() {

    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: StickyAdapter<String>
    private var cb: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler_absorb)
        rv.layoutManager = LinearLayoutManager(this)
        for (i in 1..100) mDatas.add("" + i)

        muliAdapter = TagsAdapter(this)
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
        cb!!.visibility=View.GONE
    }
}
