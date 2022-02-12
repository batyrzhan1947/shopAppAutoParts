package com.batyrzhan.autoparts.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.batyrzhan.autoparts.database.model.Cart
import com.batyrzhan.autoparts.database.model.Order
import com.batyrzhan.autoparts.database.model.User

@Database(
    entities = [Cart::class, User::class, Order::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun cartDao(): CartDAO
    abstract fun userDao(): UserDAO
    abstract fun orderDao(): OrderDAO

    companion object {
        private var instance: AppDataBase? = null
        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDataBase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDataBase::class.java,
                "commerceDb"
            ).fallbackToDestructiveMigration().build()
    }
}
