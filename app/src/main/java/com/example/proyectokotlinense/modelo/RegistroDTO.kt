package com.example.proyectokotlinense.modelo;

import com.example.proyectokotlinense.modelo.Enum.TipoPago

data class RegistroDTO(
    val usuario: String,
    val contraseña: String,
    val correo: String,
    val avatar: String,
    val tipoPago: TipoPago
)