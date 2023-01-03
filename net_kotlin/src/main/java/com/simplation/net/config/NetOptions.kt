package com.simplation.net.config

import com.simplation.net.parser.DefaultParser
import com.simplation.net.parser.IParser
import kotlin.reflect.KClass

/**
 * Net 配置类
 *
 * * logEnable   日志开关
 * * logTag      日志 Tag
 * * baseUrl     默认 baseUrl
 * * parserCls   默认数据解析器 cls
 */
class NetOptions(builder: Builder) {

    var logEnable: Boolean
    var logTag: String
    var baseUrl: String?
    var parserCls: KClass<out IParser>

    init {
        this.logEnable = builder.logEnable
        this.logTag = builder.logTag
        this.parserCls = builder.parserCls
        this.baseUrl = builder.baseUrl
    }

    class Builder {
        internal var logEnable: Boolean = NetConfig.DEFAULT_LOG_ENABLE
        internal var logTag: String = NetConfig.DEFAULT_LOG_TAG
        internal var baseUrl: String? = null
        internal var parserCls: KClass<out IParser> = DefaultParser::class

        fun setLogEnable(logEnable: Boolean): Builder {
            this.logEnable = logEnable
            return this
        }

        fun setLogTag(logTag: String): Builder {
            this.logTag = logTag
            return this
        }

        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun setParserCls(parserCls: KClass<out IParser>): Builder {
            this.parserCls = parserCls
            return this
        }

        fun build(): NetOptions {
            return NetOptions(this)
        }
    }

    override fun toString(): String {
        return "NetOptions(logEnable=$logEnable, logTag='$logTag', baseUrl=$baseUrl, parserCls=$parserCls)"
    }
}