package zone.com.zadapter3.adapter;

import android.graphics.Rect;
import android.view.View;

import com.zone.adapter3.QuickConfig;
import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

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
    public void fillData(int postion, String data, Holder helper) {
    }
    public boolean isFullspan() {
        return true;
    }

    @Override
    public Rect dectorRect() {
        return new Rect(0,ORG_DECTOR,0,ORG_DECTOR);
//        return null;
    }
}
