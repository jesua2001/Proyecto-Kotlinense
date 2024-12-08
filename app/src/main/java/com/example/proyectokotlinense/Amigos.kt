package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.proyectokotlinense.Servicios.AmigosService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class Amigos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.amigos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val amigosService = AmigosService()

        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val storedUserId = sharedPreferences.getInt("userId", -1)

            if (storedUserId == -1) {
                Toast.makeText(this@Amigos, "User ID not found", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val amigos = amigosService.getAmigos(storedUserId)

            val contenedor = findViewById<LinearLayout>(R.id.linearLayoutAmigo)

            for (amigo in amigos) {
                val inflador = LayoutInflater.from(this@Amigos)
                val tarjeta = inflador.inflate(R.layout.tarjeta_amigo, contenedor, false) as CardView

                val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
                val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

                nombreUsuarioTextView.text = amigo.usuario
                Glide.with(this@Amigos)
                    .load(amigo.avatar)
                    .circleCrop()
                    .into(imagenUsuarioImageView)

                contenedor.addView(tarjeta)
            }
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (!this::class.java.equals(Grupos::class.java)) {
                        val intent = Intent(this, Grupos::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.profile -> {
                    if (!this::class.java.equals(VistaPerfil::class.java)) {
                        val intent = Intent(this, VistaPerfil::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.search -> {
                    if (!this::class.java.equals(Amigos::class.java)) {
                        val intent = Intent(this, Amigos::class.java)
                        startActivity(intent)
                    }
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.profile -> {
                val intent = Intent(this, VistaPerfil::class.java)
                startActivity(intent)
                true
            }
            R.id.home -> {
                val intent = Intent(this, Grupos::class.java)
                startActivity(intent)
                true
            }
            R.id.search -> {
                val intent = Intent(this, Amigos::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}