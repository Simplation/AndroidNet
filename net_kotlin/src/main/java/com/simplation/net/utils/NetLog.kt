package com.simplation.net.utils

import android.util.Log
import com.simplation.net.NetKit

/**
 * Net 日志工具类
 */
object NetLog {
    fun v(tag: String = NetKit.options.logTag, log: Any) {
        if (!NetKit.options.logEnable) return
        Log.v(tag, log.toString())
    }

    fun d(tag: String = NetKit.options.logTag, log: Any) {
        if (!NetKit.options.logEnable) return
        Log.d(tag, log.toString())
    }

    fun i(tag: String = NetKit.options.logTag, log: Any) {
        if (!NetKit.options.logEnable) return
        Log.i(tag, log.toString())
    }

    fun w(tag: String = NetKit.options.logTag, log: Any) {
        if (!NetKit.options.logEnable) return
        Log.w(tag, log.toString())
    }

    fun e(tag: String = NetKit.options.logTag, log: Any) {
        if (!NetKit.options.logEnable) return
        Log.e(tag, log.toString())
    }
}