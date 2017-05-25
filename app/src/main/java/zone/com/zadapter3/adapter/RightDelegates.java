package zone.com.zadapter3.adapter;

import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.helper.Helper;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class RightDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_right;
    }

    @Override
    public void fillData(int postion, String data, Helper helper) {
        helper.setText(R.id.tv, data);
    }

    @Override
    public boolean isFullspan() {
        return true;
    }
}
