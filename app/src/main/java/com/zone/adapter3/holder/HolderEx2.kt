package 泛型.继承链式测试.kotlin

/**
 * Created by fuzhipeng on 2018/8/21.
 */

fun Holder3.put2(key: String, value: String): Holder3 {
    this.mExtension.map.put(key, value)
    return this
}

fun Holder3.adds2(): Holder3 {
    for (i in 1..10) {
        this.mExtension.map.put("$i", "value:$i")
    }
    return this
}

