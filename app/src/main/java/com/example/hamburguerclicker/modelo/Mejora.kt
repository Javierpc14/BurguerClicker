package com.example.hamburguerclicker.modelo

class Mejora(
    var nombre:String,
    var precio :Double,
    var incremento:Double,
    var idImagen :Int,
    var obtenido:Boolean =false
) {
    constructor() : this("",  0.0,0.0,0,false)

}