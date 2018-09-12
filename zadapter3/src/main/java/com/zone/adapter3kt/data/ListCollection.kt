package com.zone.adapter3kt.data

class ListCollection<T> {

    var list = ArrayList<ArrayList<T>>()

    private fun posNoAddIsIllegal(pos: Int) = pos < 0 || pos >= count()

    fun count(): Int {
        var tempCount = 0
        list.forEach { tempCount += it.count() }
        return tempCount
    }

    //0开始 没有就-1
    fun beginIndexWithNoHas(item: ArrayList<T>): Int {
        var tempCount = 0
        var hasItem = false
        for (i in 0 until list.size) {
            if (list[i] == item) {
                hasItem = true
                break
            }
            tempCount += list[i].count()
        }
        return if (hasItem) tempCount else -1
    }

    //没有就0
    fun beginIndexFromZero(item: ArrayList<T>): Int {
        var tempCount = 0
        for (i in 0 until list.size) {
            if (list[i] == item) {
                break
            }
            tempCount += list[i].count()
        }
        return tempCount
    }

    fun loop(method: (index: Int, item: T) -> Boolean) {
        var iterPos = 0
        for (index in 0 until list.size) A@ {
            for (index2 in 0 until list[index].size) {
                if (method(iterPos, list[index][index2])) return@A
                iterPos++
            }
        }
    }

    fun getItem(pos: Int): T? {
        if (posNoAddIsIllegal(pos)) return null
        var result: T? = null
        loop { index, item ->
            if (index == pos) {
                result = item
                true
            } else false
        }
        return result
    }

    fun setItem(pos: Int, item: T, method: (list: ArrayList<T>) -> Unit) {
        if (posNoAddIsIllegal(pos)) return
        var iterPos = 0
        for (index in 0 until list.size) A@ {
            for (index2 in 0 until list[index].size) {
                if (iterPos == pos) {
                    list[index].set(index2, item)
                    method(list[index])
                    return@A
                }
                iterPos++
            }
        }
    }

    fun index(item: T): Int {
        var resultIndex = -1
        for (i in 0 until list.size) {
            val indexTemp = list[i].indexOf(item)
            if (indexTemp != -1) {
                resultIndex = indexTemp
                break
            }
        }
        return resultIndex
    }

    fun remove(positionStart: Int, itemCount: Int, method: (removeItem: T) -> Unit): Boolean {
        if (posNoAddIsIllegal(positionStart) || posNoAddIsIllegal(positionStart + itemCount - 1)) return false
        var iterPos = count() - 1 //从0开始 总共个数

        // return true false就行 因为位置我做了验证 所以如果结尾能删除就代表成功了
        var removeSuccess = false
        var isBreak = false
        for (i in list.size - 1 downTo 0) {
            if (isBreak) break
            for (j in list.get(i).size - 1 downTo 0) {
                if (iterPos >= positionStart && iterPos < positionStart + itemCount) {
                    method(list.get(i).get(j))
                    list.get(i).removeAt(j)
                    if (!removeSuccess) removeSuccess = true
                }
                if (iterPos < positionStart) {
                    isBreak = true
                    break
                }
                iterPos--
            }
        }
        return removeSuccess
    }
}
