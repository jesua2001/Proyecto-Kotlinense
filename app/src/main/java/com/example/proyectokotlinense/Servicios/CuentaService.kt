package com.example.proyectokotlinense.Servicios

import AmigosService
import com.example.proyectokotlinense.modelo.Cuenta
import com.example.proyectokotlinense.modelo.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CuentaService {

    private val URL: String = "http://10.0.2.2:8080/grupo"

    suspend fun getCuenta(idUsuario: Int, idCuenta: Int): Cuenta = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$URL/$idCuenta")
            .build()
        val cuenta:Cuenta;

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val cuentaJson = JSONObject(response.body!!.string())
            cuenta = recuperarCuenta(cuentaJson)
        } catch (e: Exception) {
            throw Exception("Error al recuperar la cuenta", e)
        }

        return@withContext cuenta
    }

    private fun recuperarCuenta(cuentaJson: JSONObject): Cuenta {
        val cuenta = Cuenta(
            cuentaJson.getInt("id"),
            cuentaJson.getString("nombre"),
            cuentaJson.getString("descripcion"),
            cuentaJson.getString("imagen"),
            cuentaJson.getString("imagenFondo"),
            recuperarUsuarios(cuentaJson.getJSONArray("participantes")),
            cuentaJson.getDouble("totalGastos").toFloat()
        )
        println("Cuenta recuperada: $cuenta")
        return cuenta
    }

    private fun recuperarUsuarios(texto:JSONArray): Set<Usuario> {
        return HashSet<Usuario>(AmigosService().recuperarAmigos(texto));
    }

    suspend fun getCuentas(idUsuario: Int): ArrayList<Cuenta> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$URL/$idUsuario")
            .build()
        val grupos = ArrayList<Cuenta>()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val gruposJson = JSONArray(response.body!!.string())
            grupos.addAll(recuperarCuetas(gruposJson))
        } catch (e: Exception) {
            throw Exception("Error al recuperar los grupos", e)
        }

        return@withContext grupos
    }

    private fun recuperarCuetas(texto: JSONArray): ArrayList<Cuenta> {
        val grupos = ArrayList<Cuenta>()

        for (i in 0 until texto.length()) {
            grupos.add(recuperarCuenta(texto[i] as JSONObject))
        }

        println("Grupos recuperados: $grupos")
        return grupos
    }
}