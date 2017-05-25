package zone.com.zadapter3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.bean.ViewDelegates;
import com.zone.adapter3.helper.Helper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

public class MuliRecyclerActivity extends Activity implements Handler.Callback {

    @Bind(R.id.rv)
    RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();
    private IAdapter<String> muliAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_muli_recycler);
        ButterKnife.bind(this);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        for (int i = 1; i <= 10; i++) {
            mDatas.add("" + i);
        }
        rv.setLayoutManager(new GridLayoutManager(this, 3));

        rv.setItemAnimator(new DefaultItemAnimator());

        muliAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                return dataPosition % 2;
            }
        };

        muliAdapter
                .addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new RightDelegates())//多部剧 注释开启即可
                .addHeaderHolder(R.layout.header_simple)
                .addHeaderHolder(R.layout.header_simple2)
                .addHeaderHolder(R.layout.header_simple2)
                .addFooterHolder(footer1 = new ViewDelegates() {
                    @Override
                    public int getLayoutId() {
                        return R.layout.footer_simple;
                    }

                    @Override
                    public void fillData(int postion, Object data, Helper helper) {

                    }
                })
                .addEmptyHold(R.layout.empty)
                .relatedList(rv);
    }

    ViewDelegates footer1, footer2;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @OnClick(R.id.bt_change)
    public void onClick() {
        muliAdapter.removeFooterHolder(footer1);
        footer1.setTag("footer1");
        muliAdapter.removeFooterHolder(footer2);
        muliAdapter.addFooterHolder(footer2 = new ViewDelegates() {
            @Override
            public int getLayoutId() {
                return R.layout.footer_simple2;
            }

            @Override
            public void fillData(int postion, Object data, Helper helper) {

            }
        })
       .addFooterHolder(R.layout.header_simple);
        footer2.setTag("footer2");

        muliAdapter.notifyDataSetChanged();

    }
}
