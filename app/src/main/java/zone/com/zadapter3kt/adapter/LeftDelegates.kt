package zone.com.zadapter3kt.adapter

import android.view.View

import com.zone.adapter3.QuickConfig
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate
import com.zone.adapter3kt.holder.Holder
import zone.com.zadapter3.R

/**
 * [2017] by Zone
 */

class LeftDelegates : ViewDelegate<String>() {
    override val layoutId: Int= R.layout.item_left

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, holder: Holder, payloads: List<*>) {
        holder.setText(R.id.tv, item.data!!)
        holder.itemView.post { QuickConfig.e("height" + holder.itemView.height) }
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        holder.setText(R.id.tv, item.data!!)
                .setOnClickListener(View.OnClickListener { println("holder click测试 ") })

        //        ExtraHelperV2.wrapperV2(holder)
        //                .setText(R.id.tv, data)
        //                .heihei()
        //                .heihei2()
        //                .gaga()
        //                .gaga2()
        //        ;
    }

}
