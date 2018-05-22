package zone.com.zadapter3.adapter;

import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class TestDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void fillData(int postion, String data, Holder holder) {

    }
}
