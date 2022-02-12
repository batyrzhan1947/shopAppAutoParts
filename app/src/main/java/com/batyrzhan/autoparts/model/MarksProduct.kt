package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MarksProduct(
    @SerializedName("marks") @Expose
    val marks: String
)
