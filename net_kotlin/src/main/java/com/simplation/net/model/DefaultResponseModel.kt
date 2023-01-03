package com.simplation.net.model

/**
 * 默认的 ResponseModel
 *
 * * code 返回码
 * * msg  返回信息
 * * data 返回数据
 */
internal data class DefaultResponseModel<T>(var code: Int?, var msg: String?, val data: T?) :
    IResponseModel {
    override fun isSuccessful() = code == 0

    override fun toString(): String {
        return "DefaultResponseModel(code=$code, msg=$msg, data=$data)"
    }
}