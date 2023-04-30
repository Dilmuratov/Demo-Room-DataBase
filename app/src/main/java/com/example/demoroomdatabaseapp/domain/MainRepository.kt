package com.example.demoroomdatabaseapp.domain

import com.example.demoroomdatabaseapp.data.local.UserDao
import com.example.demoroomdatabaseapp.data.models.User

class MainRepository(val dao: UserDao) {
    suspend fun getListOfUser() = dao.getListOfUsers()

    suspend fun addUser(user: User) = dao.addUser(user)

    suspend fun editUser(user: User) = dao.editUser(user)

    suspend fun deleteUser(user: User) = dao.deleteUser(user)

    suspend fun searchUser(string: String) = dao.searchUser(string)
}