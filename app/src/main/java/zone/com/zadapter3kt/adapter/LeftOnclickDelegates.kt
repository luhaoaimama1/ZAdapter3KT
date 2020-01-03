package zone.com.zadapter3kt.adapter

import android.util.Log
import android.view.View
import com.zone.adapter3kt.QuickConfig

import com.zone.adapter3kt.data.DataWarp
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
        baseHolder.setText(R.id.tv, item.data!!)
        baseHolder.itemView.post { QuickConfig.e("height" + baseHolder.itemView.height) }
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        baseHolder.setText(R.id.tv, item.data!!)
            .setOnClickListener(View.OnClickListener { println("holder click测试 ") })
    }

    override fun onClick(v: View?, viewBaseHolder: HolderExDemoImpl, posi: Int, item: DataWarp<String>) {
        super.onClick(v, viewBaseHolder, posi, item)
        Log.e("onClick","click 位置：$posi, 点击 ${if(v!!.id==R.id.ll_main) "非文字" else "文字"}")
    }

}