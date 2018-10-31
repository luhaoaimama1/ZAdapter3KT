package zone.com.zadapter3kt.helper

import com.zone.adapter3kt.holder.Holder

/**
 * Copyright (c) 2018 BiliBili Inc.
 * [2018/9/14] by Zone
 */
class ExpandHolder(val holder: Holder) : Holder(holder.view) {
    fun go(): Holder {
        return holder
    }

    fun go2(): Holder {
        return holder
    }
}
