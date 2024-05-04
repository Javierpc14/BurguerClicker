package com.example.hamburguerclicker.modelo

class Logro(
    var imagenId: Int,
    var requisito: String,
    var descripcion: String,
    var conseguido: Boolean = false
){
    constructor() : this(0, "", "", false)
}