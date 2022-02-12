package com.batyrzhan.autoparts.database

import androidx.room.*
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.autoparts.database.model.Order.Companion.Table_Order
import io.reactivex.Single

@Dao
interface OrderDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addOrder(order: Order)

    @Delete
    fun deleteOrder(order: Order)

    @Query("select * from $Table_Order order by orderId DESC")
    fun getAllOrders(): Single<List<Order>>

    @Query("select * from $Table_Order where codeOrder=:code")
    fun getOrderWithCode(code:String): Single<Order>

    @Query("UPDATE $Table_Order SET status=:statusOrder where codeOrder=:code")
    suspend fun updateStatusOrder(code: String,statusOrder:String)

}