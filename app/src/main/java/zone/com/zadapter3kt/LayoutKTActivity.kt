package zone.com.zadapter3kt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import java.util.Random

import zone.com.zadapter3.R
import zone.com.zadapter3kt.layoutmanager.CardItemView
import zone.com.zadapter3kt.layoutmanager.CardLayoutManager

class LayoutKTActivity : AppCompatActivity() {

    private val COLORS = intArrayOf(-0xff0001, -0x214779, -0xa06160,
        -0x800100, -0x9b6a13, -0x23ebc4,
        -0xff7475, -0xff9c00, -0xd0b0b1,
        -0x964c, -0xff01, -0x32a3a4,
        -0x6f1170, -0x783106, -0x800000)
    private var mRecyclerView: androidx.recyclerview.widget.RecyclerView? = null
    private val mAdapter = Adapter()

    private var mCount = 50
    private var mGroupSize = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mRecyclerView = findViewById(R.id.list) as androidx.recyclerview.widget.RecyclerView
        init()
    }

    private fun init() {
        mRecyclerView!!.layoutManager = CardLayoutManager(mGroupSize, true)
        mRecyclerView!!.adapter = mAdapter
    }

    fun add(view: View) {
        mCount += 10
        mAdapter.notifyDataSetChanged()
    }

    fun change(view: View) {
        if (mGroupSize == 3) {
            mGroupSize = 9
        } else {
            mGroupSize = 3
        }
        init()
    }

    internal inner class Adapter : androidx.recyclerview.widget.RecyclerView.Adapter<Adapter.Holder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val item = LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
            return Holder(item)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            //            holder.item.setText("" + position);
            holder.item.setCardColor(randomColor())
            holder.text.text = "菜单$position"
            holder.item.setOnClickListener { Toast.makeText(this@LayoutKTActivity, holder.text.text, Toast.LENGTH_SHORT).show() }
        }

        override fun getItemCount(): Int {
            return mCount
        }

        private fun randomColor(): Int {
            return COLORS[Random().nextInt(COLORS.size)]
        }

        internal inner class Holder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
            var item: CardItemView
            var text: TextView

            init {
                item = itemView.findViewById<View>(R.id.item) as CardItemView
                text = itemView.findViewById<View>(R.id.text) as TextView
            }
        }
    }

}
