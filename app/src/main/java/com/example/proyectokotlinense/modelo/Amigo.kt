package com.example.proyectokotlinense.modelo

data class Amigo(
    val id: Int,
    val user: Usuario,
    val confirmado: Boolean,
    val amigo: Usuario
)
