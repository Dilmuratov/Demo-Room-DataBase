package com.example.demoroomdatabaseapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room

import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UserDataBase(): RoomDatabase() {

    abstract fun getUsersDao(): UserDao

    companion object{
        const val DATABASE_NAME = "db_name"

        fun getInstance(context: Context): UserDataBase {
            return Room.databaseBuilder(
                context,
                UserDataBase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}

