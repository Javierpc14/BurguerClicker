package com.example.hamburguerclicker.modelo

import com.example.hamburguerclicker.R

class Partida {

    var pesoTotal: Double = 0.0
    var tiendas = Tiendas ()

    var logros = arrayListOf(
        Logro( "logro1", R.drawable.logro1,"Alcanza los 100 mg","Pesas los mismo que un gusano") ,
        Logro( "logro2",R.drawable.logro2,"Panadero maestro","Compra 20 panaderias"),
        Logro( "logro3",R.drawable.logro3,"Alcanza los 10 g","Pesas lo mismo que un lapiz"),
        Logro( "logro4",R.drawable.logro4,"Carnicero maestro","Compra 20 carnicerias"),
        Logro( "logro5",R.drawable.logro5,"Alcanza los 700 g","Pesas lo mismo que una almohada"),
        Logro( "logro6",R.drawable.logro6,"Quesero maestro","Compra 20 queserias"),
        Logro( "logro7",R.drawable.logro7,"Alcanza los 20 Kg","Pesas lo mismo que un lince ibérico" ),
        Logro( "logro8",R.drawable.logro8,"Lechuga maestra","Compra 20 lechugas"),
        Logro( "logro9",R.drawable.logro9,"Alcanza los 800 Kg","Pesas lo mismo que un twingo"),
        Logro( "logro10",R.drawable.logro10,"Huerto maestra","Compra 20 huertos"),
        Logro( "logro11",R.drawable.logro11,"Alcanza las 140 T","Pesas lo mismo que una ballena azul"),
        Logro( "logro12",R.drawable.logro12,"Beicon maestro","Compra 20 beicones"),
        Logro( "logro0",R.drawable.logro12,"Primer miligramo","Obten tu primer miligramo")
    )


}
