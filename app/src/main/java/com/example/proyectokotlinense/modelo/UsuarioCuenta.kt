package com.example.proyectokotlinense.modelo

data class UsuarioCuenta(
    val id: Int,
    val usuario: Usuario,
    val Cuenta: Cuenta,
    val isAdmin: Boolean
)
