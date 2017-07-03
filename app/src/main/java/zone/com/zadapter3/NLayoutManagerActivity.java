package zone.com.zadapter3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import zone.com.zadapter3.layoutmanager.HexoItemView;
import zone.com.zadapter3.layoutmanager.ZLayoutManager;

public class NLayoutManagerActivity extends AppCompatActivity {

    private static final int[] COLORS = {0xffffa9a9, 0xffb7e6ff, 0xffff4081};

    private RecyclerView mRecyclerView;
    private Adapter mAdapter = new Adapter();

    private int mCount = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt).setVisibility(View.GONE);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        init();
    }

    private void init() {
        mRecyclerView.setLayoutManager(new ZLayoutManager());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void add(View view) {
        mCount++;
        mAdapter.notifyDataSetChanged();
    }

    public void change(View view) {
        init();
    }

    class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View item = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_hexo, parent, false);
            return new Holder(item);
        }

        @Override
        public void onBindViewHolder(final Holder holder, final int position) {
            holder.item.setCardColor(COLORS[position % COLORS.length]);
            holder.text.setText("菜单" + position);
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NLayoutManagerActivity.this, holder.text.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mCount;
        }


        class Holder extends RecyclerView.ViewHolder {
            HexoItemView item;
            TextView text;

            public Holder(View itemView) {
                super(itemView);
                item = (HexoItemView) itemView.findViewById(R.id.item);
                text = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
