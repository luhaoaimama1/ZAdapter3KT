package zone.com.zadapter3.common;

import android.content.Context;
import com.zone.adapter3.QuickRcvAdapter;
import java.util.List;
import zone.com.zadapter3.R;
import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

/**
 * [2017] by Zone
 */

public class CommonRcvAdapter extends QuickRcvAdapter<String> {
    public CommonRcvAdapter(Context context, List<String> data) {
        super(context, data);
        this.addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new RightDelegates())//多部剧 注释开启即可
                .addHeaderHolder(R.layout.header_simple)
                .addFooterHolder(R.layout.footer_simple)
                .addEmptyHold(R.layout.empty);

    }

    @Override
    protected int getItemViewType2(int dataPosition) {
        return dataPosition % 2;
    }
}