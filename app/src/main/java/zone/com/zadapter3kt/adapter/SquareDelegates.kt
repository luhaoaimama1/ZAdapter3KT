package zone.com.zadapter3kt.adapter

import android.view.View
import android.view.ViewGroup

import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.adapter.StickyAdapter
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

import zone.com.zadapter3.R

/**
 * [2017] by Zone
 */

class SquareDelegates : ViewDelegate<Int>() {

    var count=0;
    override val layoutId: Int = R.layout.item_square

    override fun initData(convertView: View, parent: ViewGroup) {
        super.initData(convertView, parent)
        println("Zone 创建SquareDelegates 个数:${count++}")
    }

    override fun onBindViewHolder(position: Int, item: DataWarp<Int>, holder: Holder, payloads: List<*>) {
        item.data?.apply {
            val lp = holder.getView<View>(R.id.ll_main).layoutParams
            lp.height = this
            holder.getView<View>(R.id.ll_main).layoutParams=lp
            holder.setText(R.id.ll_main,"$this")
        }
    }
}
