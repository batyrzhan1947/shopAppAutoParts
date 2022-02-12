package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Shipping(
    @SerializedName("shipping_id") @Expose
    val shipping_id: Int,
    @SerializedName("shipping_name") @Expose
    val shipping_name: String
)
