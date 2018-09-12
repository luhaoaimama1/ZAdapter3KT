package com.zone.adapter3kt.rubbish

//package com.zone.adapter3kt
//
///**
// *[2018] by Zone
// *
// * 间隔就不要弄了吧  因为那个和布局关系很大
// */
//interface ViewStyle<T> {
//    fun generateViewStyleOBJ(adapter: Adapter<T>, item: T): ViewStyleOBJ?
//}
//
//class ViewStyleDefault<T> : ViewStyle<T> {
//    override fun generateViewStyleOBJ(adapter: Adapter<T>, item: T): ViewStyleOBJ? {
//        return null
//    }
//}
//
//class ViewStyleOBJ {
//    var viewStyle: Int
//    val isPlaceholder: Boolean
//    val isSticky: Boolean
//
//    private constructor(viewStyle: Int, isPlaceholder: Boolean, isSticky: Boolean) {
//        this.viewStyle = viewStyle
//        this.isPlaceholder = isPlaceholder
//        this.isSticky = isSticky
//    }
//
//    class Builder {
//        var viewStyle = -1
//        var isPlaceholder = false
//        var isSticky = false
//
//        constructor()
//        constructor(viewStyle: Int) {
//            this.viewStyle = viewStyle
//        }
//
//        companion object {
//            @JvmStatic
//            fun viewStyle(viewStyle: Int): Builder = Builder(viewStyle)
//
//            @JvmStatic
//            fun defaultConfig(): ViewStyleOBJ = Builder().build()
//        }
//
//        fun isPlaceholder(isPlaceholder: Boolean): Builder {
//            this@Builder.isPlaceholder = isPlaceholder
//            return this
//        }
//
//        fun isSticky(isSticky: Boolean): Builder {
//            this@Builder.isSticky = isSticky
//            return this
//        }
//
//        fun build(): ViewStyleOBJ = ViewStyleOBJ(viewStyle, isPlaceholder, isSticky)
//    }
//}