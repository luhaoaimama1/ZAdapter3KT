package zone.com.zadapter3kt.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.adapter.OnItemClickListener
import com.zone.adapter3kt.divder.StandardDivder
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates

/**
 * [2018/9/30] by Zone
 */
class DivdersAdapter(context: Context) : QuickAdapter<String>(context) {

    init {

        divderManager = StandardDivder(this)

        registerDelegate(LeftDelegates())
        registerDelegate(0, LeftDelegates())
        registerDelegate(1, RightDelegates())
        registerDelegate(2, R.layout.header_simple)
        registerDelegate(3, R.layout.footer_simple_img)
        registerDelegate(4, R.layout.item_divder_control)
        registerEmpytDelegate(R.layout.empty)

        setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                var bottom = 10
                var viewStyle = -1
                var hideBeforeDivderBottom=false
                val tags = ArrayList<String>()
                when (item) {
                    "1" -> {
                        viewStyle = 2
                        tags.add("header")
                    }
                    "5" -> {
                        viewStyle = 4
                        bottom=0
                        hideBeforeDivderBottom=true
                    }
                    "10" -> {
                        viewStyle = 3
                        tags.add("footer")
                    }
                    else -> {
                        viewStyle = item.toInt() % 2
                        tags.add(if (viewStyle == 1) "right" else "left")
                    }
                }

                return ViewStyleOBJ().viewStyle(viewStyle).addTags(tags)
                    .hideBeforeDivderBottom(hideBeforeDivderBottom)
                    .divderRect(Rect(10,0,10, bottom))
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })

        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                val filterTagsPosi = this@DivdersAdapter.filterTagsPosi(position, "header", "footer", "right")
                Toast.makeText(context, "$filterTagsPosi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}