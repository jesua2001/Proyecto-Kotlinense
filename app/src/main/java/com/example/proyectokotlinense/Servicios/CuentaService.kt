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

class CuentaService {

    private val URL: String = "http://10.0.2.2:8080/grupo"

    suspend fun eliminarParticipante(idUsuario: Int, cuenta: Cuenta, idParticipante: Int): Cuenta = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val mensaje = """
            {
                "idUsuario": $idUsuario,
                "idGrupo": ${cuenta.id},
                "idParticipante": $idParticipante
            }
        """.trimIndent()
        val request = Request.Builder()
            .url("$URL/participantes/eliminar")
            .delete(mensaje.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()
        val cuentaNueva:Cuenta = cuenta;

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val texto = response.body!!.string()
            val cuentaJson: JSONObject
            try {
                cuentaJson = JSONObject(texto)

            } catch (e: Exception) {
                throw Exception("Error la cuenta esta vacia", e)
            }

            cuentaJson.getJSONArray("participantes")
            cuentaNueva.participantes = recuperarUsuarios(cuentaJson.getJSONArray("participantes"))
        } catch (e: Exception) {
            throw Exception("Error al eliminar el usuario", e)
        }

        return@withContext cuentaNueva
    }

    suspend fun agregarParticipante(idUsuario: Int, cuenta: Cuenta, idParticipante: Int): Cuenta = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val mensaje = """
            {
                "idUsuario": $idUsuario,
                "idGrupo": ${cuenta.id},
                "idParticipante": $idParticipante
            }
        """.trimIndent()
        val request = Request.Builder()
            .url("$URL/participantes/nuevo")
            .post(mensaje.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()
        val cuentaNueva:Cuenta = cuenta;

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val texto = response.body!!.string()
            val cuentaJson: JSONObject
            try {
                cuentaJson = JSONObject(texto)

            } catch (e: Exception) {
                throw Exception("Error la cuenta esta vacia", e)
            }

            cuentaJson.getJSONArray("participantes")
            cuentaNueva.participantes = recuperarUsuarios(cuentaJson.getJSONArray("participantes"))
        } catch (e: Exception) {
            throw Exception("Error al añadir al usuario", e)
        }

        return@withContext cuentaNueva
    }

    suspend fun crearCuenta(idUsuario: Int, cuenta: Cuenta): Cuenta = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val json = """
            {
                "nombre": "${cuenta.nombre}",
                "descripcion": "${cuenta.descripcion}",
                "imagen": "${cuenta.imagen}",
                "imagenFondo": "${cuenta.imagenFondo}",
                "personas": ${JSONArray(cuenta.participantes)},
                "saldo": ${cuenta.saldo}
            }
        """.trimIndent()
        println("JSON: $json")
        val cuentaNueva:Cuenta

        val request = Request.Builder()
            .url("$URL/nuevo/$idUsuario")
            .post(json.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()



        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            cuentaNueva = recuperarCuenta(JSONObject(response.body!!.string()))

        } catch (e: Exception) {
            throw Exception("Error al recuperar la cuenta", e)
        }

        return@withContext cuentaNueva
    }

    suspend fun getCuenta(idUsuario: Int, idCuenta: Int): Cuenta = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$URL/cuenta/$idCuenta")
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


    suspend fun agregarGasto(idUsuario: Int, cuenta: Cuenta, gasto: Producto): Producto = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val mensaje = """
            {
                "idUsuario": $idUsuario,
                "idGrupo": ${cuenta.id},
                "producto": {
                    "nombre": "${gasto.nombre}",
                    "descripcion": "${gasto.descripcion}",
                    "precio": ${gasto.precio},
                    "imagen": "${gasto.imagen}"
                }
            }
        """.trimIndent()
        val request = Request.Builder()
            .url("$URL/gasto/nuevo/$idUsuario")
            .post(mensaje.toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()
        val producto:Producto

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val texto = response.body!!.string()

            val gruposJson = JSONObject(texto)
            producto = recuperarProducto(gruposJson)

        } catch (e: Exception) {
            throw Exception("Error al recuperar los grupos", e)
        }

        return@withContext producto
    }




    //suspend fun agregarGasto(idUsuario: Int, cuenta: Cuenta, gasto: Producto, imagen: File?, factura: File?): Producto = withContext(Dispatchers.IO) {
