package com.batyrzhan.autoparts.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.batyrzhan.autoparts.database.model.User.Companion.Table_User

@Entity(tableName = Table_User)
data class User(
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: Int = 1,
    @ColumnInfo(name = "userName")
    var userName: String,
    @ColumnInfo(name = "userEmail")
    var userEmail: String,
    @ColumnInfo(name = "userPhone")
    var userPhone: String,
    @ColumnInfo(name = "userAdress")
    var userAdress: String

) {
    companion object {
        const val Table_User = "table_user"
    }
}