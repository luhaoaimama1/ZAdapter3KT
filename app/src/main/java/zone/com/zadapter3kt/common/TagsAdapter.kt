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
import com.zone.adapter3kt.adapter.StickyAdapter
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapter.LeftDelegates
import zone.com.zadapter3kt.adapter.RightDelegates

/**
 * Copyright (c) 2018 BiliBili Inc.
 * [2018/9/30] by Zone
 */
class TagsAdapter(context: Context, tag: Any? = null) : QuickAdapter<String>(context) {

    init {
        registerDelegate(LeftDelegates())
        registerDelegate(0, LeftDelegates())
        registerDelegate(1, RightDelegates())
        registerDelegate(2, R.layout.header_simple)
        registerDelegate(3, R.layout.footer_simple_img)
        registerEmpytDelegate(R.layout.empty)

        setStyleExtra(object : ViewStyleDefault<String>() {
            override fun generateViewStyleOBJ(item: String): ViewStyleOBJ? {
                var viewStyle = -1
                val tags = ArrayList<String>()
                when (item) {
                    "1" -> {
                        viewStyle = 2
                        tags.add("header")
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
            }

            override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {
            }
        })

        onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(parent: ViewGroup, view: View, position: Int) {
                val filterTagsPosi = this@TagsAdapter.filterTagsPosi(position, "header", "footer", "right")
                Toast.makeText(context, "$filterTagsPosi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}