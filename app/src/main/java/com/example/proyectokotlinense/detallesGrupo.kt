package com.example.proyectokotlinense

import CuentaService
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class detallesGrupo : AppCompatActivity() {

    private lateinit var cuentaService: CuentaService
    private lateinit var contenedorGeneral: LinearLayout
    private var cuentaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_grupo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        cuentaId = intent.getIntExtra("CUENTA_ID", -1)
        cuentaService = CuentaService()
        contenedorGeneral = findViewById(R.id.contenedorGeneral)

        val imagenCuenta = findViewById<ImageView>(R.id.imageView3)
        val nombreCuenta = findViewById<TextView>(R.id.textView5)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

        lifecycleScope.launch {
            val cuenta = cuentaService.getCuenta(cuentaId)
            nombreCuenta.text = cuenta.nombre
            Glide.with(this@detallesGrupo)
                .load(cuenta.imagen)
                .into(imagenCuenta)

            mostrarGastos()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                lifecycleScope.launch {
                    when (tab.position) {
                        0 -> mostrarGastos()
                        1 -> mostrarDivision()
                        2 -> mostrarPersonas()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


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
    }

    private suspend fun mostrarGastos() {
        contenedorGeneral.removeAllViews()
        val gastos = cuentaService.getGastos(cuentaId)
        for (gasto in gastos) {
            val inflater = layoutInflater
            val tarjetaGasto = inflater.inflate(R.layout.tarjetagasto, contenedorGeneral, false) as CardView

            val nombreGasto = tarjetaGasto.findViewById<TextView>(R.id.title_text)
            val precioGasto = tarjetaGasto.findViewById<TextView>(R.id.precio_text)
            val fechaGasto = tarjetaGasto.findViewById<TextView>(R.id.Hora_text)
            val imagenGasto = tarjetaGasto.findViewById<ImageView>(R.id.image_profile)

            nombreGasto.text = gasto.nombre
            precioGasto.text = gasto.precio.toString()
            fechaGasto.text = gasto.fecha.toString()
            Glide.with(this@detallesGrupo).load(gasto.imagen).into(imagenGasto)

            contenedorGeneral.addView(tarjetaGasto)
        }
    }

    private suspend fun mostrarDivision() {
        contenedorGeneral.removeAllViews()
        val textView = TextView(this@detallesGrupo)
        textView.text = "División de gastos no implementada aún"
        contenedorGeneral.addView(textView)
    }

    private suspend fun mostrarPersonas() {
        contenedorGeneral.removeAllViews()
        val textView = TextView(this@detallesGrupo)
        textView.text = "Personas no implementado aún"
        contenedorGeneral.addView(textView)
    }
}
