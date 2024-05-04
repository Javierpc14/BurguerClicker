package com.example.hamburguerclicker.modelo

import com.example.hamburguerclicker.R

class Partida {

    var pesoTotal: Double = 0.0

    var tiendas = arrayListOf(
        Tienda("Panaderia",150.0,7.5,R.drawable.panaderia),
         Tienda("Carniceria",1000.0,50.0,R.drawable.carniceria),
         Tienda("Queseria",130000.0,6500.0,R.drawable.queseria),
         Tienda("Lechuga",2300000.0,115000.0,R.drawable.lechuga),
         Tienda("Huerto",400000000.0,20000000.0,R.drawable.huerto),
         Tienda("Bacon",1000000000.0,50000000.0,R.drawable.bacon)
    )

    var logros = arrayListOf(
        Logro(R.drawable.logro1, "Alcanza los 100 mg", "Pesas los mismo que un gusano") ,
        Logro(R.drawable.logro2, "Compra 20 panaderias","Panadero maestro  "),
        Logro(R.drawable.logro3, "Alcanza los 10 g", "Pesas lo mismo que un lapiz"),
        Logro(R.drawable.logro4, "Compra 20 carnicerias", "Carnicero maestro"),
        Logro(R.drawable.logro5, "Alcanza los 700 g", "Pesas lo mismo que una almohada"),
        Logro(R.drawable.logro6, "Compra 20 queserias", "Quesero maestro"),
        Logro(R.drawable.logro7, "Alcanza los 20 Kg", "Pesas lo mismo que un lince ibérico"),
        Logro(R.drawable.logro8, "Compra 20 lechugas", "Lechuga maestra"),
        Logro(R.drawable.logro9, "Alcanza los 800 Kg", "Pesas lo mismo que un twingo"),
        Logro(R.drawable.logro10, "Compra 20 huertos", "Huerto maestra"),
        Logro(R.drawable.logro11, "Alcanza las 140 T", "Pesas lo mismo que una ballena azul"),
        Logro(R.drawable.logro12, "Compra 20 beicones", "Beicon maestro"),
        Logro(R.drawable.logro12, "Primer miligramo", "Obten tu primer miligramo") ,
    )

    var mejoras = arrayListOf(
        Mejora("prueba",0)
    )

}
