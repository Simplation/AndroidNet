package com.simplation.net.retrofit.interceptor

import com.simplation.net.retrofit.manager.RetrofitManager
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp 请求头拦截器
 */
class OkHttpRequestHeaderInterceptor : OkHttpBaseInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        return chain.proceed(request.newBuilder().headers(getHeaders(url)).build())
    }

    private fun getHeaders(url: String): Headers {
        val headersBuilder = Headers.Builder()
        RetrofitManager.INSTANCE.getHeaders(url)?.forEach {
            headersBuilder.add(it.key, it.value.toString())
        }
        return headersBuilder.build()
    }
}