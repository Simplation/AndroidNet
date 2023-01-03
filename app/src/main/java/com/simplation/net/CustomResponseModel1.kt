package com.simplation.net

import com.google.gson.annotations.SerializedName
import com.simplation.net.model.IResponseModel

data class CustomResponseModel1<T>(
    @SerializedName("code")
    val code: String?,
    @SerializedName("day")
    val day: String?,
    @SerializedName("result")
    val result: T?
) : IResponseModel {
    override fun isSuccessful(): Boolean = this.code == "1"

    override fun toString(): String {
        return "CustomResponseModel1(code=$code, day=$day, result=$result)"
    }
}
