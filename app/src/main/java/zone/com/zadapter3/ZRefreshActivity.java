package zone.com.zadapter3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;
import zone.com.zadapter3.helper.OnScrollRcvListenerExZRefresh;
import zone.com.zrefreshlayout.ZRefreshLayout;

public class ZRefreshActivity extends Activity implements Handler.Callback {

    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.refresh)
    ZRefreshLayout refresh;
    private List<String> mDatas = new ArrayList<String>();
    private IAdapter<String> mAdapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this);
        setContentView(R.layout.a_recycler_zrefresh);
        ButterKnife.bind(this);

        rv.setLayoutManager(new GridLayoutManager(this, 3));
        for (int i = 'A'; i <= 'z'; i++) {
            mDatas.add("" + (char) i);
        }
        //base test
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        setAdapter();
        initRefresh();
    }

    private void setAdapter() {
        mAdapter = new QuickRcvAdapter(this, mDatas) {
            @Override
            protected int getItemViewType2(int dataPosition) {
                return dataPosition % 2;
            }
        };
        mAdapter
                .addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new RightDelegates())//多部剧 注释开启即可
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
                .addOnScrollListener(new OnScrollRcvListenerExZRefresh(refresh))
        ;
    }

    private void initRefresh() {
        //更改第一个参数,可以看到 是否委托给外面的监听
        refresh.setLoadMoreListener(true, new ZRefreshLayout.LoadMoreListener() {
            @Override
            public void loadMore(ZRefreshLayout zRefreshLayout) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (loadMoreCount) {
                            case 1:
                                mAdapter.loadMoreFail();
                                mDatas.add("loadMore Fail!");
                                mAdapter.notifyItemInsertedEx(mDatas.size() - 1);
                                mAdapter.scrollToLast();
                                break;
                            case 2:
                                mAdapter.loadMoreComplete();
                                mDatas.add("loadMore Complete!");
                                mAdapter.notifyItemInsertedEx(mDatas.size() - 1);
                                mAdapter.scrollToLast();
                                break;
                            default:
                                mAdapter.end();
                                mDatas.add("loadMore end!");
                                mAdapter.notifyItemInsertedEx(mDatas.size() - 1);
                                mAdapter.scrollToLast();
                                refresh.setCanLoadMore(false);
                                break;
                        }
                        loadMoreCount++;
                    }
                }, 2000);
            }

            @Override
            public void loadMoreAnimationComplete(ZRefreshLayout zRefreshLayout) {

            }
        });
        refresh.setPullListener(new ZRefreshLayout.PullListener() {
            @Override
            public void refresh(ZRefreshLayout zRefreshLayout) {
                refresh.setCanLoadMore(true);
                loadMoreCount = 1;
//                mAdapter.removeLoadMoreDelegates();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        mDatas.clear();
                        mDatas.add( "Refresh Complete!");
                        mAdapter.notifyDataSetChanged();
//                        mAdapter.end();
                        refresh.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void refreshAnimationComplete(ZRefreshLayout zRefreshLayout) {

            }
        });
    }

    public int loadMoreCount = 1;

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
