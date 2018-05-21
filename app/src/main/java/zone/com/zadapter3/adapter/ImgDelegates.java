package zone.com.zadapter3.adapter;

import android.view.View;

import com.zone.adapter3.bean.Holder;
import com.zone.adapter3.bean.ViewDelegates;

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
    public void fillData(int postion, String data, final Holder holder) {
        if(data!=null){
            holder.setImageResource(R.id.img,R.mipmap.ic_launcher);
            holder.setOnClickListener(null,R.id.img);
        }else{
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.getData().add("");
                    adapter.notifyDataSetChanged();
                }
            },R.id.img);
        }
    }
}
