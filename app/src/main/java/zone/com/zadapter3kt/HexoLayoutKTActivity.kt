package zone.com.zadapter3kt

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import zone.com.zadapter3.R
import zone.com.zadapter3kt.layoutmanager.HexoItemView
import zone.com.zadapter3kt.layoutmanager.ZLayoutManager

class HexoLayoutKTActivity : AppCompatActivity() {
    private val COLORS = intArrayOf(-0x5657, -0x481901, -0xbf7f)
    private var mRecyclerView: RecyclerView? = null
    private val mAdapter = Adapter()

    private var mCount = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.bt).visibility = View.GONE
        mRecyclerView = findViewById(R.id.list) as RecyclerView
        init()
    }

    private fun init() {
        mRecyclerView!!.layoutManager = ZLayoutManager()
        mRecyclerView!!.adapter = mAdapter
    }

    fun add(view: View) {
        mCount++
        mAdapter.notifyDataSetChanged()
    }

    fun change(view: View) {
        init()
    }

    internal inner class Adapter : RecyclerView.Adapter<Adapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hexo, parent, false)
            return Holder(item)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.item.setCardColor(COLORS[position % COLORS.size])
            holder.text.text = "菜单$position"
            holder.item.setOnClickListener { Toast.makeText(this@HexoLayoutKTActivity, holder.text.text, Toast.LENGTH_SHORT).show() }
        }

        override fun getItemCount(): Int {
            return mCount
        }

        internal inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var item: HexoItemView
            var text: TextView

            init {
                item = itemView.findViewById<View>(R.id.item) as HexoItemView
                text = itemView.findViewById<View>(R.id.text) as TextView
            }
        }
    }

}
