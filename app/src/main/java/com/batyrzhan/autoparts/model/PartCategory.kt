package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PartCategory(
    @SerializedName("category_id") @Expose
    val id: Int,
    @SerializedName("category_name")
    @Expose
    var name: String,
    @SerializedName("category_img")
    @Expose
    var img: String
)