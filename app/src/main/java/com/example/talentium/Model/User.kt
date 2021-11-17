package com.example.talentium.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class User(

    val _id: String,

    val avatar: String,

    val email: String,

    val username: String,

    val name: String,

    val lastname: String,

    val password: String,

    val followers: Array<String>,

    val following: Array<String>,

    val profile: String,

    val confirmed: Boolean,


    )
