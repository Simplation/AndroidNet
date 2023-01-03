package com.simplation.net

import androidx.collection.ArrayMap
import com.google.gson.reflect.TypeToken
import com.simplation.net.cipher.ICipher
import com.simplation.net.config.NetworkConfig
import com.simplation.net.config.RequestMethod
import com.simplation.net.config.RequestOptions
import com.simplation.net.parser.IParser

import kotlin.reflect.KClass

open class BaseRepository {
    /**
     * 异步请求
     */
    suspend inline fun <reified T> request(
        requestMethod: RequestMethod,
        baseUrl: String = "https://api.oick.cn/",
        function: String,
        headers: ArrayMap<String, Any?>? = null,
        params: ArrayMap<String, Any?>? = null,
        contentType: String = NetworkConfig.DEFAULT_CONTENT_TYPE,
        parserCls: KClass<out IParser> = CustomParser1::class,
        cipherCls: KClass<out ICipher>? = null
    ): T {
        val optionsBuilder =
            RequestOptions.Builder().setRequestMethod(requestMethod).setBaseUrl(baseUrl)
                .setFunction(function).setContentType(contentType)

        if (!headers.isNullOrEmpty()) {
            optionsBuilder.setHeaders(headers)
        }

        if (!params.isNullOrEmpty()) {
            optionsBuilder.setParams(params)
        }

        return NetKit.getRequestManager()
            .request(optionsBuilder.build(), object : TypeToken<T>() {}.type, parserCls, cipherCls)
    }

    /**
     * 异步请求
     */
    inline fun <reified T> requestSync(
        requestMethod: RequestMethod,
        baseUrl: String = "https://www.wanandroid.com/",
        function: String,
        headers: ArrayMap<String, Any?>? = null,
        params: ArrayMap<String, Any?>? = null,
        contentType: String = NetworkConfig.DEFAULT_CONTENT_TYPE,
        parserCls: KClass<out IParser> = CustomParser1::class,
        cipherCls: KClass<out ICipher>? = null
    ): T {
        val optionsBuilder =
            RequestOptions.Builder().setRequestMethod(requestMethod).setBaseUrl(baseUrl)
                .setFunction(function).setContentType(contentType)

        if (!headers.isNullOrEmpty()) {
            optionsBuilder.setHeaders(headers)
        }

        if (!params.isNullOrEmpty()) {
            optionsBuilder.setParams(params)
        }

        return NetKit.getRequestManager().syncRequest(
                optionsBuilder.build(), object : TypeToken<T>() {}.type, parserCls, cipherCls
            )
    }
}