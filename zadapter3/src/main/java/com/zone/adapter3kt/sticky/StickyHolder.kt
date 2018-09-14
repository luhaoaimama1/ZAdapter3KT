package com.zone.adapter3kt.sticky

import android.view.View
import com.zone.adapter3kt.Holder
import com.zone.adapter3kt.delegate.ViewDelegate

/**
 * Copyright (c) 2018 BiliBili Inc.
 * [2018/9/13] by Zone
 */
open class StickyHolder(view: View, val delegate: ViewDelegate<*>, var delegateView: View,var stickyChildHolder:StickyChildHolder) : Holder(view)