package com.example.demoroomdatabaseapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.demoroomdatabaseapp.data.models.User

@Dao
interface   UserDao {

    @Query("SELECT * FROM users")
    suspend fun getListOfUsers(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun editUser(user: User)

    @Query("SELECT * FROM users WHERE name LIKE '%' || :str || '%' OR surname LIKE '%' || :str || '%' ")
    suspend fun searchUser(str: String): List<User>

    @Delete
    suspend fun deleteUser(user: User)
}