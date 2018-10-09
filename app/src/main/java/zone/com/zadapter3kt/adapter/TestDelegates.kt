package zone.com.zadapter3kt.adapter

import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

/**
 * [2017] by Zone
 */

class TestDelegates : ViewDelegate<String>() {

    override val layoutId: Int=0

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: Holder, payloads: List<*>) {

    }
}
