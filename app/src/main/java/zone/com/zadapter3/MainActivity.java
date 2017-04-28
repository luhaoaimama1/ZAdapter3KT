package zone.com.zadapter3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        ButterKnife.bind(this);
//        QuickConfig.build().perform();
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
}
