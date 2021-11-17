package com.example.talentium.dao

import androidx.room.*
import com.example.talentium.Model.Users

@Dao
interface UsersDao {
    @Insert
    fun insert(edc: Users)

    @Update
    fun update(edc: Users)

    @Delete
    fun delete(edc: Users)

    @Query("SELECT * FROM users")
    fun getAllUsers(): MutableList<Users>
}