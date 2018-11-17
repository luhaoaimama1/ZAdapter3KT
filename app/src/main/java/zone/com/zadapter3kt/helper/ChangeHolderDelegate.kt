package zone.com.zadapter3kt.helper

import com.zone.adapter3kt.holder.Holder
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

/**
 *[2018/9/14] by Zone
 */
abstract class ChangeHolderDelegate<T> : ViewDelegate<T>(){
    override fun onBindViewHolder(position: Int, item: DataWarp<T>, holder: Holder, payloads: List<*>) {

    }
}