//
    //    val producto: Producto
    //    val client = OkHttpClient()
    //    val mensaje = """
    //    {
    //        idUsuario : $idUsuario,
    //        idGrupo : ${cuenta.id},
    //        producto : {
    //            nombre : "${gasto.nombre}",
    //            descripcion : "${gasto.descripcion}",
    //            precio : ${gasto.precio},
    //            imagen : "${gasto.imagen}"
    //        }
    //    }
    //    """.trimIndent()
    //    println(JSONObject(mensaje).toString())
    //    // val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
    //    // requestBodyBuilder.addFormDataPart("idUsuario", idUsuario.toString())
    //    // requestBodyBuilder.addFormDataPart("idGrupo", cuenta.id.toString())
    //    // requestBodyBuilder.addFormDataPart("dto", null, mensaje.toRequestBody("application/json; charset=utf-8".toMediaType()))
//
    //    // Crear la parte del cuerpo para el JSON
    //    val jsonRequestBody = mensaje.toRequestBody("application/json; charset=utf-8".toMediaType())
//
    //    // Crear la parte del cuerpo para el archivo
    //    // val imageRequestBody = imagen.asRequestBody("image/jpeg".toMediaTypeOrNull())
//
    //    // Crear la parte del cuerpo para el archivo
    //    // val fileRequestBody = factura.asRequestBody("application/pdf".toMediaTypeOrNull())
//
//
    //    // Crear el multipart para enviar ambos datos
    //    val multipartBody = MultipartBody.Builder()
    //        .setType(MultipartBody.FORM)
    //        .addFormDataPart("dto", "adoptionRequest.json", jsonRequestBody)
    //        // .addFormDataPart("factura", factura.name, fileRequestBody)
    //        // .addFormDataPart("imagen", imagen.name, imageRequestBody)
    //        .build()
//
//
    //    // imagen.let {
    //    //     requestBodyBuilder.addFormDataPart("imagen", it.name, it.asRequestBody("image/*".toMediaTypeOrNull()))
    //    // }
////
    //    // factura.let {
    //    //     requestBodyBuilder.addFormDataPart("factura", it.name, it.asRequestBody("application/pdf".toMediaTypeOrNull()))
    //    // }
//
    //    // val requestBody = multipartBody.build()
//
    //    val request = Request.Builder()
    //        .url("$URL/gasto/nuevo/$idUsuario")
    //        .post(multipartBody)
    //        .build()
//
    //    val cuentaNueva: Cuenta = cuenta
//
    //    try {
    //        val response = client.newCall(request).execute()
//
    //        if (!response.isSuccessful) throw IOException("Unexpected code $response")
//
    //        val texto = response.body!!.string()
    //        val cuentaJson: JSONObject
    //        try {
    //            cuentaJson = JSONObject(texto)
    //            producto = recuperarProducto(cuentaJson.getJSONObject("producto"))
    //        } catch (e: Exception) {
    //            throw Exception("Error la cuenta esta vacia", e)
    //        }
//
    //        cuentaJson.getJSONArray("participantes")
    //        cuentaNueva.participantes = recuperarUsuarios(cuentaJson.getJSONArray("participantes"))
    //    } catch (e: Exception) {
    //        throw Exception("Error al agregar el gasto", e)
    //    }
//
    //    return@withContext producto
    //}


    private fun recuperarProducto(productoJson: JSONObject): Producto {
        val producto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var fatura: String? = null;
            try {
                fatura = productoJson.getString("factura")
            } catch (e: Exception) {
                fatura = null;
            }

            Producto(
                productoJson.getInt("id"),
                productoJson.getString("nombre"),
                productoJson.getString("descripcion"),
                productoJson.getDouble("precio").toFloat(),
                productoJson.getString("imagen"),
                LocalDateTime.parse(productoJson.getString("fecha")),
                fatura,
                Usuario(
                    productoJson.getJSONObject("usuario").getInt("id"),
                    productoJson.getJSONObject("usuario").getString("username"),
                    productoJson.getJSONObject("usuario").getString("email"),
                    productoJson.getJSONObject("usuario").getString("avatar"),
                    "",
                    TipoPago.valueOf(productoJson.getJSONObject("usuario").getString("tipoPago")),
                    Rol.valueOf(productoJson.getJSONObject("usuario").getString("rol"))
                )
            )
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        return producto
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
        return cuenta
    }

    private fun recuperarUsuarios(texto:JSONArray): Set<Usuario> {
        return HashSet<Usuario>(AmigosService().recuperarAmigos(texto));
    }

    private fun recuperarCuetas(texto: JSONArray): ArrayList<Cuenta> {
        val grupos = ArrayList<Cuenta>()

        for (i in 0 until texto.length()) {
            grupos.add(recuperarCuenta(texto[i] as JSONObject))
        }

        return grupos
    }
}