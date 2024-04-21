package com.example.hamburguerclicker.modelo

import android.widget.ImageView

class Logro (
    val nombre: String,
    var imagenId: Int,
    var requisito:String,
    var descripcion:String,
    var conseguido: Boolean =false
){
    constructor() : this("", 0,"","", false)
}