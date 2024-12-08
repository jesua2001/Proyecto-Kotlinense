package com.example.proyectokotlinense

import CuentaService
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        val botonAnyadir = findViewById<Button>(R.id.buttonAnyadir)
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

        botonAnyadir.setOnClickListener {
            val intent = Intent(this, CrearProducto::class.java)
            intent.putExtra("CUENTA_ID", cuentaId)
            startActivity(intent)
        }


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
        val por_persona = findViewById<TextView>(R.id.textViewPersona)
        val totalOcultar = findViewById<TextView>(R.id.textViewTotal)
        val botonAnyaadir = findViewById<Button>(R.id.buttonAnyadir)

        por_persona.visibility = View.GONE
        totalOcultar.visibility = View.GONE
        botonAnyaadir.visibility = View.VISIBLE


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

        val por_persona = findViewById<TextView>(R.id.textViewPersona)
        val totalOcultar = findViewById<TextView>(R.id.textViewTotal)
        val botonAnyaadir = findViewById<Button>(R.id.buttonAnyadir)

        botonAnyaadir.visibility = View.GONE


        por_persona.visibility = View.VISIBLE
        totalOcultar.visibility = View.VISIBLE

        val participantes = cuentaService.getParticipantes(cuentaId)
        val balances = cuentaService.getBalances(cuentaId)
        val cuenta = cuentaService.getCuenta(cuentaId)
        val contenedor = findViewById<LinearLayout>(R.id.contenedorGeneral)

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
                val inflador = LayoutInflater.from(this@detallesGrupo)
                val tarjeta =
                    inflador.inflate(R.layout.tarjeta_balance, contenedor, false) as CardView

                val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
                val balanceTextView = tarjeta.findViewById<TextView>(R.id.debtLabel)
                val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

                nombreUsuarioTextView.text = balance.first
                balanceTextView.text = "Debe ${balance.second}€"
                Glide.with(this@detallesGrupo)
                    .load(participante.avatar)
                    .circleCrop()
                    .into(imagenUsuarioImageView)

                contenedor.addView(tarjeta)
            }
        }
    }

    private suspend fun mostrarPersonas() {
        contenedorGeneral.removeAllViews()

        val por_persona = findViewById<TextView>(R.id.textViewPersona)
        val totalOcultar = findViewById<TextView>(R.id.textViewTotal)
        val botonAnyaadir = findViewById<Button>(R.id.buttonAnyadir)

        botonAnyaadir.visibility = View.GONE

        por_persona.visibility = View.GONE
        totalOcultar.visibility = View.GONE

        val participantes = cuentaService.getParticipantes(cuentaId)
        val contenedor = findViewById<LinearLayout>(R.id.contenedorGeneral)

        for (participante in participantes) {
            val inflador = LayoutInflater.from(this@detallesGrupo)
            val tarjeta = inflador.inflate(R.layout.tarjeta_participante, contenedor, false) as CardView

            val nombreUsuarioTextView = tarjeta.findViewById<TextView>(R.id.card_text)
            val imagenUsuarioImageView = tarjeta.findViewById<ImageView>(R.id.card_image)

            nombreUsuarioTextView.text = participante.usuario
            Glide.with(this@detallesGrupo)
                .load(participante.avatar)
                .circleCrop()
                .into(imagenUsuarioImageView)

            contenedor.addView(tarjeta)
        }
    }
}
