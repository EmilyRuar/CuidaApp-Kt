package com.example.cuidaplus
import android.content.Intent


import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
@Suppress("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        mostrarBienvenida()
        }

        private fun mostrarBienvenida(){
            object : CountDownTimer(5000,1000){

                override fun onTick(p0: Long) {

                }
                override fun onFinish() {
                    val intent = Intent(applicationContext, Inicio::class.java)
                    startActivity(intent)
                    finish()
                }
            }.start()
        }
    }
