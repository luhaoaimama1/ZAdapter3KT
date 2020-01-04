package zone.com.zadapter3kt.adapter

import android.view.View
import android.view.ViewGroup

import com.zone.adapter3kt.data.DataWarp

import zone.com.zadapter3.R
import zone.com.zadapter3kt.activity.ChangeLayoutNestRecyclerActivity
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class SquareHeaderDelegates(val activity: ChangeLayoutNestRecyclerActivity) : ViewDelegatesDemo<Int>() {


    override val layoutId: Int = R.layout.item_square_header

    override fun initData(convertView: View, parent: ViewGroup) {
        super.initData(convertView, parent)
        convertView.findViewById<View>(R.id.ll_main).setOnClickListener {
            activity.changeLayout()
        }
    }

    override fun onBindViewHolder(position: Int, item: DataWarp<Int>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        print("")
    }
}
