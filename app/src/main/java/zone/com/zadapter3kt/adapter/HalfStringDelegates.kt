package zone.com.zadapter3kt.adapter

import android.view.View
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.data.DataWarp
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 *[2019/4/17] by Zone
 */

class HalfStringDelegates : ViewDelegatesDemo<String>() {
    override val layoutId: Int= R.layout.item_half_string

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        baseHolder.setText(R.id.tv, item.data!!)
    }

}
