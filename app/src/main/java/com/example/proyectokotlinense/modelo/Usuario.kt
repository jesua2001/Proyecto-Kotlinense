package com.example.proyectokotlinense.modelo


import com.example.proyectokotlinense.modelo.Enum.TipoPago
import org.divigroup.divigroup.model.enums.Rol

data class Usuario(
    val id: Int,
    val usuario: String,
    val email: String,
    val avatar: String,
    val pago : String,
    private val tipoPago: TipoPago,
    private val  rol: Rol,
    private val password: String,
    private val eliminado: Boolean




) {

}
