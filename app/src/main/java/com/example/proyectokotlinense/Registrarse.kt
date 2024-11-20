package com.example.proyectokotlinense

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Registrarse : AppCompatActivity() {

    private val url = "http://10.0.2.2:8080/api/auth/login";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
    }
}