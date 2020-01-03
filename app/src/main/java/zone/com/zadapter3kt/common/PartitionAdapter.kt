package zone.com.zadapter3kt.common

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.zone.adapter3kt.QuickAdapter
import com.zone.adapter3kt.ViewStyleDefault
import com.zone.adapter3kt.ViewStyleOBJ
import com.zone.adapter3kt.adapter.OnItemClickListener
import com.zone.adapter3kt.delegate.ResDelegate
import com.zone.adapter3kt.holder.BaseHolder
import com.zone.adapter3kt.section.Section
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates

/**
 * [2018/9/13] by Zone
 */
class PartitionAdapter(context: Context) : QuickAdapter<String>(context) {
    companion object {
        val header = "header"
        val footer = "footer"
        val content = "content"
        val divder = "divder"
    }

    init {
        enableHistory(true)

        registerDelegate(LeftDelegates()) //不管
        registerDelegate(1, ResDelegate<String>(R.layout.item_partition_header))
        registerDelegate(2, ResDelegate<String>(R.layout.item_partition_content))
        registerDelegate(3, ResDelegate<String>(R.layout.item_partition_footer))
        registerDelegate(4, ResDelegate<String>(R.layout.item_partition_divder))

        registerEmpytDelegate(R.layout.empty)

        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                println("被点击->onItemClick" + position)
            }
        }
        setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {

                var viewStyle = when (item) {
                    header -> 1
                    content -> 2
                    footer -> 3
                    divder -> 4
                    else -> -1
                }
                if (item.contains("insert one")) {
                    val index = item.substring("insert one item!+".length, item.length).toInt()
                    viewStyle = if (index % 3 == 0) 1 else 0
                }
                return ViewStyleOBJ().viewStyle(viewStyle)
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })
    }

    override fun onViewAttachedToWindowForSection(baseHolder: BaseHolder< androidx.recyclerview.widget.RecyclerView.ViewHolder>, section: Section, sectionPosi: Int) {
        super.onViewAttachedToWindowForSection(baseHolder, section, sectionPosi)
        println("ForSection---onViewAttached----sectionPosi:${sectionPosi}----sectionObj:${section.obj}")
    }

    override fun onViewDetachedFromWindowForSection(baseHolder: BaseHolder< androidx.recyclerview.widget.RecyclerView.ViewHolder>, section: Section, sectionPosi: Int) {
        super.onViewDetachedFromWindowForSection(baseHolder, section, sectionPosi)
        println("ForSection---onViewDetached----sectionPosi:${sectionPosi}----sectionObj:${section.obj}")
    }
}
