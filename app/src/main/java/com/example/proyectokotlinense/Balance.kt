package com.example.proyectokotlinense

import CuentaService
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

class Balance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.balances)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cuentaService = CuentaService()

        lifecycleScope.launch {
            val participantes = cuentaService.getParticipantes(1)

            val contenedor = findViewById<LinearLayout>(R.id.linearLayoutBalance)

            for (participante in participantes) {
                val balances = cuentaService.getBalances(participante.id)

                for (balance in balances) {
                    val inflador = LayoutInflater.from(this@Balance)
                    val tarjeta = inflador.inflate(R.layout.tarjeta_balance, contenedor, false) as CardView

                    val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
                    val balanceTextView = tarjeta.findViewById<TextView>(R.id.debtLabel)
                    val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

                    nombreUsuarioTextView.text = balance.first
                    balanceTextView.text = "Debe ${balance.second}€"
                    Glide.with(this@Balance)
                        .load(participante.avatar)
                        .circleCrop()
                        .into(imagenUsuarioImageView)

                    contenedor.addView(tarjeta)
                }
            }
        }
    }
}