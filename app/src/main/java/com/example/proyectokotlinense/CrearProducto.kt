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
import UsuarioService
import com.example.proyectokotlinense.modelo.Producto
import com.example.proyectokotlinense.modelo.Usuario
import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CrearProducto : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var editTextImageUrl: EditText
    private lateinit var editTextName: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextDescription: EditText
    private lateinit var buttonLoadImage: Button
    private lateinit var buttonSave: Button
    private val cuentaService = CuentaService()
    private val usuarioService = UsuarioService()

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

            if (productName.isEmpty() || productPrice.isEmpty() || productDescription.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val price: Float
            try {
                price = productPrice.toFloat()
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val storedUserId = sharedPreferences.getInt("userId", -1)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val user = usuarioService.getUsuario(storedUserId)
                    if (user == null) {
                        runOnUiThread {
                            Toast.makeText(this@CrearProducto, "User not found", Toast.LENGTH_SHORT).show()
                        }
                        return@launch
                    }

                    val producto = Producto(0, productName, productDescription, price, imageUrl, null, null, user)

                    cuentaService.agregarGasto(storedUserId, 1, producto)
                    runOnUiThread {
                        Toast.makeText(this@CrearProducto, "Product saved successfully", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    println("Failed to save product: ${e.message}")
                }
            }
        }
    }
}
