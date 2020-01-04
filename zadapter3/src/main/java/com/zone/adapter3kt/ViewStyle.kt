package com.zone.adapter3kt

import android.graphics.Rect
import com.zone.adapter3kt.section.QuickUpdateSection
import com.zone.adapter3kt.section.Section

/**
 *[2018] by Zone
 *
 * 间隔就不要弄了吧  因为那个和布局关系很大
 */
interface ViewStyle<T> {
    fun generateViewStyleOBJ(item: T): ViewStyleOBJ?
    fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ)
}

open class ViewStyleDefault<T> : ViewStyle<T> {
    override fun getItemViewType(position: Int, itemConfig: ViewStyleOBJ) {}

    override fun generateViewStyleOBJ(item: T): ViewStyleOBJ? {
        return null
    }
}

enum class Part { HEADER, FOOTER, CONTENT, OTHER }

class ViewStyleOBJ {
    var viewStyle: Int = -1 //布局类型
    var isFullspan = false  //是不是满行 一般用于grid布局和瀑布流布局
    //配置tags 到时候点击或者浏览的时候可以通过tags是否上报之类的。或者在列表中瞬间找到一中tag的一些item
    val tags: HashSet<String> by lazy { HashSet<String>() }
    // 用于存一些obj的,可以到时候通过key取出来用
    val otherMaps:HashMap<String,Any> by lazy { HashMap<String,Any>() }
    // 是否吸顶
    var isSticky: Boolean = false
    // 用与快速更新Rv某部分的数据
    var quickUpdateSection: QuickUpdateSection? = null
    // 用于细分复用
    var section: Section? = null
    // 用于区分 是头部 还是底部 还是内容的
    var part: Part = Part.CONTENT
    // item 的 根部局FrameLayout 的上下左右间隔  就是divderRect。
    var divderRect: Rect? = null
    //控制前面的是否隐藏
    var isHideBeforeDivder = false
    // 内部属性。 是否生成过。如果生成过 则不生成了
    internal var isGenerate = false

    fun part(part: Part): ViewStyleOBJ {
        this@ViewStyleOBJ.part = part
        return this
    }

    fun isFullspan(isFullspan: Boolean): ViewStyleOBJ {
        this@ViewStyleOBJ.isFullspan = isFullspan
        return this
    }

    fun section(section: Section): ViewStyleOBJ {
        this@ViewStyleOBJ.section = section
        return this
    }

    fun quickUpdateSection(quickUpdateSection: QuickUpdateSection): ViewStyleOBJ {
        this@ViewStyleOBJ.quickUpdateSection = quickUpdateSection
        return this
    }

    fun viewStyle(viewStyle: Int): ViewStyleOBJ {
        this@ViewStyleOBJ.viewStyle = viewStyle
        return this
    }

    fun addTags(vararg tags: String): ViewStyleOBJ {
        tags.forEach { this@ViewStyleOBJ.tags.add(it) }
        return this
    }

    fun addTags(tags: List<String>): ViewStyleOBJ {
        tags.forEach { this@ViewStyleOBJ.tags.add(it) }
        return this
    }

    fun isSticky(isSticky: Boolean): ViewStyleOBJ {
        this@ViewStyleOBJ.isSticky = isSticky
        return this
    }

    fun otherMaps(): HashMap<String,Any> {
        return this@ViewStyleOBJ.otherMaps
    }

    fun divderRect(divderRect: Rect?): ViewStyleOBJ {
        this@ViewStyleOBJ.divderRect = divderRect
        return this
    }
    fun hideBeforeDivderBottom(isHideBeforeDivderBottom: Boolean): ViewStyleOBJ {
        this@ViewStyleOBJ.isHideBeforeDivder = isHideBeforeDivderBottom
        return this
    }

    //把当前的值付给 other other:是真正的config
    internal fun setNowValue(other: ViewStyleOBJ): ViewStyleOBJ {
        other.isFullspan = isFullspan
        other.viewStyle = viewStyle
        tags.forEach { other.tags.add(it) }
        other.isSticky = isSticky
        other.isHideBeforeDivder = isHideBeforeDivder
        other.divderRect = divderRect
        other.quickUpdateSection = quickUpdateSection
        other.part = part
        other.isGenerate = true
        return this
    }
}