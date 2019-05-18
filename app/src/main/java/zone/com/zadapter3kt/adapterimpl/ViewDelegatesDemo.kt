package zone.com.zadapter3kt.adapterimpl

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.zone.adapter3kt.delegate.ViewDelegate
import com.zone.adapter3kt.holder.BaseHolder

/**
 *[2019/3/5] by Zone
 */
abstract class ViewDelegatesDemo<T> : ViewDelegate<T, HolderExDemoImpl>() {
    companion object {
        private val INNER_HOLDER_TAG = "INNer_holder_tag"
    }

    override fun createHolder(view: View): HolderExDemoImpl {
        return HolderExDemoImpl(view)
    }

    override fun realBindViewHolder(position: Int, item: Any, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        super.realBindViewHolder(position, item, baseHolder, payloads)
    }

    override fun castDealHolder(baseHolder: BaseHolder<androidx.recyclerview.widget.RecyclerView.ViewHolder>): HolderExDemoImpl {
        return if (baseHolder is HolderExDemoImpl) baseHolder
        else {
            //为什么这么写 因为 bind是多次重复调用防止多次创建对象
            val holderExDemoImpl = baseHolder.tagMaps.get(INNER_HOLDER_TAG)
            if (holderExDemoImpl is HolderExDemoImpl) {
                holderExDemoImpl
            } else {
                val holderExDemoImplReal = HolderExDemoImpl(baseHolder)
                baseHolder.tagMaps.set(INNER_HOLDER_TAG, holderExDemoImplReal)
                holderExDemoImplReal
            }
        }
    }
}