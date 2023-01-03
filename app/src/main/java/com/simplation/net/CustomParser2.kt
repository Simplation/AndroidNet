package com.simplation.net

import com.simplation.net.exception.RequestException
import com.simplation.net.parser.AbstractParser
import com.simplation.net.utils.NetLog
import java.lang.reflect.Type

/**
 * 自定义的数据解析器，用于解析 [CustomResponseModel2] 格式数据
 */
class CustomParser2 : AbstractParser() {
    override fun <T> parse(url: String, data: String, type: Type): T {
        NetLog.i(log = "${javaClass.simpleName}#parse() data = $data, type = $type")
        var errMsg: String?
        var responseModel: CustomResponseModel2<T>? = null
        try {
            responseModel = gson.fromJson<CustomResponseModel2<T>>(
                data,
                CustomResponseModel2::class.java
            )
            if (!responseModel.isSuccessful()) {
                errMsg = "responseModel is failure"
            } else {
                return gson.fromJson(gson.toJson(responseModel.data), type)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            errMsg = e.message
        }

        throw RequestException(
            type = RequestException.Type.NATIVE,
            url = url,
            errCode = -1,
            errMsg = "${javaClass.simpleName}#parse() failure\nerrMsg = $errMsg\ntype = $type\nresponseModel = $responseModel\ndata = $data"
        )
    }
}