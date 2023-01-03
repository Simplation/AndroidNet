package com.simplation.net.model

/**
 * ResponseModel 接口，自定义 ResponseModel 需继承此接口并实现 [isSuccessful] 方法
 */
interface IResponseModel {
    fun isSuccessful(): Boolean
}