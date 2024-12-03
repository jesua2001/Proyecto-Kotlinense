package com.example.proyectokotlinense

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.proyectokotlinense.Servicios.CuentaService
import kotlinx.coroutines.launch

class Grupos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_grupos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cuentaService = CuentaService()

        lifecycleScope.launch {
            val cuentas = cuentaService.getCuentas(1)

            val container = findViewById<ScrollView>(R.id.linearLayoutContainer)

            for (cuenta in cuentas) {
                val inflater = LayoutInflater.from(this@Grupos)
                val cardView =
                    inflater.inflate(R.layout.tarjeta_grupo, container, false) as RelativeLayout

                val titleTextView = cardView.findViewById<TextView>(R.id.title_text)
                val precioTextView = cardView.findViewById<TextView>(R.id.precio_text)
                val imagenUsuario = cardView.findViewById<ImageView>(R.id.image_profile)

                titleTextView.text = cuenta.nombre
                precioTextView.text = cuenta.saldo.toString()
                Glide.with(this@Grupos)
                    .load(cuenta.imagen)
                    .circleCrop()
                    .into(imagenUsuario)

                container.addView(cardView)
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
            else -> super.onOptionsItemSelected(item)
        }

    }
}