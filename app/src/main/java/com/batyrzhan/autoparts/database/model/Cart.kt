package com.batyrzhan.autoparts.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batyrzhan.autoparts.database.model.Cart.Companion.Table_Name


@Entity(tableName = Table_Name)
data class Cart(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cartId")
    var cartId: Int = 0,
    @ColumnInfo(name = "productName")
    var productName: String,
    @ColumnInfo(name = "productQuantity")
    var productQuantity: Int,
    @ColumnInfo(name = "totalProductsPrice")
    var totalProductsPrice: Double,
    @ColumnInfo(name = "productImg")
    var productImg: String,
    @ColumnInfo(name = "currency")
    var currency: String

) {
    companion object {
        const val Table_Name = "table_cart"
    }

}
