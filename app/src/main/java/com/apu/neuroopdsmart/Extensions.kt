package com.apu.neuroopdsmart

fun <C : MutableCollection<V>, V> C.addSwitch(value: V) {
    if (!this.contains(value)) {
        add(value)
    } else {
        remove(value)
    }
}

fun Boolean.toInt() = if (this) 1 else 0

fun <T : Any> maketag(type: T): String {
    return type.javaClass.canonicalName ?: type.javaClass.name
}
