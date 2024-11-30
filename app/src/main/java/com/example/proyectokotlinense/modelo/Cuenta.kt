package com.example.proyectokotlinense.modelo

data class Cuenta(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val imagen: String,
    val imagenFondo: String,
    val participantes: Set<Usuario>,
    val totalGastos: Float
)
