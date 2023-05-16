package com.apu.neuroopdsmart

import android.os.Handler
import android.os.Looper
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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

inline fun now(): Long {
    return System.currentTimeMillis()
}

private class AndroidContinuation<T>(val cont: Continuation<T>) : Continuation<T> by cont {
    fun resume(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cont.resume(value)
        } else {
            Handler(Looper.getMainLooper()).post { cont.resume(value) }
        }
    }
    fun resumeWithException(exception: Throwable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cont.resumeWithException(exception)
        } else {
            Handler(Looper.getMainLooper()).post { cont.resumeWithException(exception) }
        }
    }
}

object Android : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> =
        AndroidContinuation(continuation)
}
