package com.example.talentium.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.talentium.dao.UsersDao

import com.example.talentium.Model.Users

@Database(entities  = [Users::class], version = 1)
abstract class AppDataBase :RoomDatabase(){
    abstract fun userDao(): UsersDao

    companion object {
        @Volatile
        private var instance: AppDataBase? = null
        fun getDatabase(context: Context): AppDataBase  {
            if (instance == null) {
                synchronized(this) {
                    instance =
                        Room.databaseBuilder(context, AppDataBase::class.java, "talentium_database")
                            .allowMainThreadQueries().build()
                }
            }
            return instance!!
        }
    }
}