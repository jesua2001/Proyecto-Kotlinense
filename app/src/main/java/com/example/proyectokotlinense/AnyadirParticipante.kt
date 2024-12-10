package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.proyectokotlinense.Servicios.AmigosService
import com.example.proyectokotlinense.Servicios.CuentaService
import com.example.proyectokotlinense.modelo.Amigo
import com.example.proyectokotlinense.modelo.Usuario
import kotlinx.coroutines.launch

class AnyadirParticipante : AppCompatActivity() {

    private var cuentaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_anyadir_participante)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cuentaId = intent.getIntExtra("CUENTA_ID", -1)

        lifecycleScope.launch {

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val storedUserId = sharedPreferences.getInt("userId", -1)

             val amigosService = AmigosService()
             val amigos = amigosService.getAmigos(storedUserId)
             val cuentaService = CuentaService()

            val contenedor = findViewById<LinearLayout>(R.id.linearLayoutContainer)
            val botonAnydir = findViewById<Button>(R.id.btnAdd)

            val selectedAmigos = mutableListOf<Usuario>()

            for (amigo in amigos) {
                val inflador = LayoutInflater.from(this@AnyadirParticipante)
                val tarjeta = inflador.inflate(R.layout.tarjeta_amigo, contenedor, false) as CardView

                val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
                val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

                nombreUsuarioTextView.text = amigo.usuario
                Glide.with(this@AnyadirParticipante)
                    .load(amigo.avatar)
                    .circleCrop()
                    .into(imagenUsuarioImageView)

                tarjeta.setOnClickListener {
                    if (selectedAmigos.contains(amigo)) {
                        selectedAmigos.remove(amigo)
                        Toast.makeText(this@AnyadirParticipante, "${amigo.usuario} eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        nombreUsuarioTextView.setTextColor(resources.getColor(R.color.red))
                        selectedAmigos.add(amigo)
                        Toast.makeText(this@AnyadirParticipante, "${amigo.usuario} añadido", Toast.LENGTH_SHORT).show()
                    }
                }

                contenedor.addView(tarjeta)
            }

            botonAnydir.setOnClickListener {
                for (amigo in selectedAmigos) {
                    lifecycleScope.launch {
                    cuentaService.agregarParticipante(storedUserId,cuentaId, amigo.id)
                    }
                    val intent = Intent(this@AnyadirParticipante, Grupos::class.java)
                    startActivity(intent)

                }
            }
        }
    }


}