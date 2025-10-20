package com.example.cuidaplus.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE correo = :correo AND contrasena = :contrasena LIMIT 1")
    suspend fun getUserByCredentials(correo: String, contrasena: String): UserEntity

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

}
