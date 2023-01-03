package com.simplation.net.parser

import java.lang.reflect.Type

/**
 * 数据解析器抽象接口
 */
interface IParser {
    fun <T> parse(url: String, data: String, type: Type): T
}