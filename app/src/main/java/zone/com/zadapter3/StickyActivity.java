package zone.com.zadapter3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;

import java.util.ArrayList;
import java.util.List;

import zone.com.zadapter3.adapter.AbsorbDelegates;
import zone.com.zadapter3.adapter.AbsorbDelegates2;
import zone.com.zadapter3.adapter.AbsorbDelegates3;
import zone.com.zadapter3.adapter.LeftDelegates;

/**
 * [2017] by Zone
 */

public class StickyActivity extends Activity {

    private List<String> mDatas = new ArrayList<String>();
    private RecyclerView rv;
    private IAdapter<String> muliAdapter;
    private FrameLayout vp;
    private CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_recycler_absorb);
        rv = (RecyclerView) findViewById(R.id.rv);
        vp = (FrameLayout) findViewById(R.id.vp);
        rv.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 1; i <= 100; i++) {
            mDatas.add("" + i);
        }


        muliAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                if (dataPosition == 3)
                    return 1;
                else if (dataPosition == 6)
                    return 2;
                else if (dataPosition == 9)
                    return 3;
                else
                    return 0;
            }
        };
        muliAdapter
                .addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new AbsorbDelegates())//多部剧 注释开启即可
                .addViewHolder(2, new AbsorbDelegates2())//多部剧 注释开启即可
                .addViewHolder(3, new AbsorbDelegates3())//多部剧 注释开启即可
//                .addHeaderHolder(R.layout.header_simple)
//                .addFooterHolder(R.layout.footer_simple)
                .addEmptyHold(R.layout.empty)
                .relatedList(rv)
//                .addSticky(vp, 3, 6, 9)
                .addItemDecoration(70)
                .setOnItemClickListener(new IAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(ViewGroup parent, View view, int position) {
                        System.out.println("被点击->onItemClick" + position);
                    }
                })
                .setOnItemLongClickListener(new IAdapter.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(ViewGroup parent, View view, int position) {
                        System.out.println("被点击->onItemLongClick:" + position);
                        return true;
                    }
                });


        cb = (CheckBox) findViewById(R.id.cb);
        if ("debug".equals(getIntent().getStringExtra("type"))) {
            muliAdapter.addSticky(Color.BLUE, vp, 3, 6, 9);
            vp.getLayoutParams().width = 700;
            cb.performClick();
        } else {
            muliAdapter.addSticky(vp, 3, 6, 9);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(StickyActivity.this, StickyActivity.class);
                if (isChecked)
                    intent.putExtra("type", "debug");
                startActivity(intent);
                finish();
            }
        });
    }
}
