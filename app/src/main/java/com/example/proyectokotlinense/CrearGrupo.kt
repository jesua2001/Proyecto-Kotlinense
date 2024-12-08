package com.example.proyectokotlinense

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import androidx.lifecycle.lifecycleScope
import com.example.proyectokotlinense.Servicios.CuentaService
import com.example.proyectokotlinense.modelo.Cuenta
import com.example.proyectokotlinense.modelo.Usuario
import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearGrupo : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var imageView: ImageView
    private lateinit var editTextImageUrl: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonLoadImage: Button
    private lateinit var buttonSave: Button
    private lateinit var buttonParticipantes: Button
    private val cuentaService = CuentaService()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creacion_grupo)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val storedUserId = sharedPreferences.getInt("userId", -1)

        imageView = findViewById(R.id.imageView)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        buttonSave = findViewById(R.id.buttonSave)
        buttonParticipantes = findViewById(R.id.buttonAnyadirParticipante)
        buttonParticipantes.isEnabled = false

        buttonLoadImage.setOnClickListener {
            val imageUrl = editTextImageUrl.text.toString()
            val groupName = editTextName.text.toString()
            if (imageUrl.isNotEmpty() && groupName.isNotEmpty()) {
                Picasso.get().load(imageUrl).into(imageView)
            }
        }

        buttonSave.setOnClickListener {
            val imageUrl = editTextImageUrl.text.toString()
            val groupName = editTextName.text.toString()
            val groupDescription = editTextDescription.text.toString()

            if (groupName.isEmpty() || groupDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val grupo = Cuenta(0, groupName, groupDescription, imageUrl, "null", setOf(), 1.0f)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    cuentaService.crearCuenta(storedUserId, grupo)
                    runOnUiThread {
                        Toast.makeText(this@CrearGrupo, "Group saved successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@CrearGrupo, "Failed to save group: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            buttonParticipantes.isEnabled = true
        }

        buttonParticipantes.setOnClickListener {
            val intent = Intent(this, AnyadirParticipante::class.java)
            lifecycleScope.launch {
                val cuentas = cuentaService.getCuentas(storedUserId)
                val ultimaCuenta = cuentas.last()
                val cuentaId = ultimaCuenta.id
                intent.putExtra("CUENTA_ID", cuentaId )
                startActivity(intent)
            }

        }
    }
}