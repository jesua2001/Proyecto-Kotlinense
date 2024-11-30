import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago
import com.example.proyectokotlinense.modelo.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.io.IOException

class AmigosService {
    private val url = "http://10.0.2.2:8080/amigos"

    suspend fun getAmigos(idUsuario: Int): ArrayList<Usuario> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$url/$idUsuario")
            .build()
        val amigos = ArrayList<Usuario>()

        try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val amigosJson = JSONArray(response.body!!.string())
            amigos.addAll(recuperarAmigos(amigosJson))
        } catch (e: Exception) {
            throw Exception("Error al recuperar los amigos", e)
        }

        return@withContext amigos
    }

    private fun recuperarAmigos(texto: JSONArray): ArrayList<Usuario> {
        val amigos = ArrayList<Usuario>()

        for (i in 0 until texto.length()) {
            val amigoJson = texto.getJSONObject(i)
            val amigo = Usuario(
                amigoJson.getInt("id"),
                amigoJson.getString("username"),
                amigoJson.getString("email"),
                amigoJson.getString("avatar"),
                "",
                TipoPago.valueOf(amigoJson.getString("tipoPago")),
                Rol.valueOf(amigoJson.getString("rol"))
            )

            amigos.add(amigo)
        }
        println("Amigos recuperados: $amigos")
        return amigos
    }
}