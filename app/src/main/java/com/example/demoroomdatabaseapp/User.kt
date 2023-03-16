package com.example.demoroomdatabaseapp

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @DrawableRes val profile: Int,
    val name: String,
    val surname: String
)
