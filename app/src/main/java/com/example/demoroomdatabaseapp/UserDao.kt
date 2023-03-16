package com.example.demoroomdatabaseapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

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

    @Query("SELECT * FROM users WHERE id=:id")
    suspend fun searchUserById(id: Int): User
}