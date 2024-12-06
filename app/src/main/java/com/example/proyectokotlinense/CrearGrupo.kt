package com.example.proyectokotlinense

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import CuentaService
import com.example.proyectokotlinense.modelo.Cuenta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearGrupo : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var editTextImageUrl: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonLoadImage: Button
    private lateinit var buttonSave: Button
    private val cuentaService = CuentaService()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creacion_grupo)

        imageView = findViewById(R.id.imageView)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        editTextName = findViewById(R.id.editTextName)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        buttonSave = findViewById(R.id.buttonSave)

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
                Toast.makeText(this, "Todos los campos deben ser rellenados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val grupo = Cuenta(0, groupName, groupDescription, imageUrl, "null", setOf(), 1.0f)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    cuentaService.crearCuenta(1, grupo)
                    runOnUiThread {
                        Toast.makeText(this@CrearGrupo, "Grupo creado correactamente", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    println("No se pudo guardar el grupo: ${e.message}")

                }
            }
        }
    }
}