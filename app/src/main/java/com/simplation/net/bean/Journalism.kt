package com.simplation.codelab.bean

import com.google.gson.annotations.SerializedName

data class Journalism(
    @SerializedName("code")
    val code: String?,
    @SerializedName("content")
    val content: String?
) {
    override fun toString(): String {
        return "Journalism(code=$code, content=$content)"
    }
}