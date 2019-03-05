package zone.com.zadapter3kt.adapter

import com.zone.adapter3kt.data.DataWarp

import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class RightDiffDelegates : ViewDelegatesDemo<String>() {
    override var layoutId: Int = R.layout.item_right


    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        if (payloads.firstOrNull()?.equals("ChangePayload") ?: false)
            baseHolder.setText(R.id.tv, item.data!!)
    }

}
