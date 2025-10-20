package com.example.cuidaplus.data

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cuidaplus.R

class UserRepository(private val userDao: UserDao){

    suspend fun insertUser(user: UserEntity){
        userDao.insertUser(user)
    }

    suspend fun login(correo: String, contrasena: String): UserEntity? {
        return userDao.getUserByCredentials(correo, contrasena)
    }

    suspend fun getAllUsers(): List<UserEntity>{
        return userDao.getAllUsers()
    }

}