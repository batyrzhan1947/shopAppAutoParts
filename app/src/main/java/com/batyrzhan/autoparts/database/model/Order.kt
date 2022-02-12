package com.batyrzhan.autoparts.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batyrzhan.autoparts.database.model.Order.Companion.Table_Order


@Entity(tableName = Table_Order)
data class Order(
@PrimaryKey(autoGenerate = true)
@ColumnInfo(name = "orderId")
var orderId:Int = 0,
@ColumnInfo(name = "codeOrder")
var codeOrder: String,
@ColumnInfo(name = "orderList")
var orderList: String,
@ColumnInfo(name = "orderTotal")
var orderTotal: String,
@ColumnInfo(name = "dateTime")
var dateTime: String,
@ColumnInfo(name = "status")
var statusOrder: String
){
    companion object{
        const val Table_Order = "tbl_order"
    }

    constructor(code: String, orderList: String, orderTotal: String, dateTime: String): this(
        orderId=0, "","","","", StatusOrder.WAITING.status, ){
        codeOrder =code
        this.orderList = orderList
        this.orderTotal = orderTotal
        this.dateTime = dateTime
    }
}