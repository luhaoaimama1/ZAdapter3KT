package zone.com.zadapter3kt.adapter

import android.view.View
import com.zone.adapter3kt.QuickConfig

import com.zone.adapter3kt.data.DataWarp
import com.zone.lib.utils.activity_fragment_ui.ToastUtils
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class LeftOnclickDelegates : ViewDelegatesDemo<String>() {
    override val layoutId: Int = R.layout.item_left_onclick

    override fun registerClickListener(): Array<Int>? = arrayOf(R.id.ll_main, R.id.tv)

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        item.data?.let {
            baseHolder.itemView.post { QuickConfig.e("height" + baseHolder.itemView.height) }
            baseHolder.setText(R.id.tv, it)
        }
    }

    override fun onClick(v: View?, viewBaseHolder: HolderExDemoImpl, posi: Int, item: DataWarp<String>) {
        super.onClick(v, viewBaseHolder, posi, item)
        ToastUtils.showShort(viewBaseHolder.itemView.context, "click 位置：$posi, 点击 ${if (v!!.id == R.id.ll_main) "非文字" else "文字"}")
    }

}
