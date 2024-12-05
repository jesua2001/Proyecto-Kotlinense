package com.example.proyectokotlinense

import CuentaService
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
import com.google.android.material.bottomnavigation.BottomNavigationView

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
            val balances = cuentaService.getBalances(1)
            val cuenta = cuentaService.getCuenta(1)
            val contenedor = findViewById<LinearLayout>(R.id.linearLayoutBalance)

            var totalBalance = 0f

            for (balance in balances) {
                totalBalance += balance.second
            }

            val balancePorPersona =
                if (participantes.isNotEmpty()) cuenta.saldo / participantes.size else 0f

            val totalTextView = findViewById<TextView>(R.id.textViewTotal)
            val personaTextView = findViewById<TextView>(R.id.textViewPersona)

            totalTextView.text = "Total: ${cuenta.saldo}€"
            personaTextView.text = "Por persona: ${balancePorPersona}€"

            for (participante in participantes) {
                for (balance in balances) {
                    if (balance.first != participante.usuario) continue
                    val inflador = LayoutInflater.from(this@Balance)
                    val tarjeta =
                        inflador.inflate(R.layout.tarjeta_balance, contenedor, false) as CardView

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

    //todo: implementar el crear gasto:

}