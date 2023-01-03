package com.simplation.net.retrofit.interceptor

import com.simplation.net.utils.NetLog
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp 日志拦截器
 */
class OkHttpLoggingInterceptor : OkHttpBaseInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val request = chain.request()
        val response = chain.proceed(request)
        val method = request.method
        val logBuilder = StringBuilder()
        val headerString: String? = if (request.headers.size == 0) {
            null
        } else {
            val headersBuilder = StringBuilder()
            request.headers.forEach {
                headersBuilder.append(it.first).append(":").append(it.second).append("\t")
            }
            headersBuilder.toString()
        }
        logBuilder.append("...\n接口地址：${request.url}")
            .append("\n请求方式：$method")
            .append("\n请求头：$headerString")
            .append("\n请求参数：${getRequestInfo(request, method)}")
        val endTime = System.currentTimeMillis()
        logBuilder.append("\n请求耗时：${endTime - startTime}ms")
        logBuilder.append("\n请求响应：${getResponseInfo(response)}")
        NetLog.i(log = logBuilder.toString())
        return response
    }
}