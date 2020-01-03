package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.a_recycler.*
import zone.com.zadapter3.R
import java.util.ArrayList
import zone.com.zadapter3kt.animator.DefaultItemAnimator
import zone.com.zadapter3kt.common.CommonAdapter

class RecyclerActivityAnimator : Activity(), Handler.Callback, View.OnClickListener {
    companion object {
        @JvmStatic
        var addIndex = 0
    }

    private val mDatas = ArrayList<String>()
    private lateinit var muliAdapter: CommonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_recycler)
        rv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        for (i in 0..15) {
            mDatas.add("" + i);
        }
        //base test
        val type = intent.getStringExtra("type")
        rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)
        val animator = DefaultItemAnimator()
        animator.addDuration = 2000
        animator.changeDuration = 2000
        animator.removeDuration = 2000
        animator.moveDuration = 2000
        rv.itemAnimator = animator
        muliAdapter = CommonAdapter(this@RecyclerActivityAnimator).apply {
            add(mDatas)
        }
        rv.adapter = muliAdapter
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt_add -> {
                val str = "insert one item!+${addIndex++}"
                muliAdapter.add(str)
                muliAdapter.scrollTo(str)
            }
            R.id.bt_remove -> {
                muliAdapter.remove(muliAdapter.itemCount-1)
            }
            R.id.bt_clear -> {
                muliAdapter.clearAll()
            }
            R.id.bt_addHeader -> {
                val mode = (Math.random() * 100 % 3).toInt()
                muliAdapter.add("header${mode + 1}")
                rv!!.scrollBy(0, -40)
                Toast.makeText(this,"scollby -40",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        return false
    }
}
