package com.zone.adapter3kt.sticky

import android.view.View
import com.zone.adapter3kt.holder.Holder

/**
 * [2018/9/13] by Zone
 */
open class StickyHolder(view: View, var stickyChildHolderItemView: View, var stickyChildHolder:StickyChildHolder) : Holder(view)
