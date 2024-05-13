package com.example.mc_project

// User.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val username: String,
    val email: String,
    val password: String,
    val mobileNo: String
)
