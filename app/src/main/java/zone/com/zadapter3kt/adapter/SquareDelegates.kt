package zone.com.zadapter3kt.adapter

import android.view.View
import android.view.ViewGroup

import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.data.DataWarp

import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class SquareDelegates : ViewDelegatesDemo<Int>() {

    var count=0;
    override val layoutId: Int = R.layout.item_square

    override fun initData(convertView: View, parent: ViewGroup) {
        super.initData(convertView, parent)
        println("Zone 创建SquareDelegates 个数:${count++}")
    }

    override fun onBindViewHolder(position: Int, item: DataWarp<Int>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        item.data?.apply {
            val lp = baseHolder.getView<View>(R.id.ll_main).layoutParams
            lp.height = this
            baseHolder.getView<View>(R.id.ll_main).layoutParams=lp
            baseHolder.setText(R.id.ll_main,"$this")
        }
    }
}
