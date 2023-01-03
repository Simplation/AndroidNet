package com.simplation.net.parser

import com.simplation.net.exception.RequestException
import com.simplation.net.model.DefaultResponseModel
import com.simplation.net.utils.NetLog
import java.lang.reflect.Type

/**
 * 默认数据解析器
 *
 * ResponseModel 包含
 * * code
 * * msg
 * * data
 */
internal class DefaultParser : AbstractParser() {
    override fun <T> parse(url: String, data: String, type: Type): T {
        NetLog.i(log = "${javaClass.simpleName}#parse() data = $data, type = $type")

        var errMsg: String?
        var responseModel: DefaultResponseModel<T>? = null
        try {
            responseModel = gson.fromJson<DefaultResponseModel<T>>(
                data,
                DefaultResponseModel::class.java
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
            errCode = responseModel?.code ?: -1,
            errMsg = "${javaClass.simpleName}#parse() failure\nerrMsg = $errMsg\ntype = $type\nresponseModel = $responseModel\ndata = $data"
        )
    }

}