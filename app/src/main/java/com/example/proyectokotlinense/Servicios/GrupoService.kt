package com.example.proyectokotlinense.Servicios

import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.example.proyectokotlinense.modelo.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import java.io.IOException

class GrupoService {
    private val url = "http://10.0.2.2:8080/grupo"

    fun crearGrupo(idUsuario: Int, nombre: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$url/nuevo/$idUsuario")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), "{\"nombre\":\"$nombre\"}"))
            .build()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        } catch (e: Exception) {
            throw Exception("Error al crear el grupo", e)
        }
    }
}