package com.example.demoroomdatabaseapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.demoroomdatabaseapp.domain.MainRepository
import com.example.demoroomdatabaseapp.data.local.UserDataBase
import com.example.demoroomdatabaseapp.data.models.User

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val repository = MainRepository(UserDataBase.getInstance(application).getUsersDao())

    private val _getAllUserLiveData = MutableLiveData<List<User>>()
    val getAllUserLiveData: LiveData<List<User>> get() = _getAllUserLiveData

    suspend fun getAllUsers() {
        _getAllUserLiveData.value = repository.getListOfUser()
    }

    suspend fun searchUsers(string: String) {
        _getAllUserLiveData.value = repository.searchUser(string)
    }

    suspend fun addUser(user: User) {
        repository.addUser(user)
    }

    suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }

    suspend fun editUser(user: User) {
        repository.editUser(user)
    }
}