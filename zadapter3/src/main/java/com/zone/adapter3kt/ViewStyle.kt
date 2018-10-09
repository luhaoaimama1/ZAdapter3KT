package com.zone.adapter3kt

import android.graphics.Rect
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
    var viewStyle: Int = -1
    var isFullspan = false
    val tags: HashSet<String> by lazy { HashSet<String>() }
    var isSticky: Boolean = false
    var section: Section? = null
    var part: Part = Part.CONTENT
    var userage: Any? = null
    var divderRect: Rect? = null
    //控制前面的是否隐藏
    var isHideBeforeDivder = false
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

    fun useage(useage: Any?): ViewStyleOBJ {
        this@ViewStyleOBJ.userage = useage
        return this
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
        other.section = section
        other.part = part
        other.isGenerate = true
        return this
    }
}