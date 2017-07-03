package zone.com.zadapter3.adapter;

import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.ViewDelegates;

import zone.com.zadapter3.helper.ExtraHelper;

import com.zone.adapter3.helper.Helper;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class LeftDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_left;
    }

    @Override
    public void fillData(int postion, String data, Helper helper) {
//        helper.setText(R.id.tv, data);
        getItemView().post(new Runnable() {
            @Override
            public void run() {
                QuickConfig.e("height"+getItemView().getHeight());
            }
        });
        ExtraHelper.wrapper(helper).setText(R.id.tv, data).heihei().heihei2()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("helper click测试 ");
                    }
                }, new int[]{R.id.tv, R.id.ll_main});
        ;
    }


}
