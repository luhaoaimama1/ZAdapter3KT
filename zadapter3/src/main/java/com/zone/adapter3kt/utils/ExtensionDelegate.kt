package com.zone.adapter3kt.utils

import kotlin.reflect.KProperty

/**
 *[2018] by Zone
 */
class ExtensionDelegate {
    val map = HashMap<Any, Any?>()
    operator fun <T> getValue(thisRef: Any, property: KProperty<*>): T? {
        return if (map.get(property.name) == null) null else map.get(property.name) as T
    }

    operator fun <T> setValue(thisRef: Any, property: KProperty<*>, value: T) {
        map.put(property.name, value)
    }
}