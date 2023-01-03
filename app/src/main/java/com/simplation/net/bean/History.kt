package com.simplation.codelab.bean

import com.google.gson.annotations.SerializedName

data class History(
    @SerializedName("date")
    val date: String?,
    @SerializedName("title")
    val title: String?
) {
    override fun toString(): String {
        return "History(date=$date, title=$title)"
    }
}