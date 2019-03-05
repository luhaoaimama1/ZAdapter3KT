package com.zone.adapter3kt.holder

import android.view.View

/**
 * [2019/3/5] by Zone
 */
open class Holder:BaseHolder<Holder>{
    constructor(view: View) : super(view)
    constructor(baseHolder: BaseHolder<*>) : super(baseHolder)
    override fun getReturnValue(): Holder =this
}
