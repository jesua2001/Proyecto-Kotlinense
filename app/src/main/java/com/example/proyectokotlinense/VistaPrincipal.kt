package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class VistaPrincipal :AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        val botonaccederperfil = findViewById<Button>(R.id.botonperfil)
        botonaccederperfil.setOnClickListener {
            val intent = Intent(this, VistaPerfil::class.java)
            startActivity(intent)
        }

    }
}