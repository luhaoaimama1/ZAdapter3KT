package zone.com.zadapter3.adapter;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

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
    public void fillData(int postion, String data, Holder holder) {
        holder.setText(R.id.tv, data);
    }

    @Override
    public boolean isFullspan() {
        return true;
    }
}
