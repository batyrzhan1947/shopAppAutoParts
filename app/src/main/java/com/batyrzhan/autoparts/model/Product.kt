package com.batyrzhan.autoparts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("p_id") @Expose
    val p_id: Int,
    @SerializedName("p_name") @Expose
    val p_name: String,
    @SerializedName("category_fk") @Expose
    val category_fk: Int,
    @SerializedName("p_brand") @Expose
    val p_brand: String,

    @SerializedName("p_articul") @Expose
    val p_articul: String,

    @SerializedName("p_oem") @Expose
    val p_oem: String,

    @SerializedName("p_type") @Expose
    val p_type: String,

    @SerializedName("p_target") @Expose
    val p_target: String,

    @SerializedName("category_name") @Expose
    val category_name: String,

    @SerializedName("p_price") @Expose
    val p_price: Double,

    @SerializedName("p_status") @Expose
    val p_status: String,

    @SerializedName("p_img") @Expose
    val p_img: String,

    @SerializedName("p_description") @Expose
    val p_description: String,

    @SerializedName("p_quantity") @Expose
    val p_quantity: Int,

    @SerializedName("currency_id") @Expose
    val currency_id: Int,

    @SerializedName("tax") @Expose
    val tax: String,

    @SerializedName("currency_code") @Expose
    val currency_code: String,

    @SerializedName("currency_name") @Expose
    val currency_name: String
) {
    companion object {
        val ASC_TOURS_PRICE: Comparator<Product> =
            Comparator { o1, o2 ->
                return@Comparator o2.p_price.let { o1.p_price.compareTo(it) }
            }
        val DESC_TOURS_PRICE: Comparator<Product> =
            Comparator { o1, o2 ->
                return@Comparator o1.p_price.let { o2.p_price.compareTo(it) }
            }
    }
}

