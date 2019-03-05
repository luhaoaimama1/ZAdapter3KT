package zone.com.zadapter3kt.adapter

import android.view.View
import com.zone.adapter3kt.adapter.StickyAdapter
import com.zone.adapter3kt.data.DataWarp
import zone.com.zadapter3.R
import zone.com.zadapter3kt.adapterimpl.HolderExDemoImpl
import zone.com.zadapter3kt.adapterimpl.ViewDelegatesDemo

/**
 * [2017] by Zone
 */

class ImgDelegates : ViewDelegatesDemo<String>() {
    override val layoutId: Int = R.layout.item_img

    override fun onBindViewHolder(position: Int, item: DataWarp<String>, baseHolder: HolderExDemoImpl, payloads: List<*>) {
        if ("".equals(item.data)) {
            baseHolder.setImageResource(R.id.img, R.mipmap.ic_launcher)
            baseHolder.setOnClickListener(null, R.id.img)
        } else {
            baseHolder.setOnClickListener(View.OnClickListener {
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
