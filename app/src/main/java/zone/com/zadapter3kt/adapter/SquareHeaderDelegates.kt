package zone.com.zadapter3kt.adapter

import android.view.View
import android.view.ViewGroup

import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

import zone.com.zadapter3.R
import zone.com.zadapter3kt.ChangeLayoutNestRecyclerActivity
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


    //todo bug！！ 点击找不到holder
//    override fun registerClickListener(): Array<Int>? {
//        return arrayOf(R.id.ll_main)
//    }


//    override fun onClick(v: View?, viewHolder: Holder, posi: Int, item: DataWarp<Int>) {
//        super.onClick(v, viewHolder, posi, item)
//        v?.id?.apply {
//            when (this) {
//                R.id.ll_main -> {
//                    activity.changeLayout()
//                }
//            }
//        }
//    }
}
