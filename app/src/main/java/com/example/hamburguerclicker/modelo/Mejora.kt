package com.example.hamburguerclicker.modelo

class Mejora(
    var nombre:String,
    var precio :Double,
    var incremento:Double,
    var imagenId :Int,
    var descripcion : String,
    var tiendaId :Int,
    var requisitoId :Int =-1,
    var obtenida:Boolean =false
) {
    constructor() : this("",  0.0,0.0,0, "", 0,-1,false)

}