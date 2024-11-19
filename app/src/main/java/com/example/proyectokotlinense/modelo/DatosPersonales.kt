package com.example.proyectokotlinense.modelo

import java.time.LocalDate

data class DatosPersonales(
    val id: Int,
    val usuario: Usuario,
    val nombre: String,
    val primerapellido: String,
    val segundoapellido: String,
    val fechaNacimiento:LocalDate,
    val telefono: String,
    val direccion : String,

)
