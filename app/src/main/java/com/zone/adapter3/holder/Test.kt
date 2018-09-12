package com.zone.adapter3.holder


/**
 * [2018] by Zone
 */

class Test {
    data class Extra(var inner: String?, var inner2: String?)

    var extra = Extra(null, null)
    var inner: String?
        get() = extra.inner
        set(value) {
            extra.inner = value
        }

    var inner2: String? = extra.inner2
}

fun main(args: Array<String>) {
    //有两种方式可用。
    //1. 内部全是 有一个类。类中存储 所有属性。然后可以暴露给外面
    //2. 类中包含  getView这种类似的方法。扩展方式去调用即可


    //应对第一种 ，可以用注解 生成外面的类 。这样就内部就可以简单的使用 外部就引用扩展对象的了
//        println(Holder3().put("abc", "waht"))
//        println(Holder3().put("abc2", "waht2"))
    val obj = Test()
    obj.inner = "abc"
    println(obj.extra.toString())
}
