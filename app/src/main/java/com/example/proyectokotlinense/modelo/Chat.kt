package com.example.proyectokotlinense.modelo

import java.time.LocalDateTime

data class Chat(
    val id: Int,
    val hora: LocalDateTime,
    val mensaje: String,
    val emisor: Usuario,
    val receptor: Usuario
)
