package zone.com.zadapter3.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

import zone.com.zadapter3.R;
import zone.com.zadapter3.helper.ExtraHelper;
import zone.com.zadapter3.helper.ExtraHelperV2;

/**
 * [2017] by Zone
 */

public class LeftDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_left;
    }

    @Override
    public void fillData(int postion, String data, final Holder holder) {
        holder.setText(R.id.tv, data);
        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                QuickConfig.e("height" + holder.itemView.getHeight());
            }
        });
        //需要泛型补全 holder<holder> 不然里面的泛型会出问题！ 既这行出错

        holder.setText(R.id.tv, data)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("holder click测试 ");
                    }
                });

        ExtraHelperV2.wrapperV2(holder)
                .setText(R.id.tv, "a")
                .heihei()
                .heihei2()
                .gaga()
                .gaga2()
        ;
    }
}
