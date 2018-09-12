package zone.com.zadapter3;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NineRecyclerActivity extends Activity {

    private RecyclerView rv;
    private List<String> mDatas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_rt_recycler);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(this, 3));
        new zone.com.zadapter3.common.NineGridAdapter(this, mDatas)
                .relatedList(rv)
                .addItemDecoration(10);


    }

}
