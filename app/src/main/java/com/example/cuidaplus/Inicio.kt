package com.example.cuidaplus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Inicio : AppCompatActivity() {

private lateinit var Btn_ir_Registros : Button
private lateinit var Btn_ir_logeo : Button
private lateinit var Btn_ir_invitado: Button
private lateinit var logoCuidApp: ActivityResultContracts.PickVisualMedia.ImageOnly

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

            Btn_ir_Registros = findViewById(R.id.Btn_ir_registros)
            Btn_ir_logeo = findViewById(R.id.Btn_ir_logeo)

            Btn_ir_Registros.setOnClickListener {
                val intent = Intent(this@Inicio, RegistroActivity::class.java)
                Toast.makeText(applicationContext, "Registros", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            Btn_ir_logeo.setOnClickListener {
                val intent = Intent(this@Inicio, LoginActivity::class.java)
                Toast.makeText(applicationContext, "Login", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }


        }


    }
