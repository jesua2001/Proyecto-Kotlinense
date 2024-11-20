package com.example.proyectokotlinense

import android.os.Bundle
import android.telecom.Call
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


//Revisar las versiones de android
class MainActivity : AppCompatActivity() {

    val url = "http://localhost:8080/usuario";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //JetPassComposse enlazado
        val vistadesdeelcomposse = findViewById<ComposeView>(R.id.holadesdelavistacomposse)
        vistadesdeelcomposse.setContent {
            HolaconJetPackCompose()
        }


    }
fun login(username: String, password: String) {
    val url = "http://10.0.2.2:8080/api/auth/login"
    val json = """
        {
            "username": "$username",
            "password": "$password"
        }
    """.trimIndent()

    val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaType())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()
    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            runOnUiThread {
                Toast.makeText(this@MainActivity, "ERROR: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onResponse(call: okhttp3.Call, response: Response) {
            runOnUiThread {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "EXITO", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "ERROR: ${response.code} ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    })
}
    @Preview (showSystemUi = true)
    @Composable
    private fun HolaconJetPackCompose() {
        Text(text ="Hola con JetPack Compose" , modifier =  Modifier.wrapContentWidth(Alignment.CenterHorizontally))
        Button(onClick={login("1","1")}) {}




    }



}
