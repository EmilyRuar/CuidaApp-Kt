package com.example.cuidaplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cuidaplus.ui.theme.CuidaplusTheme
import com.example.cuidaplus.util.PreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//////
import com.example.cuidaplus.data.AppDatabase
import com.example.cuidaplus.data.UserEntity
import com.example.cuidaplus.data.UserRepository
//////


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencesManager = PreferencesManager(this)

        setContent {

            val database = AppDatabase.getDatabase(this)
            val userRepository = UserRepository(database.userDao())


            val isLoggedIn by preferencesManager.isLoggedInFlow.collectAsState(initial = false)
            val (nombre, apellido) = preferencesManager.getUserNameFlow.collectAsState(initial = "" to "").value

            val navController = rememberNavController()

            val startDestination = if (isLoggedIn && nombre.isNotEmpty() && apellido.isNotEmpty()){
                "home/${nombre}/${apellido}"
            }else{
                "login"
            }

            CuidaplusTheme {
                AppNavigator(navController, preferencesManager, startDestination, userRepository)
            }

            LaunchedEffect(Unit) {
                CoroutineScope(Dispatchers.IO).launch {
                    userRepository.insertUser(
                        UserEntity(
                            nombre = "Victor",
                            apellido = "Toledo",
                            rut = "11.111.111-1",
                            correo = "victor@gmail.com",
                            contrasena = "123456"
                        )
                    )

                    userRepository.insertUser(
                        UserEntity(
                            nombre = "Andres",
                            apellido = "Miranda",
                            rut = "22.222.222-2",
                            correo = "andres@gmail.com",
                            contrasena = "abcdef"
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigator(
    navController: NavHostController,
    preferencesManager: PreferencesManager,
    startDestination: String,
    userRepository: UserRepository
) {
    //val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination){
        composable(route = "login") {
            LoginForm(navController, preferencesManager, userRepository)
        }

        composable (
            route = "home/{nombre}/{apellido}",
            arguments = listOf(
                navArgument(name = "nombre") {type = NavType.StringType},
                navArgument(name = "apellido") {type = NavType.StringType}
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val apellido = backStackEntry.arguments?.getString("apellido") ?: ""
            HomeScreen(nombre, apellido, navController)
        }
    }
}

@Composable
fun LoginForm(navController: androidx.navigation.NavController, preferencesManager: PreferencesManager, userRepository: UserRepository){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = {Text("Email")},
            isError = emailError,
            modifier = Modifier.fillMaxWidth()
        )
        if(emailError){
            Text("Correo ingresado inválido", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = it.length < 6
            },
            label = {Text("Password")},
            isError = passwordError,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        if(passwordError){
            Text("La contraseña ingresada debe tener al menos 6 caracteres", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                //val user = Usuarios.lista.find { it.correo == email && it.contrasena == password }
                //if(user != null){
                CoroutineScope(Dispatchers.IO).launch {


                    //preferencesManager.saveLoginState(true)
                    //preferencesManager.saveUserName(user.nombre, user.apellido)

                    val user = userRepository.login(email,password)

                    if(user != null){
                        preferencesManager.saveLoginState(true)
                        preferencesManager.saveUserName(user.nombre, user.apellido)

                        launch(Dispatchers.Main)  {
                            navController.navigate("home/${user.nombre}/${user.apellido}")
                        }

                    }else {
                        launch(Dispatchers.Main) {
                            loginError = true
                        }
                    }

                }
                /*navController.navigate("home/${user.nombre}/${user.apellido}")
            }else {
                loginError = true
            }*/
            },
            enabled = !emailError && !passwordError && email.isNotEmpty() && password.isNotEmpty()
        ){
            Text("Login")
        }

        if(loginError){
            Spacer(modifier = Modifier.height(8.dp))
            Text("Usuario o contraseña incorrecta", color = Color.Red, fontSize = 12.sp)
        }

    }

}
