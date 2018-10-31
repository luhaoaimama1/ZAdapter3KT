package zone.com.zadapter3kt.adapter

import android.view.View
import android.view.ViewGroup
import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate
import zone.com.zadapter3.R
import zone.com.zadapter3kt.helper.ExpandHolder

/**
 * [2017] by Zone
 */

class TestExpandHolderDelegates : ViewDelegate<String>() {

    override val layoutId: Int = 0

    override fun registerClickListener(): Array<Int> = arrayOf(R.id.image, R.id.beginning)

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: Holder, payloads: List<*>) {
        ExpandHolder(holder).apply {
            go()
            go2()
        }
    }

    override fun onClick(v: View?, viewHolder: Holder, posi: Int, item: DataWarp<String>) {
        super.onClick(v, viewHolder, posi, item)
    }
}
