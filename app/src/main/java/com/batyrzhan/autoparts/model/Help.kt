package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Help (
    @SerializedName("id") @Expose
    val id: Int,
    @SerializedName("title") @Expose
    val title: String,
    @SerializedName("content") @Expose
    val content: String,
    )