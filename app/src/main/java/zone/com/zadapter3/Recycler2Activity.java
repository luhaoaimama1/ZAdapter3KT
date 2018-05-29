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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zone.adapter3.QuickRcvAdapter;
import com.zone.adapter3.base.IAdapter;

import java.util.ArrayList;
import java.util.List;

import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;

public class Recycler2Activity extends Activity implements Handler.Callback, View.OnClickListener {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();
    private RecyclerView.Adapter<RecyclerView.ViewHolder> muliAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_recycler);
        rv = (RecyclerView) findViewById(R.id.rv);

        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setItemAnimator(new DefaultItemAnimator());
        muliAdapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>(){

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(Recycler2Activity.this).inflate(R.layout.item_left, rv, false)){};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView tv = (TextView) holder.itemView.findViewById(R.id.tv);
                tv.setText(mDatas.get(position));
            }

            @Override
            public int getItemCount() {
                return mDatas.size();
            }
        };

        rv.setAdapter(muliAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                String str = "insert one no ani";
                mDatas.add(str);
                muliAdapter.notifyItemInserted(mDatas.indexOf(str));
//                muliAdapter.notifyDataSetChanged();
//                muliAdapter.notifyItemInserted();
                break;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
