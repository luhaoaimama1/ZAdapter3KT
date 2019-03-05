package zone.com.zadapter3kt.adapter

import android.view.View
import com.zone.adapter3kt.QuickConfig

import com.zone.adapter3kt.data.DataWarp
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class LeftDelegates : ViewDelegatesDemo<String>() {
    override val layoutId: Int= R.layout.item_left

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        baseHolder.setText(R.id.tv, item.data!!)
        baseHolder.itemView.post { QuickConfig.e("height" + baseHolder.itemView.height) }
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        baseHolder.setText(R.id.tv, item.data!!)
                .setOnClickListener(View.OnClickListener { println("holder click测试 ") })
    }

}
