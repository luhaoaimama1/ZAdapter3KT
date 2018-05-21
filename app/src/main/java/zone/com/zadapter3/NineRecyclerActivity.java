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

import java.util.ArrayList;
import java.util.List;

import zone.com.zadapter3.adapter.LeftDelegates;
import zone.com.zadapter3.adapter.RightDelegates;
import zone.com.zadapter3.common.NineGridAdapter;

public class NineRecyclerActivity extends Activity {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_rt_recycler);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        new NineGridAdapter(this, mDatas)
                .relatedList(rv)
                .addItemDecoration(10);
    }

}
