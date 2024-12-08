package com.example.proyectokotlinense

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectokotlinense.Servicios.CuentaService

import com.example.proyectokotlinense.modelo.Producto
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CrearProducto : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    private var cuentaId: Int = -1

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

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val storedUserId = sharedPreferences.getInt("userId", -1)
        imageView = findViewById(R.id.imageView)
        editTextImageUrl = findViewById(R.id.editTextImageUrl)
        editTextName = findViewById(R.id.editTextName)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextDescription = findViewById(R.id.editTextDescription)
        buttonLoadImage = findViewById(R.id.buttonLoadImage)
        buttonSave = findViewById(R.id.buttonSave)
        cuentaId = intent.getIntExtra("CUENTA_ID", -1)

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
                cuentaService.agregarGasto(storedUserId,cuentaId, producto)
            }
        }
    }
}