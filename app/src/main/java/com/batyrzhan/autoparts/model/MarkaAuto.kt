package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MarkaAuto(
    @SerializedName("id") @Expose
    val markaId: Int,
    @SerializedName("marka_name") @Expose
    val markaName: String
)
