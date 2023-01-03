package com.simplation.net.exception

/**
 * 封装的请求异常
 *
 * * [type]         异常类型
 * * [url]          接口地址(baseUrl+function)
 * * [statusCode]   http状态码
 * * [errCode]      业务错误码
 * * [errMsg]       业务错误信息
 * * [errBody]      错误详细信息
 */
class RequestException(
    private val type: Type = Type.NATIVE,
    private val url: String? = null,
    private val statusCode: Int? = null,
    private val errCode: Int? = null,
    private val errMsg: String?,
    private val errBody: String? = null
) : Throwable(errMsg) {

    /**
     * 异常类型
     *
     * [NATIVE]     本地异常
     * [NETWORK]    网络异常
     */
    enum class Type {
        NATIVE, NETWORK
    }

    override fun toString(): String {
        return "RequestException(type=$type, url=$url, statusCode=$statusCode, errCode=$errCode, errMsg=$errMsg)"
    }
}