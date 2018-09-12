package com.zone.adapter3kt.data

/**
 * Copyright (c) 2018 BiliBili Inc.
 *[2018/9/5] by Zone
 */

open class HFListHistory<T> : HFList<T>() {

    var notifyHistory = ArrayList<Operate>()
    var enableHistory = false

    class Operate {
        var range: Range? = null
        var move: Move? = null
        var single: Single? = null

        class Range {
            var pos = 0
            var size = 0
            var mode = MMOde.ADD

        }

        class Move {
            var pos = 0
            var toPos = 0
        }

        class Single {
            var index_ = 0
            var mode = MMOde.ADD
        }

        enum class MMOde {
            ADD, CHANGE, REMOVE
        }
    }

    override fun notifyItemChangedInner(index: Int) {
        if (!enableHistory) return
        val operate = Operate().apply {
            single = Operate.Single().apply {
                index_ = index
                mode = Operate.MMOde.CHANGE
            }
        }
        notifyHistory.add(operate)
    }

    override fun notifyItemRangeChangedInner(positionStart: Int, itemCount: Int, payload: Any?) {
        if (!enableHistory) return
        val operate = Operate().apply {
            range = Operate.Range().apply {
                this@apply.pos = pos
                size = itemCount
                mode = Operate.MMOde.CHANGE
            }
        }
        notifyHistory.add(operate)
    }


    override fun notifyItemMovedInner(fromPosition: Int, toPosition: Int) {
        if (!enableHistory) return
        val operate = Operate().apply {
            move = Operate.Move().apply {
                pos = fromPosition
                toPos = toPosition
            }
        }
        notifyHistory.add(operate)

    }

    // 调用内部通知  这样覆盖方法即可

    override fun notifyItemRangeRemovedInner(positionStart: Int, itemCount: Int) {
        if (!enableHistory) return
        val operate = Operate().apply {
            range = Operate.Range().apply {
                this@apply.pos = positionStart
                size = itemCount
                mode = Operate.MMOde.REMOVE
            }
        }
        notifyHistory.add(operate)

    }

    override fun notifyItemInsertedInner(position: Int) {
        if (!enableHistory) return
        val operate = Operate().apply {
            single = Operate.Single().apply {
                index_ = position
                mode = Operate.MMOde.ADD
            }
        }
        notifyHistory.add(operate)
    }

    override fun notifyItemRangeInsertedInner(positionStart: Int, itemCount: Int) {
        if (!enableHistory) return
        val operate = Operate().apply {
            range = Operate.Range().apply {
                this@apply.pos = positionStart
                size = itemCount
                mode = Operate.MMOde.ADD
            }
        }
        notifyHistory.add(operate)
    }

}