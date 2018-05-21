package zone.com.zadapter3.common;

import android.content.Context;

import com.zone.adapter3.QuickRcvAdapter;

import java.util.List;

import zone.com.zadapter3.R;
import zone.com.zadapter3.adapter.ImgDelegates;
import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

/**
 * [2017] by Zone
 * todo  空数据的时候 默认有一个+号  而不用 empty去处理
 */

public class NineGridAdapter extends QuickRcvAdapter<String> {
    public NineGridAdapter(Context context, final List<String> data) {
        super(context, data);
        this.addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new ImgDelegates()); //多部剧 注释开启即可
//                .addHeaderHolder(R.layout.header_simple)
//                .addFooterHolder(R.layout.footer_simple)
//                .addEmptyHold(R.layout.empty);


        setContentDataMapListener(new ContentDataMapListener() {
            @Override
            public <T> int getContentCount(List<T> datas) {
                return datas.size() < 9 ? datas.size() + 1 : datas.size();
            }

//            @Override
//            public <T> T getData(List<T> datas, int position) {
//                if (datas.size() < 9) {
//                    if (datas.size() == 0)
//                        return null;
//                    else {
//                        if (position >=datas.size())
//                            return null;
//                        else
//                            return datas.get(position);
//                    }
//                } else
//                    return datas.get(position);
//            }

            //同上 快捷写法
            @Override
            public <T> T getData(List<T> datas, int position) {
                try {
                    return datas.get(position);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });


    }

    @Override
    protected int getItemViewType2(int dataPosition) {
        return 0;
    }

}