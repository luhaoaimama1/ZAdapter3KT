package zone.com.zadapter3kt.adapter

import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

import zone.com.zadapter3.R

/**
 * [2017] by Zone
 */

class RightDiffDelegates : ViewDelegate<String>() {
    override var layoutId: Int = R.layout.item_right


    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: com.zone.adapter3kt.Holder, payloads: List<*>) {
        if (payloads.firstOrNull()?.equals("ChangePayload") ?: false)
            holder.setText(R.id.tv, item.data!!)
    }

}
