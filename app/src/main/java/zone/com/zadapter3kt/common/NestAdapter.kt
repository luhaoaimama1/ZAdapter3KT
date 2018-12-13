package zone.com.zadapter3kt.common

import android.support.v7.widget.GridLayoutManager
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.data.HFMode
import zone.com.zadapter3.R
import zone.com.zadapter3kt.ChangeLayoutNestRecyclerActivity
import zone.com.zadapter3kt.adapter.SquareDelegates
import zone.com.zadapter3kt.adapter.SquareHeaderDelegates

/**
 * [2018/9/13] by Zone
 */
class NestAdapter(activity: ChangeLayoutNestRecyclerActivity) : QuickAdapter<Int>(activity) {
    init {
        enableHistory(true)
        registerDelegate(SquareDelegates())
        registerDelegate(1, SquareDelegates())
        registerDelegate(2, SquareHeaderDelegates(activity))
        defineHeaderOrder(HFMode.ADD, 2)
        registerEmpytDelegate(R.layout.empty)
        setStyleExtra(object : ViewStyleDefault<Int>() {
            override fun generateViewStyleOBJ(item: Int): ViewStyleOBJ? {
                return ViewStyleOBJ().viewStyle(if (item == Int.MAX_VALUE) 2 else 1)
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })


    }
}
