package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.proyectokotlinense.Servicios.CuentaService
import kotlinx.coroutines.launch

class Participantes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.participantes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cuentaService = CuentaService()
        lifecycleScope.launch {
            val participantes = cuentaService.getParticipantes(1)
            val contenedor = findViewById<LinearLayout>(R.id.linearLayoutParticipante)

            for (participante in participantes) {
                val inflador = LayoutInflater.from(this@Participantes)
                val tarjeta = inflador.inflate(R.layout.tarjeta_participante, contenedor, false) as CardView

                val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
                val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

                nombreUsuarioTextView.text = participante.usuario
                Glide.with(this@Participantes)
                    .load(participante.avatar)
                    .circleCrop()
                    .into(imagenUsuarioImageView)

                contenedor.addView(tarjeta)
            }
        }
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