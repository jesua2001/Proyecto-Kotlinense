package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class Registrarse : AppCompatActivity() {

    private val url = "http://guillemonas.synology.me:8081/usuario/registrar"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)

        findViewById<ComposeView>(R.id.registrarusuario).setContent {
            registrarse()
        }
    }

    private fun anadirusuario(username: String, password: String, email: String, avatar: String, tipoPago: TipoPago) {
        val json = """
        {
            "username": "$username",
            "password": "$password",
            "email": "$email",
            "avatar": "$avatar",
            "tipoPago": "$tipoPago"
        }
        """.trimIndent()
        val client = OkHttpClient()
        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@Registrarse, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@Registrarse, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Registrarse, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@Registrarse, "Error al registrar usuario: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

   @Composable
private fun registrarse() {
    var avatar by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var tipoPago by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = avatar,
            onValueChange = { avatar = it },
            label = { Text("Avatar URL", color = textColor) },
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
            value = username,
            onValueChange = { username = it },
            label = { Text("Username", color = textColor) },
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
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = textColor) },
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
            value = tipoPago,
            onValueChange = { tipoPago = it },
            label = { Text("Tipo de Pago", color = textColor) },
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password", color = textColor) },
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
            onClick = {
                val tipoPagoEnum = try {
                    TipoPago.valueOf(tipoPago)
                } catch (e: IllegalArgumentException) {
                    null
                }

                if (tipoPagoEnum != null) {
                    anadirusuario(username, password, email, avatar, tipoPagoEnum)
                } else {
                    Toast.makeText(this@Registrarse, "Tipo de pago no válido", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)
        ) {
            Text(text = "Registrar", color = Color.White)
        }
    }
}
}