package zone.com.zadapter3.adapter;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class AbsorbDelegates3 extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_absorb3;
    }

    @Override
    public void fillData(int postion, String data, Holder helper) {
    }

    public boolean isFullspan() {
        return true;
    }
}
