package zone.com.zadapter3kt.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.zone.adapter3kt.adapter.StickyAdapter
import kotlinx.android.synthetic.main.a_recycler_absorb.*
import java.util.ArrayList
import zone.com.zadapter3.R
import zone.com.zadapter3kt.common.DivdersAdapter

/**
 * [2017] by Zone
 */
class DivderKTActivity : Activity() {

    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: DivdersAdapter
    private var cb: CheckBox? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler_absorb)
        rv.layoutManager = LinearLayoutManager(this)
        for (i in 1..40) mDatas.add("" + i)

        muliAdapter = DivdersAdapter(this)
        muliAdapter.add(mDatas)
        rv.adapter = muliAdapter
        rv.postDelayed({
            val posi = muliAdapter.findFirstPositionByExculdeCardType(0, 2, 3)
            Toast.makeText(this@DivderKTActivity, "排除头和底部、左边 的第一个位置：$posi", Toast.LENGTH_SHORT).show()
        }, 500)

        cb = findViewById<View>(R.id.cb) as CheckBox
        cb!!.visibility = View.GONE
    }
}
