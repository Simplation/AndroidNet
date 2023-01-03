package com.simplation.net.parser

import com.google.gson.Gson

/**
 * 抽象的数据解析器
 *      如果需要自定义解析器，继承此 [AbstractParser]，实现 [parse] 方法即可
 *       @see [DefaultParser]
 */
abstract class AbstractParser : IParser {
    protected val gson: Gson by lazy { Gson() }
}