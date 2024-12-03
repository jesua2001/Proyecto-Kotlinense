package com.example.proyectokotlinense.modelo

data class HistorialPago(
    val id:Int,
    val tipoPago:TipoPago,
    val monton: Double,
    val usuario : Usuario,
    val cuenta: Cuenta
)
