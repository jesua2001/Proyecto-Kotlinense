package com.example.proyectokotlinense

import CuentaService
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
import kotlinx.coroutines.launch

// val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
// val storedUserId = sharedPreferences.getInt("userId", -1)

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

        val userId = intent.getIntExtra("USER_ID", -1)

        val cuentaService = CuentaService()

        lifecycleScope.launch {

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val storedUserId = sharedPreferences.getInt("userId", -1)
            println("Stored user id: $storedUserId")

            val cuentas = cuentaService.getCuentas(userId)

            val container = findViewById<LinearLayout>(R.id.linearLayoutContainer)

            for (cuenta in cuentas) {
                val inflater = LayoutInflater.from(this@Grupos)
                val cardView = inflater.inflate(R.layout.tarjeta_grupo, container, false) as CardView

                val titleTextView = cardView.findViewById<TextView>(R.id.title_text)
                val precioTextView = cardView.findViewById<TextView>(R.id.precio_text)
                val imagenUsuario = cardView.findViewById<ImageView>(R.id.image_profile)
                val container2 = cardView.findViewById<LinearLayout>(R.id.linearLayout2jiji)

                titleTextView.text = cuenta.nombre
                precioTextView.text = cuenta.saldo.toString()
                Glide.with(this@Grupos)
                    .load(cuenta.imagen)
                    .circleCrop()
                    .into(imagenUsuario)

                container.addView(cardView)

                for (participante in cuenta.participantes) {
                    val inflater2 = LayoutInflater.from(this@Grupos)
                    val linear = inflater2.inflate(R.layout.imagenusuarios, container2, false) as ImageView

                    Glide.with(this@Grupos)
                        .load(participante.avatar)
                        .circleCrop()
                        .into(linear)

                    container2.addView(linear)
                }
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