package com.example.proyectokotlinense

import AmigosService
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
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
            val amigos = amigosService.getAmigos(1)

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
    }
}