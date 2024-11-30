package com.example.proyectokotlinense.Servicios

import com.example.proyectokotlinense.modelo.Cuenta
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

class CuentaService {

    private val URL: String = "http://10.0.2.2:8080/grupo"

    suspend fun getGrupos(idUsuario: Int): ArrayList<Cuenta> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$URL/$idUsuario")
            .build()
        val grupos = ArrayList<Cuenta>()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val gruposJson = JSONArray(response.body!!.string())
            grupos.addAll(recuperarGrupos(gruposJson))
        } catch (e: Exception) {
            throw Exception("Error al recuperar los grupos", e)
        }

        return@withContext grupos
    }

    private fun recuperarGrupos(texto: JSONArray): ArrayList<Cuenta> {
        val grupos = ArrayList<Cuenta>()

        for (i in 0 until texto.length()) {
            val grupoJson = texto.getJSONObject(i)
            val grupo = Cuenta(
                grupoJson.getInt("id"),
                grupoJson.getString("nombre"),
                grupoJson.getString("descripcion"),
                grupoJson.getString("imagen"),
                grupoJson.getString("imagenFondo")
            )

            grupos.add(grupo)
        }
        println("Grupos recuperados: $grupos")
        return grupos
    }
}