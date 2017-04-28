package zone.com.zadapter3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zone.adapter3.QuickConfig;

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

    @OnClick(R.id.bt_rv)
    public void onClick() {
        startActivity(new Intent(this, RecyclerActivity.class));
    }
}
