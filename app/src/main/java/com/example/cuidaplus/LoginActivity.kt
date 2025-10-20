package com.example.cuidaplus

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.launch
import com.example.cuidaplus.util.PreferencesManager
import androidx.navigation.NavController


                class LoginActivity : AppCompatActivity() {
                    private lateinit var btnLogin: Button
                    override fun onCreate(savedInstanceState: Bundle?) {
                        super.onCreate(savedInstanceState)
                        setContentView(R.layout.activity_login)
                        supportActionBar?.title="Login"

                        // Referencia al botón del XML
                        btnLogin = findViewById(R.id.Btn_Login)

                        // Acción al hacer click
                        btnLogin.setOnClickListener {
                            // Abrir HomePrincipalActivity
                            val intent = Intent(this@LoginActivity, HomePrincipalActivity::class.java)
                            startActivity(intent)

            }  }  }



