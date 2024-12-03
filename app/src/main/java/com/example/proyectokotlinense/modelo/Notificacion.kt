package com.example.proyectokotlinense.modelo

import java.time.LocalDate

data class Notificacion(
    val id:Int,
    val fecha:LocalDate,
    val mensaje:String,
    val visto : Boolean,
    val usuario:Usuario

)
