package com.batyrzhan.autoparts.database

import androidx.room.*
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.autoparts.database.model.Cart.Companion.Table_Name
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CartDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProductToCart(cart: Cart)

    @Query("select count() from $Table_Name where cartId =:id")
    fun ifExistsById(id: Int): Int

    @Query("update $Table_Name set productQuantity= productQuantity + :quantity, totalProductsPrice= totalProductsPrice +:totalPrice where cartId =:id")
    fun updateData(id: Int, quantity: Int, totalPrice: Double)

    @Query("SELECT COUNT(cartId) from $Table_Name")
    fun getCountCarts(): Single<Int>

    @Query("SELECT SUM(totalProductsPrice) FROM $Table_Name")
    fun getAllTotalPrice():Maybe<Double?>

    @Query("select * from $Table_Name")
    fun getAllCarts(): Single<List<Cart>>

    @Delete
    fun deleteCart(cart: Cart)

    @Query("DELETE FROM $Table_Name")
    fun deleteAllCarts()

//   @Query("""SELECT * FROM $TABLE_NAME WHERE $COLUMN_FIRST_NAME LIKE :first
//            AND $COLUMN_LAST_NAME LIKE :last LIMIT 1""")
}