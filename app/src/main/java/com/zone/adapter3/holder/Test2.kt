package com.zone.adapter3.holder

import kotlin.reflect.KProperty


/**
 * [2018] by Zone
 *
 *  为了扩展函数 开放Extension 而设计
 *
 *  外部包用extraProperty 去使用。
 *  内部项目则用 内部属性即可
 */
class Test2 {
    val delete = ExtensionDelegate()
    internal var inner: String? by delete
    internal var value: Int? by delete

    val extraProperty = ExtensionProperty()

    inner class ExtensionProperty {
        var inner: String? by delete
        var value: Int? by delete
    }

    class ExtensionDelegate {
        val map = HashMap<Any, Any?>()
        operator fun <T> getValue(thisRef: Any?, property: KProperty<*>): T? {
            return if (map.get(property.name) == null) null else map.get(property.name) as T
        }

        operator fun <T> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
            map.put(property.name, value)
        }
    }
}

fun main(args: Array<String>) {
    //有两种方式可用。
    //1. 内部全是 有一个类。类中存储 所有属性。然后可以暴露给外面
    //2. 类中包含  getView这种类似的方法。扩展方式去调用即可


    //应对第一种 ，可以用注解 生成外面的类 。这样就内部就可以简单的使用 外部就引用扩展对象的了
//        println(Holder3().put("abc", "waht"))
//        println(Holder3().put("abc2", "waht2"))
    val obj = Test2()
    obj.inner = "abc"
    obj.value = 12
    println(obj.delete.map)

    obj.extraProperty.inner = null
    obj.extraProperty.value = 14
    println(obj.delete.map)
}
