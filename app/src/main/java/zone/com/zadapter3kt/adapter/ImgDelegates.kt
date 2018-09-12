package zone.com.zadapter3kt.adapter

import android.view.View

import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.StickyAdapter
import com.zone.adapter3kt.data.DataWarp
import com.zone.adapter3kt.delegate.ViewDelegate

import zone.com.zadapter3.R

/**
 * [2017] by Zone
 */

class ImgDelegates : ViewDelegate<String>() {
    override val layoutId: Int = R.layout.item_img

    override fun onBindViewHolder(position: Int,  item: DataWarp<String>, holder: Holder, payloads: List<*>) {
        if ("".equals(item.data)) {
            holder.setImageResource(R.id.img, R.mipmap.ic_launcher)
            holder.setOnClickListener(null, R.id.img)
        } else {
            holder.setOnClickListener(View.OnClickListener {
                if (adapter is StickyAdapter){
                    val stickyAdapter = adapter as StickyAdapter<String>
                    if (stickyAdapter.itemCount == 9){
                        stickyAdapter.changed(stickyAdapter.itemCount-1,"")
//                        stickyAdapter.remove(stickyAdapter.itemCount-1)
//                        stickyAdapter.add("")
                    }else{
                        stickyAdapter.add(stickyAdapter.itemCount-1, "")
                    }
                }
            }, R.id.img)
        }
    }
}
