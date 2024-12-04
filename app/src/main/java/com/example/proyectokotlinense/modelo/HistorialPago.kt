package com.example.proyectokotlinense.modelo

import com.example.proyectokotlinense.modelo.Enum.TipoPago

data class HistorialPago(
    val id:Int,
    val tipoPago:TipoPago,
    val monton: Double,
    val usuario : Usuario,
    val cuenta: Cuenta
)