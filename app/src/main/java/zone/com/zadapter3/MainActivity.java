package zone.com.zadapter3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import zone.com.zrefreshlayout.Config;
import zone.com.zrefreshlayout.footer.MeterialFooter;
import zone.com.zrefreshlayout.header.MeterialHead;
import zone.com.zrefreshlayout.resistance.DampingHalf;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        ButterKnife.bind(this);
//        QuickConfig.build().perform();
        Config.build()
                .setPinContent(true)
                .setHeader(new MeterialHead())
                .setFooter(new MeterialFooter())
                .setResistance(new DampingHalf())
//                .setHeader(new  CircleRefresh())
//                .setResistance(new Damping2Head8per())
                .writeLog(true)
                .perform();
    }

    @OnClick({R.id.bt_Linear, R.id.bt_Grid, R.id.bt_Staggered})
    public void onClick(View view) {

        Intent intent = new Intent(this, RecyclerActivity.class);
        switch (view.getId()) {
            case R.id.bt_Linear:
                intent.putExtra("type", "Linear");
                break;
            case R.id.bt_Grid:
                intent.putExtra("type", "Grid");
                break;
            case R.id.bt_Staggered:
                intent.putExtra("type", "StaggeredGrid");
                break;
        }
        startActivity(intent);
    }

    @OnClick(R.id.bt_Link)
    public void onClick() {
        startActivity(new Intent(this, ZRefreshActivity.class));
    }

    @OnClick(R.id.bt_HeadFooter)
    public void onClick2() {
        startActivity(new Intent(this, HeadFooterRecyclerActivity.class));
    }

    @OnClick(R.id.bt_diff)
    public void onClick3() {
        startActivity(new Intent(this, DiffActivity.class));
    }

    @OnClick(R.id.bt_layoutManager)
    public void layoutManagerClick() {
        startActivity(new Intent(this, LayoutActivity.class));
    }

    @OnClick(R.id.bt_HexolayoutManager)
    public void nLayoutManagerClick() {
        startActivity(new Intent(this, HexoLayoutActivity.class));
    }

    @OnClick(R.id.bt_Fully)
    public void fullyClick() {
        startActivity(new Intent(this, FullyRecyclerActivity.class));
    }

    @OnClick(R.id.bt_Absorb)
    public void absorbClick() {
        startActivity(new Intent(this, AdsorbActivity.class));
    }
    @OnClick(R.id.bt_AbsorbGrid)
    public void absorbGridClick() {
        startActivity(new Intent(this, AdsorbGridActivity.class));
    }
    @OnClick(R.id.bt_NineGrid)
    public void nineGridClick() {
        startActivity(new Intent(this, NineRecyclerActivity.class));
    }
}
