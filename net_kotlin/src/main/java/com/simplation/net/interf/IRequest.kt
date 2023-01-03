package com.simplation.net.interf

import com.simplation.net.cipher.ICipher
import com.simplation.net.config.RequestOptions
import com.simplation.net.parser.IParser
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * 抽象的接口请求封装，自定义 RequestManager 实现此接口即可
 */
interface IRequest {

    /**
     * 异步请求
     * @param options   请求参数
     * @param type      数据类型映射
     * @param parserCls 数据解析器
     * @param cipherCls 数据加解密器
     */
    suspend fun <T> request(
        options: RequestOptions,
        type: Type,
        parserCls: KClass<out IParser>,
        cipherCls: KClass<out ICipher>? = null
    ): T

    /**
     * 同步请求
     * @param options   请求参数
     * @param type      数据类型映射
     * @param parserCls 数据解析器
     * @param cipherCls 数据加解密器
     */
    fun <T> syncRequest(
        options: RequestOptions,
        type: Type,
        parserCls: KClass<out IParser>,
        cipherCls: KClass<out ICipher>? = null
    ): T
}