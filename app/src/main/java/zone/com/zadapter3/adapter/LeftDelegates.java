package zone.com.zadapter3.adapter;

import android.view.View;
import android.widget.TextView;

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
    public void fillData(int postion, String data, Helper<Helper> helper) {
        helper.setText(R.id.tv, data);
        getItemView().post(new Runnable() {
            @Override
            public void run() {
                QuickConfig.e("height" + getItemView().getHeight());
            }
        });
        //需要泛型补全 Helper<Helper> 不然里面的泛型会出问题！ 既这行出错
        TextView tv3 = helper.getView(R.id.tv);
        helper.setText(R.id.tv, data)
                .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            System.out.println("helper click测试 ");
                                        }
                                    });
//
//                        ExtraHelper.wrapper(helper).setText(R.id.tv, data).heihei().heihei2()
//                                .setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        System.out.println("helper click测试 ");
//                                    }
//                                }, new int[]{R.id.tv, R.id.ll_main});
    }


}
