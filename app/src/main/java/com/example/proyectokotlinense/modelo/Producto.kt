package com.example.proyectokotlinense.modelo

import java.time.LocalDateTime
import java.time.LocalTime

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Float,
    val imagen: String,
    val fecha: LocalDateTime,
    val factura: String,
    val user: Usuario,
    val cuenta: Cuenta
)
