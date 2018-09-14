package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.zone.adapter3kt.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.ImgDelegates

class NineRecyclerKTActivity : Activity() {

    private var rv: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(zone.com.zadapter3.R.layout.a_rt_recycler)
        rv = findViewById<View>(R.id.rv) as RecyclerView
        rv!!.layoutManager = GridLayoutManager(this, 3)

        rv!!.adapter = StickyAdapter<String>(this,"NineRecyclerKTActivity").apply {
            enableHistory(true)
            registerDelegate( ImgDelegates())
            add("add")
        }
    }

}