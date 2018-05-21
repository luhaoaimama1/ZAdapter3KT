package zone.com.zadapter3.adapter;

import android.view.View;

import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.helper.Helper;

import zone.com.zadapter3.R;

/**
 * [2017] by Zone
 */

public class ImgDelegates extends ViewDelegates<String> {


    @Override
    public int getLayoutId() {
        return R.layout.item_img;
    }

    @Override
    public void fillData(int postion, String data, final Helper helper) {
        if(data!=null){
            helper.setImageResource(R.id.img,R.mipmap.ic_launcher);
            helper.setOnClickListener(null,R.id.img);
        }else{
            helper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.getAdapter().getData().add("");
                    helper.getAdapter().notifyDataSetChanged();
                }
            },R.id.img);
        }
    }
}
