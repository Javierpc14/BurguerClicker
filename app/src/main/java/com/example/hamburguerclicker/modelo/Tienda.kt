package com.example.hamburguerclicker.modelo

class Tienda(
    val nombre:String,
    var precioCompra:Double,
    var aportePasivo:Double,
    var total:Int=0
) {
    constructor() : this("", 0.0, 0.0, 0)

}