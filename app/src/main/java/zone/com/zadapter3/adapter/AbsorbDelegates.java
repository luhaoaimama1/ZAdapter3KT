package zone.com.zadapter3.adapter;

import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.helper.Helper;

import zone.com.zadapter3.R;
import zone.com.zadapter3.helper.ExtraHelper;

/**
 * [2017] by Zone
 */

public class AbsorbDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_absorb;
    }

    @Override
    public void fillData(int postion, String data, Helper helper) {
    }


}
