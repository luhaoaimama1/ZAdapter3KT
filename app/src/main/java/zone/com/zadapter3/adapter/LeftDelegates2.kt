package zone.com.zadapter3.adapter

import android.view.View

import com.zone.adapter3.QuickConfig
import com.zone.adapter3.bean.Holder
import com.zone.adapter3.bean.ViewDelegates

import zone.com.zadapter3.R
import zone.com.zadapter3.helper.ExtraHelperV2

/**
 * [2017] by Zone
 */

class LeftDelegates2 : ViewDelegates<String>() {


    override fun getLayoutId(): Int {
        return R.layout.item_left
    }

    override fun fillData(postion: Int, data: String, holder: Holder<*>) {
        holder.setText(R.id.tv, data)
        holder.itemView.post { QuickConfig.e("height" + holder.itemView.height) }
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        holder.setText(R.id.tv, data)
                .setOnClickListener(View.OnClickListener { println("holder click测试 ") })

        aa(holder, data)
    }

    @SuppressWarnings("unchecked")
    private fun aa(holder: Holder<*>, data: String) {
        ExtraHelperV2.wrapperV2(holder)
                .setText(R.id.tv, data)
                .heihei()
                .heihei2()
                .gaga()
                .gaga2()
    }
}
