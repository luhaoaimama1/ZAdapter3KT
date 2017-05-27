package zone.com.zadapter3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.diff.DiffCallBack;

import java.util.ArrayList;
import java.util.List;

import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

public class RecyclerDiffActivity extends Activity implements Handler.Callback, View.OnClickListener {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();
    private IAdapter<String> muliAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_recycler_diff);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        for (int i = 1; i <= 100; i++) {
            mDatas.add("" + i);
        }

//        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setLayoutManager(new GridLayoutManager(this, 3));
//        rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        muliAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                return dataPosition % 3 == 0 ? 1 : 0;
            }
        };
        muliAdapter
                .addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new RightDelegates())//多部剧 注释开启即可
                .addEmptyHold(R.layout.empty)
                .relatedList(rv)
                .addItemDecoration(10);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_diff:
                muliAdapter.diffSetKeyframe();
                mDatas.add(7, "insert diff");
                for (int i = 0; i < 3; i++) {
                    mDatas.remove(1 + i);
                }
                muliAdapter.diffCalculate(new DiffCallBack<String>() {
                    @Override
                    public boolean areContentsTheSame(String oldItem, String newItem) {
                        return oldItem.equals(newItem);
                    }
                });
                muliAdapter.diffNotifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
