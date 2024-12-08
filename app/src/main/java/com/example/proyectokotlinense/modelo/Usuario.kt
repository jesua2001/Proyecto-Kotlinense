package com.example.proyectokotlinense.modelo

import com.example.proyectokotlinense.modelo.Enum.Rol
import com.example.proyectokotlinense.modelo.Enum.TipoPago

data class Usuario(
    val id: Int,
    var usuario: String,
    val email: String,
    val avatar: String,
    val pago : String,
    val tipoPago: TipoPago,
    val  rol:Rol
) {

}