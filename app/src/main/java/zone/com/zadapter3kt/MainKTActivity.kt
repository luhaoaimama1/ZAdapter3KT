package zone.com.zadapter3kt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import butterknife.ButterKnife
import butterknife.OnClick
import zone.com.zadapter3.R
import zone.com.zadapter3.StickyActivity
import zone.com.zrefreshlayout.Config
import zone.com.zrefreshlayout.footer.MeterialFooter
import zone.com.zrefreshlayout.header.MeterialHead
import zone.com.zrefreshlayout.resistance.DampingHalf

class MainKTActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        ButterKnife.bind(this)
        //        QuickConfig.build().perform();
        Config.build()
            .setPinContent(true)
            .setHeader(MeterialHead())
            .setFooter(MeterialFooter())
            .setResistance(DampingHalf())
            //                .setHeader(new  CircleRefresh())
            //                .setResistance(new Damping2Head8per())
            .writeLog(true)
            .perform()
    }

    @OnClick(R.id.bt_Linear, R.id.bt_Grid, R.id.bt_Staggered)
    fun onClick(view: View) {
        val intent = Intent(this, RecyclerKTActivity::class.java)
        when (view.id) {
            R.id.bt_Linear -> intent.putExtra("type", "Linear")
            R.id.bt_Grid -> intent.putExtra("type", "Grid")
            R.id.bt_Staggered -> intent.putExtra("type", "StaggeredGrid")
        }
        startActivity(intent)
    }

    @OnClick(R.id.bt_Link)
    fun onClick() = startActivity(Intent(this, ZRefreshKTActivity::class.java))

    @OnClick(R.id.bt_layoutManager)
    fun layoutManagerClick() = startActivity(Intent(this, LayoutKTActivity::class.java))

    @OnClick(R.id.bt_HexolayoutManager)
    fun nLayoutManagerClick()= startActivity(Intent(this, HexoLayoutKTActivity::class.java))

    @OnClick(R.id.bt_Fully)
    fun fullyClick() = startActivity(Intent(this, FullyRecyclerKTActivity::class.java))


    @OnClick(R.id.bt_NineGrid)
    fun nineGridClick() = startActivity(Intent(this, NineRecyclerKTActivity::class.java))


    @OnClick(R.id.bt_Sticky)
    fun stickyClick() = startActivity(Intent(this, StickyKTActivity::class.java))
//    fun stickyClick() = startActivity(Intent(this, StickyActivity::class.java))

    @OnClick(R.id.bt_animator)
    fun animatorClick() = startActivity(Intent(this, RecyclerActivityAnimator::class.java))

    @OnClick(R.id.bt_scroll2)
    fun scrollClick() = startActivity(Intent(this, RecyclerActivityScroll::class.java))

}
