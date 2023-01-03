package com.simplation.net.retrofit.manager

import androidx.collection.ArrayMap
import com.simplation.net.AbstractRequestManager
import com.simplation.net.cipher.ICipher
import com.simplation.net.config.RequestMethod
import com.simplation.net.config.RequestOptions
import com.simplation.net.exception.RequestException
import com.simplation.net.parser.IParser
import com.simplation.net.retrofit.api.IApiService
import com.simplation.net.utils.NetLog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * 基于 Retrofit 实现的 RequestManager
 */
internal class RetrofitRequestManager private constructor() : AbstractRequestManager() {

    /**
     * 异步请求
     * @param options   请求参数
     * @param type      数据类型映射
     * @param parserCls 数据解析器
     * @param parserCls 数据加解密器
     */
    override suspend fun <T> request(
        options: RequestOptions,
        type: Type,
        parserCls: KClass<out IParser>,
        cipherCls: KClass<out ICipher>?
    ): T {
        NetLog.d(log = "${javaClass.simpleName}#request()\noptions = $options\ntype = $type\nparserCls = $parserCls\ncipherCls = $cipherCls")
        val function = options.function
        if (function.isNullOrEmpty()) {
            throw RequestException(
                type = RequestException.Type.NATIVE,
                errMsg = "${javaClass.simpleName}#request failure, reason: function is null or empty"
            )
        }
        val baseUrl = options.baseUrl
        if (baseUrl.isNullOrEmpty()) {
            throw RequestException(
                type = RequestException.Type.NATIVE,
                errMsg = "${javaClass.simpleName}#request failure, reason: baseUrl is null or empty"
            )
        }
        val url = "${options.baseUrl}${options.function}"
        return try {
            val apiService =
                RetrofitManager.INSTANCE.getRetrofit(baseUrl).create(IApiService::class.java)
            val headers = options.headers
            RetrofitManager.INSTANCE.saveHeaders("${baseUrl}${function}", headers)

            cipherCls.apply {
                RetrofitManager.INSTANCE.saveCipher("${baseUrl}${function}", this)
            }
            val params = options.params
            val contentType = options.contentType
            val result = when (options.requestMethod) {
                RequestMethod.GET -> {
                    if (params.isNullOrEmpty()) {
                        apiService.get(function)
                    } else {
                        apiService.get(function, params)
                    }
                }
                RequestMethod.POST -> {
                    if (params.isNullOrEmpty()) {
                        apiService.post(function)
                    } else {
                        apiService.post(function, convertParamsToRequestBody(params, contentType))
                    }
                }
                RequestMethod.PUT -> {
                    if (params.isNullOrEmpty()) {
                        apiService.put(function)
                    } else {
                        apiService.put(function, convertParamsToRequestBody(params, contentType))
                    }
                }
                RequestMethod.DELETE -> {
                    if (params.isNullOrEmpty()) {
                        apiService.delete(function)
                    } else {
                        apiService.delete(function, params)
                    }
                }
            }
            parse(url, result, type, parserCls)
        } catch (e: HttpException) {
            val response = e.response()
            val errorBody = response?.errorBody()?.string()
            val statusCode = response?.code()
            val errorMsg = response?.message()
            throw RequestException(
                type = RequestException.Type.NATIVE,
                url = url,
                statusCode = statusCode,
                errMsg = errorMsg ?: e.message,
                errBody = errorBody
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw RequestException(
                type = RequestException.Type.NATIVE,
                url = url,
                errMsg = e.message ?: ""
            )
        }
    }


    /**
     * 同步请求
     * @param options   请求参数
     * @param type      数据类型映射
     * @param parserCls 数据解析器
     * @param cipherCls 数据加解密器
     */
    override fun <T> syncRequest(
        options: RequestOptions,
        type: Type,
        parserCls: KClass<out IParser>,
        cipherCls: KClass<out ICipher>?
    ): T {
        NetLog.d(log = "${javaClass.simpleName}#syncRequest()\noptions = $options\ntype = $type\nparserCls = $parserCls\ncipherCls = $cipherCls")
        val function = options.function
        if (function.isNullOrEmpty()) {
            throw RequestException(
                type = RequestException.Type.NATIVE,
                errMsg = "${javaClass.simpleName}#syncRequest failure, reason: function is null or empty"
            )
        }
        val baseUrl = options.baseUrl
        if (baseUrl.isNullOrEmpty()) {
            throw RequestException(
                type = RequestException.Type.NATIVE,
                errMsg = "${javaClass.simpleName}#syncRequest failure, reason: baseUrl is null or empty"
            )
        }
        val url = "${options.baseUrl}${options.function}"
        return try {
            val apiService =
                RetrofitManager.INSTANCE.getRetrofit(baseUrl).create(IApiService::class.java)
            val headers = options.headers
            RetrofitManager.INSTANCE.saveHeaders("${baseUrl}${function}", headers)
            val params = options.params
            val contentType = options.contentType
            val result = when (options.requestMethod) {
                RequestMethod.GET -> {
                    if (params.isNullOrEmpty()) {
                        apiService.syncGet(function)
                    } else {
                        apiService.syncGet(function, params)
                    }
                }
                RequestMethod.POST -> {
                    if (params.isNullOrEmpty()) {
                        apiService.syncPost(function)
                    } else {
                        apiService.syncPost(
                            function,
                            convertParamsToRequestBody(params, contentType)
                        )
                    }
                }
                RequestMethod.PUT -> {
                    if (params.isNullOrEmpty()) {
                        apiService.syncPut(function)
                    } else {
                        apiService.syncPut(
                            function,
                            convertParamsToRequestBody(params, contentType)
                        )
                    }
                }
                RequestMethod.DELETE -> {
                    if (params.isNullOrEmpty()) {
                        apiService.syncDelete(function)
                    } else {
                        apiService.syncDelete(function, params)
                    }
                }
            }
            parse(url, result.execute().body()!!, type, parserCls)
        } catch (e: HttpException) {
            e.printStackTrace()
            val response = e.response()
            val errorBody = response?.errorBody()?.string()
            val statusCode = response?.code()
            val errorMsg = response?.message()
            throw RequestException(
                type = RequestException.Type.NATIVE,
                url = url,
                statusCode = statusCode,
                errMsg = errorMsg ?: e.message,
                errBody = errorBody
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw RequestException(
                type = RequestException.Type.NATIVE,
                url = url,
                errMsg = e.message ?: ""
            )
        }
    }


    /**
     * 将请求参数转换到 RequestBody
     * POST/PUT 请求适用
     */
    private fun convertParamsToRequestBody(
        params: ArrayMap<String, Any?>,
        contentType: String
    ): RequestBody {
        return gson.toJson(params).toRequestBody(contentType.toMediaTypeOrNull())
    }


    companion object {
        val INSTANCE: RetrofitRequestManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitRequestManager()
        }
    }
}