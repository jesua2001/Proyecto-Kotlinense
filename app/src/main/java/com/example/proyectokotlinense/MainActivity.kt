package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val url = "http://10.0.2.2:8080/api/auth/login"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var varusuario by mutableStateOf("")
        var varcontaseña by mutableStateOf("")

        findViewById<ComposeView>(R.id.botoniniciarsesion).setContent {
            VistadelLogin(varusuario, varcontaseña, { varusuario = it }, { varcontaseña = it }) {
                login(varusuario, varcontaseña)
            }
        }
    }

    private fun login(username: String, password: String) {
        val json = """
            {
                "username": "$username",
                "password": "$password"
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(url).post(requestBody).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val Intent = Intent(this@MainActivity, VistaPrincipal::class.java)
                        startActivity(Intent)
                        Toast.makeText(this@MainActivity, "EXITO", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "ERROR: ${response.code} ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}


@Composable
private fun VistadelLogin(
    usuario: String,
    contaseña: String,
    usuarioteclado: (String) -> Unit,// Funcion para el teclado es decir el cambio de texto en el textfield de usuario
    //Unit es una lamdad que hace referencia a una funcion que no retorna nada es decir no lleva ningun return
    contraseñateclado: (String) -> Unit, // Funcion para el teclado es decir el cambio de texto en el textfield de contraseña
    //Unit es una lamdad que hace referencia a una funcion que no retorna nada es decir no lleva ningun return
    funcionIniciarSesion: () -> Unit //funcion para el boton de iniciar sesion
    //Unit es una lamdad que hace referencia a una funcion que no retorna nada es decir no lleva ningun return
) {
    val context = LocalContext.current
    //Alinear los textos en el centro
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = usuario,
            onValueChange = usuarioteclado,
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = contaseña,
            onValueChange = contraseñateclado,
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = funcionIniciarSesion,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Iniciar Sesión", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val intent = Intent(context, Registrarse::class.java)
            context.startActivity(intent)
        }, modifier = Modifier.fillMaxWidth() , colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
            Text(text = "Registrarse", color = Color.White)
        }
    }
}