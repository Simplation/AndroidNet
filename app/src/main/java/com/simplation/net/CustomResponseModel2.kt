package com.simplation.net

import com.google.gson.annotations.SerializedName
import com.simplation.net.model.IResponseModel

data class CustomResponseModel2<T>(
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: T?
) : IResponseModel {
    override fun isSuccessful(): Boolean = this.message == "success"
}
