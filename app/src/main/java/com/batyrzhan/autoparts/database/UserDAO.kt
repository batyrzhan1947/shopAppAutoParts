package com.batyrzhan.autoparts.database

import androidx.room.*
import com.batyrzhan.autoparts.database.model.User
import com.batyrzhan.autoparts.database.model.User.Companion.Table_User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("select * from $Table_User where userId=1")
    fun getUser(): User

    @Query("select COUNT(userId) from $Table_User")
    fun getCount(): Int
}