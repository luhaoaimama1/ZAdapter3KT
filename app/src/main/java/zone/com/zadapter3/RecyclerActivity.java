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
import android.view.ViewGroup;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;
import com.zone.adapter3.loadmore.OnScrollRcvListener;

import java.util.ArrayList;
import java.util.List;

import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;
import zone.com.zadapter3.helper.OnScrollRcvListenerEx;

public class RecyclerActivity extends Activity implements Handler.Callback, View.OnClickListener {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();
    private IAdapter<String> muliAdapter;
    private Handler handler;
    private OnScrollRcvListenerEx scroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
        setContentView(R.layout.a_recycler);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        for (int i = 'A'; i <= 'Z'; i++) {
            mDatas.add("" + (char) i);
        }

        //base test
        String type = getIntent().getStringExtra("type");
        if ("Linear".equals(type)) {
            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
        if ("Grid".equals(type)) {
            rv.setLayoutManager(new GridLayoutManager(this, 3));
        }
        if ("StaggeredGrid".equals(type)) {
            rv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        muliAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                return dataPosition % 2;
            }
        };
        muliAdapter
                .addViewHolder(new LeftDelegates())
                .addViewHolder(0, new LeftDelegates())
                .addViewHolder(1, new RightDelegates())
//                .addHeaderHolder(R.layout.header_simple)
//                .addFooterHolder(R.layout.footer_simple)
                .addEmptyHold(R.layout.empty)
                .relatedList(rv)
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
                })
                .addOnScrollListener(scroller=new OnScrollRcvListenerEx(new OnScrollRcvListenerEx.LoadMoreCallback() {
                    @Override
                    public void loadMore() {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                switch (loadMoreCount) {
                                    case 1:
                                        scroller.loadMoreFail();
                                        break;
                                    case 2:
                                        scroller.loadMoreComplete();
                                        break;
                                    default:
                                        scroller.end();
                                        break;
                                }
                                loadMoreCount++;
                            }
                        }, 2000);
                    }
                }))
        ;
//        RecyclerViewEmptyUtils.addEmptyView(rv,View.inflate(this, R.layout.empty, null));
    }

    public int loadMoreCount = 1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                mDatas.add("insert one no ani");
                muliAdapter.notifyDataSetChanged();
//                muliAdapter.add(1, "insert one no ani");
                break;
            case R.id.bt_aniAdd:
                mDatas.add(1, "insert one with ani");
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_remove:
                if (mDatas.size() > 1) {
                    mDatas.remove(1);
                    muliAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.bt_removeAni:
                if (mDatas.size() > 1) {
                    mDatas.remove(1);
                    muliAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.bt_clear:
                mDatas.clear();
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_addHeader:
                muliAdapter.addHeaderHolder(R.layout.header_simple);
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_addFooter:
                muliAdapter.addFooterHolder(R.layout.footer_simple);
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_clearFooter:
                muliAdapter.clearFooterHolder();
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_clearHeader:
                muliAdapter.clearHeaderHolder();
                muliAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
