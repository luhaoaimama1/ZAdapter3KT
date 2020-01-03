package zone.com.zadapter3kt

import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.zone.adapter3kt.adapter.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.ImgDelegates

class NineRecyclerKTActivity : Activity() {

    private var rv: androidx.recyclerview.widget.RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(zone.com.zadapter3.R.layout.a_rt_recycler)
        rv = findViewById<View>(R.id.rv) as androidx.recyclerview.widget.RecyclerView
        rv!!.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)

        rv!!.adapter = StickyAdapter<String>(this).apply {
            enableHistory(true)
            registerDelegate( ImgDelegates())
            add("add")
        }
    }

}
