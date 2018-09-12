package 泛型.继承链式测试.kotlin

import java.util.*

/**
 * Created by fuzhipeng on 2018/8/21.
 */


open class Holder3 {
    companion object {//类的伴生对象
        fun hha(){}
    }
    class Extension {
        var map = HashMap<String, String>()
    }

    var mExtension = Extension()

    fun addInner() {
        mExtension.map.put("addInner", "kb")
    }
}