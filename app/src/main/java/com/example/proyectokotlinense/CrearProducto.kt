package com.example.proyectokotlinense

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectokotlinense.Servicios.CuentaService
import com.example.proyectokotlinense.modelo.Cuenta
import com.example.proyectokotlinense.modelo.Producto
import com.example.proyectokotlinense.modelo.Usuario
import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CrearProducto : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var editTextImageUrl: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonLoadImage: Button
    private lateinit var buttonSave: Button
    private val cuentaService = CuentaService()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.vista_producto)

        imageView = findViewById(R.id.imageView)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        editTextName = findViewById(R.id.editTextName)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        buttonSave = findViewById(R.id.buttonSave)

        buttonLoadImage.setOnClickListener {
            val imageUrl = editTextImageUrl.text.toString()
            val productName = editTextName.text.toString()
            if (imageUrl.isNotEmpty() && productName.isNotEmpty()) {
                Picasso.get().load(imageUrl).into(imageView)
            }
        }

        buttonSave.setOnClickListener {
            val imageUrl = editTextImageUrl.text.toString()
            val productName = editTextName.text.toString()
            val productPrice = editTextPrice.text.toString()
            val productDescription = editTextDescription.text.toString()
            val producto = Producto(0, productName, productDescription,
                productPrice.toDouble().toFloat(), imageUrl, null, null, null)

            CoroutineScope(Dispatchers.IO).launch {
                cuentaService.agregarGasto(1,1, producto)
            }
        }
    }
}