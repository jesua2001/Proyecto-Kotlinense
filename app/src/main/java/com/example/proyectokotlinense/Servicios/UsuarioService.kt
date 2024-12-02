package com.example.proyectokotlinense.Servicios

import AmigosService
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.proyectokotlinense.modelo.Cuenta
import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.example.proyectokotlinense.modelo.Producto
import com.example.proyectokotlinense.modelo.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.time.LocalDateTime


class UsuarioService {
    private val url = "http://10.0.2.2:8080/usuario"

    /**
     * Obtiene un usuario de la base de datos
     * @param idUsuario Identificador del usuario
     * @return Usuario
     * @throws Exception Error al recuperar el usuario
     */
    suspend fun getUsuario(idUsuario: Int): Usuario = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$url/id/$idUsuario")
            .build()
        val usuario: Usuario

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val usuarioJson = JSONObject(response.body!!.string())
            usuario = recuperarUsuario(usuarioJson)
        } catch (e: Exception) {
            throw Exception("Error al recuperar el usuario", e)
        }

        return@withContext usuario
    }

    /**
     * Crea un usuario en la base de datos
     * @param usuario Usuario a crear
     * @return Usuario creado
     * @throws Exception Error al crear el usuario
     */
    suspend fun postUsuario(usuario: Usuario): Usuario = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val json = """
            {
                "id": ${usuario.id},
                "username": "${usuario.usuario}",
                "email": "${usuario.email}",
                "avatar": "${usuario.avatar}",
                "tipoPago": "${usuario.tipoPago}"
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$url/id")
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val usuarioJson = JSONObject(response.body!!.string())
            return@withContext recuperarUsuario(usuarioJson)
        } catch (e: Exception) {
            throw Exception("Error al crear el usuario", e)
        }
    }

    private fun recuperarUsuario(texto: JSONObject): Usuario {
        val usuario = Usuario(
            texto.getInt("id"),
            texto.getString("username"),
            texto.getString("email"),
            texto.getString("avatar"),
            "",
            TipoPago.valueOf(texto.getString("tipoPago")),
            Rol.valueOf(texto.getString("rol"))
        )

        return usuario
    }
}