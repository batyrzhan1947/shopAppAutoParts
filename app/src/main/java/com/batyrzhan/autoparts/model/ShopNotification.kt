package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopNotification(
    @SerializedName("id") @Expose
    val notify_id: Int,
    @SerializedName("message") @Expose
    val notify_msg: String,
    @SerializedName("image") @Expose
    val notify_img: String,
    @SerializedName("title") @Expose
    val notify_title: String,
    @SerializedName("link") @Expose
    val notify_link: String
)
