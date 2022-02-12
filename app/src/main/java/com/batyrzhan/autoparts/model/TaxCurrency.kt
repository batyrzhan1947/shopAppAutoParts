package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TaxCurrency(
    @SerializedName(value = "tax") @Expose
    val tax: Double,
    @SerializedName("currency_code") @Expose
    val currency_code: String
)
