package com.example.proyectokotlinense

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val url = "http://guillemonas.synology.me:8081/usuario/login"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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
        val urlWithParams = "$url/$username/$password"
        val request = Request.Builder().url(urlWithParams).get().build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "El usuario o contraseña son incorrectos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val userId = responseBody?.toIntOrNull()
                    runOnUiThread {
                        if (userId != null) {
                            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putInt("userId", userId)
                            editor.apply()

                            val intent = Intent(this@MainActivity, Grupos::class.java).apply {
                                putExtra("USER_ID", userId)
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Error al obtener el ID de usuario", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "ERROR: ${response.code} ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
   @Composable
private fun VistadelLogin(
    usuario: String,
    contaseña: String,
    usuarioteclado: (String) -> Unit,
    contraseñateclado: (String) -> Unit,
    funcionIniciarSesion: () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = usuario,
            onValueChange = usuarioteclado,
            label = { Text("Usuario", color = textColor) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                cursorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = contaseña,
            onValueChange = contraseñateclado,
            label = { Text("Contraseña", color = textColor) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                cursorColor = textColor,
                focusedLabelColor = textColor,
                unfocusedLabelColor = textColor
            )
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
        Button(
            onClick = {
                val intent = Intent(context, Registrarse::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Registrarse", color = Color.White)
        }
    }
}
}