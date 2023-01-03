package com.simplation.net.retrofit.interceptor

import com.simplation.net.config.RequestMethod
import com.simplation.net.retrofit.manager.RetrofitManager
import com.simplation.net.utils.NetLog
import okhttp3.Interceptor
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder
import java.nio.charset.Charset

/**
 * OkHttp 请求数据加密拦截器
 */
class OkHttpRequestEncryptInterceptor : OkHttpBaseInterceptor() {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val requestMethod = getRequestMethod(request.method) ?: return chain.proceed(request)
        val url = request.url
        val urlString = url.toString()
        when (requestMethod) {
            RequestMethod.GET, RequestMethod.DELETE -> {
                if (!url.encodedQuery.isNullOrEmpty()) {
                    try {
                        val api = "${url.scheme}://${url.host}:${url.port}${url.encodedPath}".trim()
                        RetrofitManager.INSTANCE.getCipher(
                            urlString.substring(
                                0,
                                urlString.indexOf("?")
                            )
                        )?.apply {
                            val newApi = "${api}?${getParamName()}=${encrypt(url.encodedQuery)}"
                            request = request.newBuilder().url(newApi).build()
                            NetLog.i(log = "${TAG}#intercept() \napi = $api\nnewApi = $newApi")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetLog.e(log = "${TAG}#intercept() encrypt failure, reason:${e.message}")
                        chain.proceed(request)
                    }
                }
            }

            RequestMethod.POST, RequestMethod.PUT -> {
                request.body?.let { body ->
                    var charset: Charset? = null
                    val contentType = body.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(Charsets.UTF_8)
                        // 如果 contentType 为 multipart，则不进行加密
                        if (contentType.type.lowercase() == "multipart") {
                            return chain.proceed(request)
                        }
                    }
                    try {
                        RetrofitManager.INSTANCE.getCipher(urlString)?.apply {
                            val buffer = Buffer()
                            body.writeTo(buffer)
                            val requestData = URLDecoder.decode(
                                buffer.readString(charset!!).trim(),
                                Charsets.UTF_8.name()
                            )
                            val encryptData = encrypt(requestData)
                            encryptData?.apply {
                                val newRequestBody = toRequestBody(contentType)
                                val newRequestBuilder = request.newBuilder()
                                when (requestMethod) {
                                    RequestMethod.POST -> {
                                        newRequestBuilder.post(newRequestBody)
                                    }
                                    RequestMethod.PUT -> {
                                        newRequestBuilder.put(newRequestBody)
                                    }
                                    else -> {}
                                }
                                request = newRequestBuilder.build()
                                NetLog.i(log = "${TAG}#intercept() \nrequestBody = $body\nnewRequestBody = $newRequestBody\nrequestData = $requestData\nencryptData = $encryptData")
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        NetLog.e(log = "${TAG}#intercept() encrypt failure, reason:${e.message}")
                        chain.proceed(request)
                    }
                }
            }
        }
        return chain.proceed(request)
    }

    companion object {
        private const val TAG = "OkHttpRequestEncryptInterceptor"
    }
}