package com.example.proyectokotlinense.modelo

data class Cuenta(
    var id: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val imagenFondo: String,
    var participantes: Set<Usuario>,
    val saldo: Float
)
