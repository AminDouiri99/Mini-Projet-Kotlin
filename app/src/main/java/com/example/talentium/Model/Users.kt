package com.example.talentium.Model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    @ColumnInfo(name = "avatar")
    val avatar: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "username")
    val username: String

) {
    constructor(_id: String, avatar: String,name: String, email: String, username: String) :
            this(_id, avatar, email, username)
}
