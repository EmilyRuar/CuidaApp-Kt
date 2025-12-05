
package com.example.cuidaplus.data.remote
import com.example.cuidaplus.data.remote.AuthApiService
import com.example.cuidaplus.data.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // Para Android Emulator
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: AuthApiService = retrofit.create(AuthApiService::class.java)

}

