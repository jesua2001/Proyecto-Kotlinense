package com.example.proyectokotlinense

import UsuarioService
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VistaPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vista_perfil)
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val storedUserId = sharedPreferences.getInt("userId", -1)
        println("Stored user id: $storedUserId")
        println(storedUserId)
        val usuario: EditText = findViewById(R.id.usuario)
        val email: EditText = findViewById(R.id.email)
        val tipoPago: EditText = findViewById(R.id.tipodepago)
        val rolusuario: EditText = findViewById(R.id.rolusuario)
        val imagen: ImageView = findViewById(R.id.imagenperfil)

        usuario.isFocusable = false
        usuario.isFocusableInTouchMode = false
        usuario.isClickable = false

        email.isFocusable = false
        email.isFocusableInTouchMode = false
        email.isClickable = false

        tipoPago.isFocusable = false
        tipoPago.isFocusableInTouchMode = false
        tipoPago.isClickable = false

        rolusuario.isFocusable = false
        rolusuario.isFocusableInTouchMode = false
        rolusuario.isClickable = false

        val usuarioService = UsuarioService()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (!Grupos::class.java.isAssignableFrom(this::class.java)) {
                        val intent = Intent(this, Grupos::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.profile -> {
                    if (!VistaPerfil::class.java.isAssignableFrom(this::class.java)) {
                        val intent = Intent(this, VistaPerfil::class.java)
                        startActivity(intent)
                    }
                    true
                }
                R.id.search -> {
                    if (!Amigos::class.java.isAssignableFrom(this::class.java)) {
                        val intent = Intent(this, Amigos::class.java)
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }

        lifecycleScope.launch {
            try {
                val user = usuarioService.getUsuario(storedUserId)
                withContext(Dispatchers.Main) {
                    usuario.setText(user.usuario)
                    email.setText(user.email)
                    tipoPago.setText(user.tipoPago.name)
                    rolusuario.setText(user.rol.name)
                    Glide.with(this@VistaPerfil)
                        .load(user.avatar)
                        .override(250, 250)
                        .circleCrop()
                        .into(imagen)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@VistaPerfil, "Error loading user data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}