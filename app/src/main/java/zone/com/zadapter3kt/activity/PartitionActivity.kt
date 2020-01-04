package zone.com.zadapter3kt.activity

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.zone.adapter3kt.section.Section
import zone.com.zadapter3.R
import zone.com.zadapter3kt.common.PartitionAdapter

/**
 *[2018/11/15] by Zone
 */
class PartitionActivity : Activity() {


    private var rv: androidx.recyclerview.widget.RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(zone.com.zadapter3.R.layout.a_rt_recycler)
        rv = findViewById<View>(R.id.rv) as androidx.recyclerview.widget.RecyclerView
        rv!!.setBackgroundColor(Color.YELLOW)
        rv!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.VERTICAL, false)

        rv!!.adapter = PartitionAdapter(this).apply {
            for (i in 0..10) {
                addSection(Section(i)) {
                    add(PartitionAdapter.header)
                    add(PartitionAdapter.content)
                    add(PartitionAdapter.footer)
                }
                if (i != 10) {
                    add(PartitionAdapter.divder)
                }
            }
        }
    }

}
