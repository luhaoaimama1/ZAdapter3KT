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
import android.widget.Toast;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;
import java.util.ArrayList;
import java.util.List;
import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

public class RecyclerActivity extends Activity implements Handler.Callback, View.OnClickListener {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();
    private IAdapter<String> muliAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_recycler);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
//        for (int i = 1; i <= 100; i++) {
//            mDatas.add("" +  i);
//        }

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
        muliAdapter = new QuickRcvAdapter(this, mDatas){
            @Override
            protected int getItemViewType2(int dataPosition) {
                return dataPosition % 3==0?1:0;
            }
        };
        muliAdapter
                .addViewHolder(new LeftDelegates())//默认
                .addViewHolder(0, new LeftDelegates()) //多部剧 注释开启即可
                .addViewHolder(1, new RightDelegates())//多部剧 注释开启即可
//                .addHeaderHolder(R.layout.header_simple)
//                .addFooterHolder(R.layout.footer_simple)
                .addEmptyHold(R.layout.empty)
                .relatedList(rv)
                .addItemDecoration(10)
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
        muliAdapter.notifyDataSetChanged();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                String str="insert one no ani";
                mDatas.add(str);
//                muliAdapter.notifyDataSetChanged();
                //todo 如果有empty的话 就会有问题 因为默认是有一个东西的；
                muliAdapter.notifyItemInsertedEmpty(mDatas.indexOf(str));
//                muliAdapter.notifyItemInserted();
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
                int mode= (int) (Math.random()*100%3);
                switch (mode) {
                    case 0:
                        muliAdapter.addHeaderHolder(R.layout.header_simple);
                        break;
                    case 1:
                        muliAdapter.addHeaderHolder(R.layout.header_simple2);
                        break;
                    case 2:
                        muliAdapter.addHeaderHolder(R.layout.header_simple3);
                        break;
                }
                muliAdapter.notifyDataSetChanged();
                break;
            case R.id.bt_addFooter:
                int mode2= (int) (Math.random()*100%3);
                switch (mode2) {
                    case 0:
                        muliAdapter.addFooterHolder(R.layout.footer_simple);
                        break;
                    case 1:
                        muliAdapter.addFooterHolder(R.layout.footer_simple_img2);
                        break;
                    case 2:
                        muliAdapter.addFooterHolder(R.layout.footer_simple_img);
                        break;
                }
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
            case R.id.bt_addHeaderFirst:
                //todo   addHeaderHolder(0,id,true) 的话就有问题  如果没有数据就没有问题
//                muliAdapter.addHeaderHolder(0,R.layout.header_simple3);
                muliAdapter.addHeaderHolder(0,R.layout.header_simple3,true);
                muliAdapter.scrollToPosition(0);
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
