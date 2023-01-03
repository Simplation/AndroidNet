package com.simplation.net.retrofit.manager

import androidx.collection.ArrayMap
import com.simplation.net.cipher.ICipher
import com.simplation.net.retrofit.converter.StringConverterFactory
import com.simplation.net.retrofit.interceptor.OkHttpLoggingInterceptor
import com.simplation.net.retrofit.interceptor.OkHttpRequestEncryptInterceptor
import com.simplation.net.retrofit.interceptor.OkHttpRequestHeaderInterceptor
import com.simplation.net.retrofit.interceptor.OkHttpResponseDecryptInterceptor
import com.simplation.net.utils.NetLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

import kotlin.reflect.KClass

/**
 * Retrofit 管理类，提供获取 OkHttpClient、Retrofit 等方法
 */
class RetrofitManager private constructor() {
    /**
     * Retrofit 集合
     * * key:     baseUrl
     * * value:   Retrofit
     */
    private val retrofitMap: ConcurrentHashMap<String, Retrofit> by lazy {
        ConcurrentHashMap()
    }

    /**
     * 加解密器 Cls 集合
     * * key:     url(baseUrl+function)
     * * value:   Cipher Clazz
     */
    private val cipherClsMap: HashMap<String, KClass<out ICipher>?> by lazy {
        hashMapOf()
    }

    /**
     * 加解密器集合
     * * key:     Cipher Clazz
     * * value:   Cipher instance
     */
    private val cipherMap: ConcurrentHashMap<KClass<out ICipher>, ICipher> by lazy {
        ConcurrentHashMap()
    }

    /**
     * 请求头集合
     * * key:     url(baseUrl+function)
     * * value:   headers
     */
    private val headersMap: HashMap<String, ArrayMap<String, Any?>?> by lazy {
        hashMapOf()
    }

    /**
     * 获取 OkHttpClient
     *
     * @return
     */
    private fun getOkHttpClient(): OkHttpClient {
        val timeout = 60 * 1000L
        val builder = OkHttpClient.Builder()
            .connectTimeout(timeout, TimeUnit.MILLISECONDS)
            .readTimeout(timeout, TimeUnit.MILLISECONDS)
            .writeTimeout(timeout, TimeUnit.MILLISECONDS)
            .addInterceptor(OkHttpRequestHeaderInterceptor())
            .addInterceptor(OkHttpLoggingInterceptor())
            .addInterceptor(OkHttpRequestEncryptInterceptor())
            .addInterceptor(OkHttpResponseDecryptInterceptor())
        return builder.build()
    }

    /**
     * 根据 baseUrl获取对应的 Retrofit 实例
     * 首次获取时同时保存起来，方便下次直接获取
     */
    fun getRetrofit(baseUrl: String): Retrofit {
        return retrofitMap.getOrPut(baseUrl) {
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(StringConverterFactory.create())
                .client(getOkHttpClient())
                .build()
        }
    }

    /**
     * 临时保存接口请求头
     * @param url baseUrl+function
     */
    fun saveHeaders(url: String, headers: ArrayMap<String, Any?>?) {
        if (headersMap.containsKey(url)) return
        headersMap[url] = headers
    }

    /**
     * 获取接口请求头，并移除
     * @param url baseUrl+function
     */
    fun getHeaders(url: String): ArrayMap<String, Any?>? {
        if (!headersMap.containsKey(url)) return null
        val headers = headersMap[url]
        headers?.apply {
            headersMap.remove(url)
        }
        return headers
    }

    /**
     * 临时保存接口加解密器
     * @param url baseUrl+function
     */
    fun saveCipher(url: String, cipherCls: KClass<out ICipher>?) {
        if (cipherClsMap.containsKey(url)) return
        cipherClsMap[url] = cipherCls
    }

    /**
     * 获取接口加解密器
     * @param url baseUrl+function
     */
    fun getCipher(url: String): ICipher? {
        val cipherCls = cipherClsMap[url] ?: return null
        val cipher: ICipher = cipherMap.getOrPut(cipherCls) {
            Class.forName(cipherCls.java.name).newInstance() as ICipher
        }
        NetLog.i(log = "RetrofitManager#getCipher() \nurl = $url\ncipherCls = $cipherCls\ncipher = $cipher")
        return cipher
    }

    /**
     * 移除接口加解密器
     */
    fun removeCipherCls(url: String) {
        if (!cipherClsMap.containsKey(url)) return
        cipherClsMap.remove(url)
    }

    companion object {
        val INSTANCE: RetrofitManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }
}
