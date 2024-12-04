package com.example.proyectokotlinense

import CuentaService
import android.os.Bundle
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
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class detallesGrupo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_grupo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cuentaId = intent.getIntExtra("CUENTA_ID", -1)

        val cuentaService = CuentaService()
        val imagenCuenta = findViewById<ImageView>(R.id.imageView3)
        val nombreCuenta = findViewById<TextView>(R.id.textView5)


        lifecycleScope.launch {

            val tabLayout = findViewById<TabLayout>(R.id.tabLayout)

            val tabItemCuenta = findViewById<TabItem>(R.id.cuenta)
            val tabItemDivision = findViewById<TabItem>(R.id.division)
            val tabItemPersonas = findViewById<TabItem>(R.id.personas)

            nombreCuenta.text = cuentaService.getCuenta(cuentaId).nombre
            Glide.with(this@detallesGrupo).load(cuentaService.getCuenta(cuentaId).imagen).into(imagenCuenta)

            val gastos = cuentaService.getGastos(cuentaId)

            val contenedorGeneral = findViewById<LinearLayout>(R.id.contenedorGeneral)

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
    }
}