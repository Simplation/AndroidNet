package com.simplation.net

import com.google.gson.Gson
import com.simplation.net.exception.RequestException
import com.simplation.net.interf.IRequest
import com.simplation.net.parser.IParser
import com.simplation.net.utils.NetLog
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

import kotlin.reflect.KClass

/**
 *  RequestManager抽象类，自定义 RequestManager 需继承此类
 */
abstract class AbstractRequestManager : IRequest {
    protected val gson: Gson by lazy { Gson() }

    private val parserMap: ConcurrentHashMap<KClass<*>, IParser> by lazy {
        ConcurrentHashMap()
    }

    /**
     * 解析数据
     */
    protected fun <T> parse(
        url: String,
        data: String,
        type: Type,
        parserCls: KClass<out IParser>
    ): T {
        return getParser(parserCls).parse(url, data, type)
    }

    /**
     * 获取 Parser
     */
    private fun getParser(parserCls: KClass<out IParser>?): IParser {
        try {
            (parserCls ?: NetKit.options.parserCls).apply {
                val parser: IParser = parserMap.getOrPut(parserCls) {
                    Class.forName(java.name).newInstance() as IParser
                }
                NetLog.i(log = "${this@AbstractRequestManager.javaClass.simpleName}#getParser() parser = $parser, parser = $parser")
                return parser
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        throw RequestException(
            type = RequestException.Type.NATIVE,
            errMsg = "${this@AbstractRequestManager.javaClass.simpleName}#parse() parser 为空"
        )
    }

}