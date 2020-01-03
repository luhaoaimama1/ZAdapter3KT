package zone.com.zadapter3kt

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import com.zone.adapter3kt.QuickConfig
import com.zone.adapter3kt.loadmore.LoadingSetting
import zone.com.zadapter3.R
import zone.com.zrefreshlayout.Config
import zone.com.zrefreshlayout.ZRefreshLayout
import zone.com.zrefreshlayout.footer.MeterialFooter
import zone.com.zrefreshlayout.header.MeterialHead
import zone.com.zrefreshlayout.resistance.DampingHalf

class MainKTActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        QuickConfig.build().apply {
            loadingSetting = LoadingSetting().apply {
                threshold = 0
                isScrollToLoadData = true
            }
            perform()
        }
        Config.build()
                .setHeadPin(ZRefreshLayout.HeadPin.PIN)
                .setHeader(MeterialHead())
                .setFooter(MeterialFooter())
                .setResistance(DampingHalf())
                //                .setHeader(new  CircleRefresh())
                //                .setResistance(new Damping2Head8per())
                .writeLog(true)
                .perform()
    }

    override fun onClick(view: View?) {
        val intent = Intent(this, RecyclerKTActivity::class.java)
        when (view?.id) {
            R.id.bt_Linear -> startActivity(intent.apply {
                putExtra("type", "Linear")
            })
            R.id.bt_Grid -> startActivity(intent.apply {
                    putExtra("type", "Grid")
                })
            R.id.bt_Staggered -> startActivity(intent.apply {
                putExtra("type", "StaggeredGrid")
            })
            R.id.bt_Link -> startActivity(Intent(this, ZRefreshKTActivity::class.java))
            R.id.bt_layoutManager -> startActivity(Intent(this, LayoutKTActivity::class.java))
            R.id.bt_HexolayoutManager -> startActivity(Intent(this, HexoLayoutKTActivity::class.java))
            R.id.bt_Fully -> startActivity(Intent(this, FullyRecyclerKTActivity::class.java))
            R.id.bt_NineGrid -> startActivity(Intent(this, NineRecyclerKTActivity::class.java))
            R.id.bt_Sticky -> startActivity(Intent(this, StickyKTActivity::class.java))
            R.id.bt_Divder -> startActivity(Intent(this, DivderKTActivity::class.java))
            R.id.bt_animator -> startActivity(Intent(this, RecyclerActivityAnimator::class.java))
            R.id.bt_scroll2 -> startActivity(Intent(this, RecyclerActivityScroll::class.java))
            R.id.bt_partition -> startActivity(Intent(this, PartitionActivity::class.java))
            R.id.bt_changeLayout -> startActivity(Intent(this, ChangeLayoutNestRecyclerActivity::class.java))
            R.id.bt_fast -> startActivity(Intent(this, FastRecyclerActivity::class.java))
            R.id.bt_onclick -> startActivity(Intent(this, OnclickRecyclerActivity::class.java))
            else->{}
        }

    }

}
